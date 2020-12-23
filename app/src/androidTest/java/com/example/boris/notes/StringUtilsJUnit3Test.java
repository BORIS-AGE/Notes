package com.example.boris.notes;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import com.example.boris.notes.managers.StringUtils;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
class StringUtilsJUnit3Test extends TestCase {
    private final Map<String, byte[]> toHexStringData = new HashMap<>();

    @Test
    protected void setUp() throws Exception {
        toHexStringData.put("", new byte[0]);
        toHexStringData.put("01020d112d7f", new byte[] { 1, 2, 13, 17, 45, 127 });
        toHexStringData.put("00fff21180", new byte[] { 0, -1, -14, 17, -128 });
    }

    protected void tearDown() throws Exception {
        toHexStringData.clear();
    }

    @Test
    public void testToHexString() {
        for (final String expected : toHexStringData.keySet()) {
            final byte[] testData = toHexStringData.get(expected);
            final String actual = StringUtils.toHexString(testData);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testIsEmpty() {
        boolean actual = StringUtils.isEmpty(null);
        assertTrue(actual);

        actual = StringUtils.isEmpty("");
        assertTrue(actual);

        actual = StringUtils.isEmpty(" ");
        assertFalse(actual);

        actual = StringUtils.isEmpty("some string");
        assertFalse(actual);
    }
}
