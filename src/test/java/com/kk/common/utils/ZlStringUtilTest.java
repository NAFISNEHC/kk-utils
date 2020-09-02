package com.kk.common.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ZlStringUtilTest {
    @Test
    public void trimStringWith() {
        String str = "abbbba";
        assertEquals("bbbb", ZlStringUtil.trimStringWith(str, 'a'));
        assertNotEquals("bbbba", ZlStringUtil.trimStringWith(str, 'a'));
    }
}
