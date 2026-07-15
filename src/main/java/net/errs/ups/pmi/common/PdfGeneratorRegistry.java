package net.errs.ups.pmi.common;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PdfGeneratorRegistry {

    private final Map<String, PdfGenerator<?>> generators;

    public PdfGeneratorRegistry(List<PdfGenerator<?>> services) {
        this.generators = services.stream()
                .collect(Collectors.toMap(PdfGenerator::supportsType, s -> s));
    }

    public PdfGenerator<?> get(String type) {
        PdfGenerator<?> generator = generators.get(type);
        if (generator == null) {
            throw new IllegalArgumentException("No PMI generator for type: " + type);
        }
        return generator;
    }
}