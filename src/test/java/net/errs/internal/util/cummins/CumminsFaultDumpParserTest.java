package net.errs.internal.util.cummins;

import net.errs.internal.util.cummins.dto.CumminsFaultDump;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CumminsFaultDumpParserTest {
    @Test
    void parsesCumminsFaultDumpShape() {
        String sample = """
                CM2380A ECM Time (Key On Time) 007710:06:58
                Engine Hours 007377:55:52
                Keyoffs 2069
                Engine Serial Number : 74983707
                Customer Unit Number : 197996PS

                5111 Inactive 2 Amber
                Aftertreatment 1 Three Way Catalyst Conversion Efficiency -
                Data Valid But Below Normal Operating Range - Moderately Severe Level
                1 18 6652
                ECM Time (Key On Time)                    007479:45:07     007670:28:50     HHHHHH:MM:SS
                Aftertreatment Catalyst Intake Temperature 1048.9          997.9            °F
                Engine Coolant Temperature                203.3            203.6            °F

                0151 Inactive 1 Red
                Engine Coolant Temperature - Data Valid But Above Normal Operating Range - Most Severe Level
                110 0 0 110
                ECM Time (Key On Time)                    007530:22:26     007530:22:26     HHHHHH:MM:SS
                Engine Coolant Temperature                236.9            236.9            °F
                """;

        CumminsFaultDump dump = new CumminsFaultDumpParser().parseText(sample);

        assertEquals("74983707", dump.getEngineSerialNumber());
        assertEquals("197996PS", dump.getCustomerUnitNumber());
        assertEquals("007377:55:52", dump.getEngineHours());
        assertEquals(2, dump.getFaults().size());

        assertEquals("5111", dump.getFaults().get(0).getCode());
        assertEquals("6652", dump.getFaults().get(0).getSpn());
        assertEquals("18", dump.getFaults().get(0).getJ1939Fmi());
        assertEquals("007479:45:07", dump.getFaults().get(0).getFirstSeen());

        assertEquals("0151", dump.getFaults().get(1).getCode());
        assertEquals("110", dump.getFaults().get(1).getSpn());
        assertEquals("0", dump.getFaults().get(1).getJ1939Fmi());
    }
}
