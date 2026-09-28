package net.errs.internal.util.cummins;

import net.errs.internal.util.cummins.dto.CumminsFault;
import net.errs.internal.util.cummins.dto.CumminsFaultDump;
import net.errs.internal.util.cummins.dto.CumminsFaultParameter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CumminsFaultDumpParser {
    private static final long MAX_UPLOAD_BYTES = 25L * 1024L * 1024L;

    private static final Pattern FAULT_HEADER = Pattern.compile(
            "^\\s*(\\d{3,5})\\s+(Active|Inactive)\\s+(\\d+)\\s+([A-Za-z]+)(?:\\s+(.*))?$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern ENGINE_SERIAL = Pattern.compile(
            "^\\s*Engine Serial Number\\s*:\\s*(\\S+)\\s*$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern CUSTOMER_UNIT = Pattern.compile(
            "^\\s*Customer Unit Number\\s*:\\s*(\\S+)\\s*$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern ECM_TIME = Pattern.compile(
            "^(?:\\S+\\s+)?ECM Time \\(Key On Time\\)\\s+(\\d{6}:\\d{2}:\\d{2}).*$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern ENGINE_HOURS = Pattern.compile(
            "^\\s*Engine Hours\\s+(\\d{6}:\\d{2}:\\d{2}).*$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern KEYOFFS = Pattern.compile(
            "^\\s*Keyoffs\\s+(\\d+)\\s*$",
            Pattern.CASE_INSENSITIVE);

    public CumminsFaultDump parse(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Pick an INSITE Faults Information PDF or text dump first.");
        }
        if (file.getSize() > MAX_UPLOAD_BYTES) {
            throw new IllegalArgumentException("Fault dump is larger than 25 MiB.");
        }

        String originalName = file.getOriginalFilename() == null ? "fault-dump" : file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        String text;
        if (looksLikePdf(originalName, file.getContentType(), bytes)) {
            text = extractPdf(bytes);
        } else {
            text = new String(bytes, StandardCharsets.UTF_8);
        }

        CumminsFaultDump dump = parseText(text);
        dump.setSourceFileName(originalName);
        return dump;
    }

    CumminsFaultDump parseText(String text) {
        String normalized = normalize(text);
        List<String> lines = Arrays.asList(normalized.split("\\R", -1));

        CumminsFaultDump dump = new CumminsFaultDump();
        parseGlobalMetadata(lines, dump);

        FaultBuilder current = null;

        for (String line : lines) {
            Matcher header = FAULT_HEADER.matcher(line);

            if (header.matches()) {
                if (current != null) {
                    dump.getFaults().add(current.finish());
                }

                current = new FaultBuilder(
                        header.group(1),
                        header.group(2),
                        parseCount(header.group(3)),
                        header.group(4));

                String remainder = header.group(5);
                if (remainder != null && !remainder.isBlank()) {
                    current.acceptPreamble(remainder);
                }
                continue;
            }

            if (current != null) {
                current.accept(line);
            }
        }

        if (current != null) {
            dump.getFaults().add(current.finish());
        }

        if (dump.getFaults().isEmpty()) {
            throw new IllegalArgumentException(
                    "No Cummins fault records were found. Expected an INSITE 'Faults Information' dump.");
        }

        return dump;
    }

    private static String extractPdf(byte[] bytes) throws IOException {
        try (PDDocument document = Loader.loadPDF(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            stripper.setWordSeparator(" ");
            return stripper.getText(document);
        }
    }

    private static void parseGlobalMetadata(List<String> lines, CumminsFaultDump dump) {
        for (String raw : lines) {
            String line = raw == null ? "" : raw.trim();

            if (FAULT_HEADER.matcher(line).matches()) break;

            Matcher m = ENGINE_SERIAL.matcher(line);
            if (m.matches() && dump.getEngineSerialNumber().isBlank()) {
                dump.setEngineSerialNumber(m.group(1));
                continue;
            }

            m = CUSTOMER_UNIT.matcher(line);
            if (m.matches() && dump.getCustomerUnitNumber().isBlank()) {
                dump.setCustomerUnitNumber(m.group(1));
                continue;
            }

            m = ECM_TIME.matcher(line);
            if (m.matches() && dump.getEcmTime().isBlank()) {
                dump.setEcmTime(m.group(1));
                continue;
            }

            m = ENGINE_HOURS.matcher(line);
            if (m.matches() && dump.getEngineHours().isBlank()) {
                dump.setEngineHours(m.group(1));
                continue;
            }

            m = KEYOFFS.matcher(line);
            if (m.matches() && dump.getKeyoffs().isBlank()) {
                dump.setKeyoffs(m.group(1));
            }
        }

        if (dump.getEngineSerialNumber().isBlank() || dump.getCustomerUnitNumber().isBlank()) {
            for (String raw : lines) {
                String line = raw == null ? "" : raw.trim();

                if (dump.getEngineSerialNumber().isBlank()) {
                    Matcher m = ENGINE_SERIAL.matcher(line);
                    if (m.matches()) dump.setEngineSerialNumber(m.group(1));
                }

                if (dump.getCustomerUnitNumber().isBlank()) {
                    Matcher m = CUSTOMER_UNIT.matcher(line);
                    if (m.matches()) dump.setCustomerUnitNumber(m.group(1));
                }

                if (!dump.getEngineSerialNumber().isBlank() && !dump.getCustomerUnitNumber().isBlank()) break;
            }
        }
    }

    private static boolean looksLikePdf(String fileName, String contentType, byte[] bytes) {
        if (fileName.toLowerCase(Locale.ROOT).endsWith(".pdf")) return true;
        if ("application/pdf".equalsIgnoreCase(contentType)) return true;

        return bytes.length >= 4
                && bytes[0] == '%'
                && bytes[1] == 'P'
                && bytes[2] == 'D'
                && bytes[3] == 'F';
    }

    private static String normalize(String text) {
        if (text == null) return "";

        return text
                .replace('\u00a0', ' ')
                .replace('\u2007', ' ')
                .replace('\u202f', ' ')
                .replace("\r\n", "\n")
                .replace('\r', '\n');
    }

    private static int parseCount(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private static boolean isNoise(String line) {
        String t = line.trim();
        if (t.isBlank()) return true;

        return t.startsWith("Fault Code Status Count Lamp Description")
                || t.equals("Fault Code")
                || t.equals("Status")
                || t.startsWith("PID SID")
                || t.startsWith("J1587")
                || t.startsWith("J1939")
                || t.startsWith("Fault Parameters First Last Units")
                || t.startsWith("Engine Serial Number :")
                || t.startsWith("Customer Unit Number :")
                || t.startsWith("Work Order Name :")
                || t.equals("Faults Information")
                || t.startsWith("INSITE ")
                || t.startsWith("Company Name :")
                || t.startsWith("ECM Image Name :")
                || t.startsWith("Customer Name :")
                || t.matches(".*\\bPage\\s+\\d+\\s+of\\s+\\d+.*");
    }

    private static final class FaultBuilder {
        private final CumminsFault fault = new CumminsFault();
        private final List<String> descriptionParts = new ArrayList<>();
        private final StringBuilder pendingParameterName = new StringBuilder();
        private boolean parametersStarted;

        private FaultBuilder(String code, String status, int count, String lamp) {
            fault.setCode(code);
            fault.setStatus(status);
            fault.setCount(count);
            fault.setLamp(lamp);
        }

        private void accept(String rawLine) {
            if (rawLine == null) return;

            String line = rawLine.stripTrailing();
            if (isNoise(line)) return;

            fault.getRawLines().add(line.trim());

            if (!parametersStarted) {
                if (line.toLowerCase(Locale.ROOT).contains("ecm time (key on time)")) {
                    parametersStarted = true;
                    acceptParameter(line);
                    return;
                }

                acceptPreamble(line);
                return;
            }

            acceptParameter(line);
        }

        private void acceptPreamble(String raw) {
            if (raw == null || raw.isBlank()) return;

            IdTail ids = IdTail.tryParse(raw);
            if (ids != null) {
                applyIds(ids);
                if (!ids.prefix().isBlank()) descriptionParts.add(ids.prefix());
            } else {
                descriptionParts.add(raw.trim());
            }
        }

        private void acceptParameter(String raw) {
            String line = raw.trim();
            if (line.isBlank() || isNoise(line)) return;

            String[] columns = line.split("\\s{2,}");

            if (columns.length >= 3) {
                String name = columns[0].trim();

                if (pendingParameterName.length() > 0) {
                    name = pendingParameterName + " " + name;
                    pendingParameterName.setLength(0);
                }

                String first = columns[1].trim();
                String last = columns[2].trim();
                String units = columns.length > 3
                        ? String.join(" ", Arrays.copyOfRange(columns, 3, columns.length)).trim()
                        : "";

                CumminsFaultParameter parameter = new CumminsFaultParameter(name, first, last, units);
                fault.getParameters().add(parameter);

                if (name.toLowerCase(Locale.ROOT).contains("ecm time (key on time)")) {
                    fault.setFirstSeen(first);
                    fault.setLastSeen(last);
                }
                return;
            }

            if (pendingParameterName.length() > 0) pendingParameterName.append(' ');
            pendingParameterName.append(line);
        }

        private void applyIds(IdTail ids) {
            List<String> n = ids.numbers();
            int size = n.size();

            fault.setSpn(n.get(size - 1));
            fault.setJ1939Fmi(n.get(size - 2));
            fault.setJ1587Fmi(n.get(size - 3));

            if (size == 4) {
                fault.setPid(n.get(0));
            } else if (size >= 5) {
                fault.setPid(n.get(size - 5));
                fault.setSid(n.get(size - 4));
            }
        }

        private CumminsFault finish() {
            fault.setDescription(String.join(" ", descriptionParts));
            return fault;
        }
    }

    private record IdTail(String prefix, List<String> numbers) {
        private static IdTail tryParse(String raw) {
            String line = raw.trim();
            if (line.isBlank()) return null;

            String[] tokens = line.split("\\s+");
            List<String> numbers = new ArrayList<>();
            int i = tokens.length - 1;

            while (i >= 0 && numbers.size() < 5 && tokens[i].matches("\\d+")) {
                numbers.add(0, tokens[i]);
                i--;
            }

            if (numbers.size() < 3) return null;

            String prefix = i >= 0
                    ? String.join(" ", Arrays.copyOfRange(tokens, 0, i + 1)).trim()
                    : "";

            return new IdTail(prefix, numbers);
        }
    }
}
