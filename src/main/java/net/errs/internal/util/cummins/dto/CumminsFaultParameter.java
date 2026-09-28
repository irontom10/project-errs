package net.errs.internal.util.cummins.dto;

public class CumminsFaultParameter {
    private final String name;
    private final String first;
    private final String last;
    private final String units;

    public CumminsFaultParameter(String name, String first, String last, String units) {
        this.name = value(name);
        this.first = value(first);
        this.last = value(last);
        this.units = value(units);
    }

    public String getName() { return name; }
    public String getFirst() { return first; }
    public String getLast() { return last; }
    public String getUnits() { return units; }
    public boolean isChanged() { return !first.equals(last); }

    private static String value(String value) {
        return value == null ? "" : value.trim();
    }
}
