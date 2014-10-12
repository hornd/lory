package me.lory.irc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.StringReader;

import me.lory.irc.Configuration.Field;

import org.junit.Test;

public class TestConfigurationParser {

    private static final String LS = System.getProperty("line.separator");

    /**
     * Verify basic parsing.
     * 
     * @throws Exception
     */
    @Test
    public void testValidConfig() throws Exception {
        String t = "NICK=nick" + LS + "NAME=name with spaces";

        BufferedReader reader = new BufferedReader(new StringReader(t));
        Configuration.Parser parser = new Configuration.Parser(reader);
        Configuration result = parser.parse();

        assertTrue(result.containsField(Field.NICK));
        assertTrue(result.containsField(Field.NAME));

        assertEquals("nick", result.getValue(Field.NICK));
        assertEquals("name with spaces", result.getValue(Field.NAME));
    }

    /**
     * Verify that invalid entries are skipped.
     * 
     * @throws Exception
     */
    @Test
    public void testInvalidEntrySkipped() throws Exception {
        String t = "NICK=nick" + LS + "NNAME=name" + LS + "NAME=another";

        BufferedReader reader = new BufferedReader(new StringReader(t));
        Configuration.Parser parser = new Configuration.Parser(reader);
        Configuration result = parser.parse();

        assertTrue(result.containsField(Field.NICK));
        assertTrue(result.containsField(Field.NAME));

        assertEquals("nick", result.getValue(Field.NICK));
        assertEquals("another", result.getValue(Field.NAME));
    }

    /**
     * Verify missing entries cause no issues.
     * 
     * @throws Exception
     */
    @Test
    public void testEntriesMissing() throws Exception {
        String t = "NAME=another";

        BufferedReader reader = new BufferedReader(new StringReader(t));
        Configuration.Parser parser = new Configuration.Parser(reader);
        Configuration result = parser.parse();

        assertFalse(result.containsField(Field.NICK));
        assertTrue(result.containsField(Field.NAME));

        assertNull(result.getValue(Field.NICK));
        assertEquals("another", result.getValue(Field.NAME));
    }
}
