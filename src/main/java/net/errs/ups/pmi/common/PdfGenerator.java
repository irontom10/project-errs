package net.errs.ups.pmi.common;

import java.io.IOException;

public interface PdfGenerator<T> {
    String supportsType();
    byte[] generate(T request) throws IOException;
}