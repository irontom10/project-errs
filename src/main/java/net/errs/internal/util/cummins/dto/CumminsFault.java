package net.errs.internal.util.cummins.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CumminsFault {
    private String code = "";
    private String status = "";
    private int count;
    private String lamp = "";
    private String description = "";
    private String pid = "";
    private String sid = "";
    private String j1587Fmi = "";
    private String j1939Fmi = "";
    private String spn = "";
    private String firstSeen = "";
    private String lastSeen = "";
    private final List<CumminsFaultParameter> parameters = new ArrayList<>();
    private final List<String> rawLines = new ArrayList<>();

    public String getCode() { return code; }
    public void setCode(String code) { this.code = value(code); }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = value(status); }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public String getLamp() { return lamp; }
    public void setLamp(String lamp) { this.lamp = value(lamp); }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = value(description).replaceAll("\\s+", " "); }
    public String getPid() { return pid; }
    public void setPid(String pid) { this.pid = value(pid); }
    public String getSid() { return sid; }
    public void setSid(String sid) { this.sid = value(sid); }
    public String getJ1587Fmi() { return j1587Fmi; }
    public void setJ1587Fmi(String j1587Fmi) { this.j1587Fmi = value(j1587Fmi); }
    public String getJ1939Fmi() { return j1939Fmi; }
    public void setJ1939Fmi(String j1939Fmi) { this.j1939Fmi = value(j1939Fmi); }
    public String getSpn() { return spn; }
    public void setSpn(String spn) { this.spn = value(spn); }
    public String getFirstSeen() { return firstSeen; }
    public void setFirstSeen(String firstSeen) { this.firstSeen = value(firstSeen); }
    public String getLastSeen() { return lastSeen; }
    public void setLastSeen(String lastSeen) { this.lastSeen = value(lastSeen); }
    public List<CumminsFaultParameter> getParameters() { return parameters; }
    public List<String> getRawLines() { return rawLines; }

    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }

    public String getStatusKey() {
        return status.toLowerCase(Locale.ROOT);
    }

    public String getLampKey() {
        return lamp.toLowerCase(Locale.ROOT);
    }

    public String getSearchBlob() {
        StringBuilder out = new StringBuilder(256);
        append(out, code);
        append(out, status);
        append(out, lamp);
        append(out, description);
        append(out, pid);
        append(out, sid);
        append(out, j1587Fmi);
        append(out, j1939Fmi);
        append(out, spn);
        append(out, firstSeen);
        append(out, lastSeen);
        for (CumminsFaultParameter p : parameters) {
            append(out, p.getName());
            append(out, p.getFirst());
            append(out, p.getLast());
            append(out, p.getUnits());
        }
        return out.toString().toLowerCase(Locale.ROOT);
    }

    private static void append(StringBuilder out, String value) {
        if (value != null && !value.isBlank()) out.append(value).append(' ');
    }

    private static String value(String value) {
        return value == null ? "" : value.trim();
    }
}
