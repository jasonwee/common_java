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
package ch.weetech.validator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An abstract base class providing contract definitions and shared utility methods for data validation.
 * <p>
 * This class includes common routines for evaluating string properties such as structure,
 * formatting, character composition, and basic security matching for suspicious input patterns.
 * </p>
 *
 * @param <T> the type of data payload this validator inspects
 */
@SuppressWarnings("doclint:missing")
public abstract class Validator<T> {

	/**
     * Validates a given input value against implementation-specific structural constraints.
     *
     * @param v the value payload to evaluate
     * @return {@code true} if the value is structurally sound; {@code false} otherwise
     */
    public abstract boolean isValid(T v);

    /**
     * A predefined reference list of keywords, command tokens, and character flags 
     * commonly tracked for input sanitization or basic command injection checks.
     */
    public final List<String> TOXIC_CHAR  = List.of(
          "AND", "1=1", "OR", "iKO", "nvOpzp", "'A=0", "<", ">",
          "crlfinjection", "SELECT ", "(", ")", "/", "WHEN ", " END", "WAITFOR ", "Set-Cookie:",
          "RLIKE", "CASE", "THEN", "UNION", "NULL", "CONCAT", "HAVING", "waitfor");

    /**
     * Checks if the given string consists entirely of valid Unicode characters.
     * <p>
     * Rejects null or empty strings, or any string containing unassigned Unicode codepoints.
     * </p>
     *
     * @param v the string to evaluate
     * @return {@code true} if every character is defined in Unicode; {@code false} otherwise
     */
    public boolean isValidString(String v) {
        if (v == null || v.isEmpty())
            return false;

        for (int i = 0; i < v.length(); i++) {
            if (!Character.isDefined(v.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a space-delimited string contains a threshold number of discrete toxic tokens.
     * <p>
     * This evaluation splits the target string by spaces and performs exact-match comparisons 
     * against the internal collection of restricted substrings. If the input contains no spaces, 
     * the check returns immediately.
     * </p>
     *
     * @param v              the space-separated text string to scan
     * @param minShouldMatch the minimum number of matching toxic tokens required to trigger a flag
     * @return {@code true} if the matching token count meets or exceeds the threshold; {@code false} otherwise
     */
    public boolean containToxicChar(String v, int minShouldMatch) {
        if (v == null || v.isEmpty())
            return false;

        // only check for string contain spaces
        if (!v.contains(" "))
            return false;

        List<String> matches = Stream.of(v.split(" ")).filter(s -> TOXIC_CHAR.contains(s)).collect(Collectors.toList());

        if (matches.size() >= minShouldMatch) {
            return true;
        }
        return false;
    }

    /**
     * Scans a target string to count how many restricted substrings it contains.
     * <p>
     * Unlike {@link #containToxicChar(String, int)}, this method checks for partial 
     * embedding of restricted phrases across the entire continuous body of the target string.
     * </p>
     *
     * @param v              the text string to scan
     * @param minShouldMatch the minimum number of unique toxic patterns required to trigger a flag
     * @return {@code true} if the count of embedded patterns meets or exceeds the threshold; {@code false} otherwise
     */
    public boolean containToxicCharCheck1(String v, int minShouldMatch) {
       if (v == null || v.isEmpty())
           return false;

       List<String> matches = TOXIC_CHAR.stream().filter(t -> v.contains(t)).collect(Collectors.toList());

       if (matches.size() >= minShouldMatch) {
           return true;
       }
       return false;
   }

    /**
     * Verifies whether the string consists strictly of numeric digits.
     * <p>
     * Note: Returns {@code true} for an empty string because no non-digit character is encountered.
     * </p>
     *
     * @param v the string sequence to evaluate
     * @return {@code true} if all characters are valid digits; {@code false} otherwise
     */
    public boolean isDigit(String v) {
        for (int i = 0; i < v.length(); i++) {
            if (!Character.isDigit(v.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Evaluates a character to ensure it represents a safely visible or standard symbol layout block.
     * 
     * {@code https://www.ssec.wisc.edu/~tomw/java/unicode.html}
     * {@code https://util.unicode.org/UnicodeJsps/character.jsp?a=0x1f60&B1=Show}
     * 
     * <p>
     * Filters out ISO control characters, the absolute unassigned boundary {@code 0xffff}, 
     * and symbols housed under the {@link java.lang.Character.UnicodeBlock#SPECIALS} category block.
     * </p>
     *
     * @param c the target character to inspect
     * @return {@code true} if the character is safely printable; {@code false} otherwise
     */
    public boolean isPrintableChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c)) && c != 0xffff && block != null && block != Character.UnicodeBlock.SPECIALS;
    }

}
