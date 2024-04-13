package br.com.eztest.core.common;


/*
 * @(#)Base64.java  1.7 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
 * Static methods for translating Base64 encoded strings to byte arrays and vice-versa.
 *
 * @author Josh Bloch
 * @version 1.7, 11/17/05
 * @see java.util.prefs.Preferences* @since 1.4
 */
class Base64 {

    /**
     * This array is a lookup table that translates 6-bit positive integer index values into their "Base64 Alphabet"
     * equivalents as specified in Table 1 of RFC 2045.
     */
    private static final List<Character> INT_TO_BASE_64_ARRAY = [
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'
            , 'i', 'j', 'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7'
            , '8', '9', '+', '/'
    ];


    /**
     * This array is a lookup table that translates unicode characters drawn from the "Base64 Alphabet" (as specified in
     * Table 1 of RFC 2045) into their 6-bit positive integer equivalents. Characters that are not in the Base64
     * alphabet but fall within the bounds of the array are translated to -1.
     */
    private static final List<Byte> BASE_64_TO_INT_ARRAY = [
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
            , -1, 62, -1, -1, -1, 63, 52, 53, 54, 55,
            56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
            , 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
            22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38
            , 39, 40, 41, 42, 43, 44, 45, 46, 47, 48,
            49, 50, 51
    ];


    /**
     * Translates the specified Base64 string (as per Preferences.get(byte[])) into a byte array.
     *
     * @param s
     *            string to be translated.
     * @return translated string
     * @throw IllegalArgumentException if <tt>s</tt> is not a valid Base64 string.
     */
    static byte[] base64ToByteArray(final String s) {
        byte[] alphaToInt = Base64.BASE_64_TO_INT_ARRAY;
        final int sLen = s.length();
        final int numGroups = sLen / 4;
        if (4 * numGroups != sLen) {
            throw new IllegalArgumentException("String length must be a multiple of four.");
        }
        int missingBytesInLastGroup = 0;
        int numFullGroups = numGroups;
        if (sLen != 0) {
            if (s.charAt(sLen - 1) == '=') {
                missingBytesInLastGroup++;
                numFullGroups--;
            }
            if (s.charAt(sLen - 2) == '=') {
                missingBytesInLastGroup++;
            }
        }
        final byte[] result = new byte[3 * numGroups - missingBytesInLastGroup];

        // Translate all full groups from base64 to byte array elements
        int inCursor = 0;
        int outCursor = 0;
        for (int i = 0; i < numFullGroups; i++) {
            final int ch0 = base64toInt(s.charAt(inCursor++), alphaToInt);
            final int ch1 = base64toInt(s.charAt(inCursor++), alphaToInt);
            final int ch2 = base64toInt(s.charAt(inCursor++), alphaToInt);
            final int ch3 = base64toInt(s.charAt(inCursor++), alphaToInt);
            result[outCursor++] = (byte) (ch0 << 2 | ch1 >> 4);
            result[outCursor++] = (byte) (ch1 << 4 | ch2 >> 2);
            result[outCursor++] = (byte) (ch2 << 6 | ch3);
        }

        // Translate partial group, if present
        if (missingBytesInLastGroup != 0) {
            final int ch0 = base64toInt(s.charAt(inCursor++), alphaToInt);
            final int ch1 = base64toInt(s.charAt(inCursor++), alphaToInt);
            result[outCursor++] = (byte) (ch0 << 2 | ch1 >> 4);

            if (missingBytesInLastGroup == 1) {
                final int ch2 = base64toInt(s.charAt(inCursor++), alphaToInt);
                result[outCursor++] = (byte) (ch1 << 4 | ch2 >> 2);
            }
        }
        return result;
    }

    /**
     * Translates the specified character, which is assumed to be in the "Base 64 Alphabet" into its equivalent 6-bit
     * positive integer.
     *
     * @param c
     *            character to be translated.
     * @param alphaToInt
     *            alphabet.
     * @return character translated.
     * @throw IllegalArgumentException or ArrayOutOfBoundsException if c is not in the Base64 Alphabet.
     */
    private static int base64toInt(final char c, final byte[] alphaToInt) {
        final int result = alphaToInt[c];
        if (result < 0) {
            throw new IllegalArgumentException("Illegal character " + c);
        }
        return result;
    }

    /**
     * Translates the specified byte array into a Base64 string as per Preferences.put(byte[]).
     *
     * @param a
     *            array to be translated.
     * @return translated array
     */
    static String byteArrayToBase64(final byte[] a) {
        final int aLen = a.length;
        final int numFullGroups = aLen / 3;
        final int numBytesInPartialGroup = aLen - 3 * numFullGroups;
        final int resultLen = 4 * ((aLen + 2) / 3);
        final StringBuffer result = new StringBuffer(resultLen);
        char[] intToAlpha = Base64.INT_TO_BASE_64_ARRAY;

        // Translate all full groups from byte array elements to Base64
        int inCursor = 0;
        for (int i = 0; i < numFullGroups; i++) {
            final int byte0 = a[inCursor++] & 0xff;
            final int byte1 = a[inCursor++] & 0xff;
            final int byte2 = a[inCursor++] & 0xff;
            result.append(intToAlpha[byte0 >> 2]);
            result.append(intToAlpha[byte0 << 4 & 0x3f | byte1 >> 4]);
            result.append(intToAlpha[byte1 << 2 & 0x3f | byte2 >> 6]);
            result.append(intToAlpha[byte2 & 0x3f]);
        }

        // Translate partial group if present
        if (numBytesInPartialGroup != 0) {
            final int byte0 = a[inCursor++] & 0xff;
            result.append(intToAlpha[byte0 >> 2]);
            if (numBytesInPartialGroup == 1) {
                result.append(intToAlpha[byte0 << 4 & 0x3f]);
                result.append("==");
            } else {
                final int byte1 = a[inCursor++] & 0xff;
                result.append(intToAlpha[byte0 << 4 & 0x3f | byte1 >> 4]);
                result.append(intToAlpha[byte1 << 2 & 0x3f]);
                result.append('=');
            }
        }
        return result.toString();
    }
}
