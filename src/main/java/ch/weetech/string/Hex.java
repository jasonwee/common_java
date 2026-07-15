/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.weetech.string;

/**
 * A utility class for converting data between hexadecimal strings and byte arrays.
 * <p>
 * This class provides low-level decoding and encoding routines optimized for 
 * performance without relying on external cryptography or XML framing libraries.
 * </p>
 * 
 * @see <a href="https://stackoverflow.com/a/11139098/775849">Stack Overflow Source Algorithm</a>
 */
@SuppressWarnings("doclint:missing")
public class Hex {

	/**
     * Converts a hexadecimal string representation into an equivalent byte array.
     * 
     * https://stackoverflow.com/a/11139098/775849
     * <p>
     * The input string must have an even number of characters and consist only of 
     * valid hexadecimal characters ({@code 0-9}, {@code A-F}, {@code a-f}).
     * </p>
     *
     * @param s the hexadecimal string to decode
     * @return a byte array containing the decoded binary data
     * @throws IllegalArgumentException if the string length is odd or contains non-hexadecimal characters
     */
    public static byte[] toByteArray(String s) {
        final int len = s.length();

        // "111" is not a valid hex encoding.
        if (len % 2 != 0) {
            throw new IllegalArgumentException("hexBinary needs to be even-length: " + s);
        }

        byte[] out = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            int h = hexToBin(s.charAt(i));
            int l = hexToBin(s.charAt(i + 1));
            if (h == -1 || l == -1) {
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + s);
            }

            out[i / 2] = (byte) (h * 16 + l);
        }

        return out;
    }

    /**
     * Converts a single hexadecimal character into its base-10 integer equivalent.
     *
     * @param ch the character to convert (e.g., 'A' or 'a')
     * @return the integer value between 0 and 15, or {@code -1} if the character is invalid
     */
    private static int hexToBin(char ch) {
        if ('0' <= ch && ch <= '9') {
            return ch - '0';
        }
        if ('A' <= ch && ch <= 'F') {
            return ch - 'A' + 10;
        }
        if ('a' <= ch && ch <= 'f') {
            return ch - 'a' + 10;
        }
        return -1;
    }

    /**
     * Pre-compiled lookup table for converting nibbles to uppercase hexadecimal characters.
     */
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    /**
     * Encodes a byte array into a continuous uppercase hexadecimal string.
     *
     * @param data the byte array to encode
     * @return an uppercase hexadecimal string representation of the bytes
     */
    public static String toHexString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

}
