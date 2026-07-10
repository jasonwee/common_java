/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.weetech.validator;

/**
 * Validates email addresses by enforcing length constraints and RFC syntax compliance.
 * <p>
 * This class inherits from {@link Validator} and parses string payloads to verify both 
 * the local-part and the domain-part of an email address based on strict structural rules.
 * </p>
 */
@SuppressWarnings("doclint:missing")
public class EmailValidator extends Validator<String> {

	/**
     * Set of special characters excluded from the local part of an unquoted address, 
     * excluding the dot and at-sign characters.
     */
    private static final String specialsNoDotNoAt = "()<>,;:\\\"[]";
    
    /**
     * Combined set of illegal characters for unquoted local parts, including the at-sign separator.
     */
    private static final String specialsNoDot = specialsNoDotNoAt
             + "@";

    /**
     * Validates whether the given string is a syntactically correct email address.
     * <p>
     * Rejects null inputs, empty strings, and inputs exceeding the maximum 
     * standard SMTP limit of 320 characters.
     * </p>
     *
     * @param v the email address string to validate
     * @return {@code true} if the email syntax is valid; {@code false} otherwise
     */
    @Override
    public boolean isValid(String v) {
        if (v == null || v.isEmpty())
            return false;

        if (v.length() >= 320) {
            return false;
        }

        try {
            checkAddress(v, true, true);
        } catch (IllegalArgumentException ex) {
            return false;
        }

        return true;
    }

    /**
     * Core syntax checking routine that parses local-parts, routing paths, and domain labels.
     * 
     * mail-api/api/src/main/java/jakarta/mail/internet/InternetAddress.java
     * <p>
     * Validates character sets, quoting constraints, structural dot configurations, 
     * and literal domain formatting rules.
     * </p>
     *
     * @param addr      the raw email string to inspect
     * @param routeAddr {@code true} if RFC 822 source routing syntax is permitted
     * @param validate  {@code true} if a final domain-part is strictly required
     * @throws IllegalArgumentException if any syntactic structural or character violation is discovered
     */
    private static void checkAddress(String addr, boolean routeAddr,
        boolean validate) throws IllegalArgumentException {

        int i, start = 0;

        if (addr == null)
            throw new IllegalArgumentException("Address is null");
        int len = addr.length();
        if (len == 0)
            throw new IllegalArgumentException("Empty address" + addr);

        /*
         * routeAddr indicates that the address is allowed to have an RFC 822
         * "route".
         */
        if (routeAddr && addr.charAt(0) == '@') {
            /*
             * Check for a legal "route-addr": [@domain[,@domain ...]:]local@domain
             */
            for (start = 0; (i = indexOfAny(addr, ",:", start)) >= 0; start = i + 1) {
                if (addr.charAt(start) != '@')
                    throw new IllegalArgumentException("Illegal route-addr" + addr);
                if (addr.charAt(i) == ':') {
                    // end of route-addr
                    start = i + 1;
                    break;
                }
            }
        }

        /*
         * The rest should be "local@domain", but we allow simply "local" unless
         * called from validate.
         *
         * local-part must follow RFC 822 - no specials except '.' unless quoted.
         */

        char c = (char) -1;
        char lastc = (char) -1;
        boolean inquote = false;
        for (i = start; i < len; i++) {
            lastc = c;
            c = addr.charAt(i);
            // a quoted-pair is only supposed to occur inside a quoted string,
            // but some people use it outside so we're more lenient
            if (c == '\\' || lastc == '\\')
                continue;
            if (c == '"') {
                if (inquote) {
                    // peek ahead, next char must be "@"
                    if (validate && i + 1 < len && addr.charAt(i + 1) != '@')
                        throw new IllegalArgumentException("Quote not at end of local address " + addr);
                    inquote = false;
                } else {
                    if (validate && i != 0)
                        throw new IllegalArgumentException("Quote not at start of local address" + addr);
                    inquote = true;
                }
                continue;
            } else if (c == '\r') {
                // peek ahead, next char must be LF
                if (i + 1 < len && addr.charAt(i + 1) != '\n')
                    throw new IllegalArgumentException("Quoted local address contains CR without LF" + addr);
            } else if (c == '\n') {
                /*
                 * CRLF followed by whitespace is allowed in a quoted string. We
                 * allowed naked LF, but ensure LF is always followed by whitespace
                 * to prevent spoofing the end of the header.
                 */
                 if (i + 1 < len && addr.charAt(i + 1) != ' ' && addr.charAt(i + 1) != '\t')
                     throw new IllegalArgumentException("Quoted local address contains newline without whitespace" + addr);
            }
            if (inquote)
                continue;
            // dot rules should not be applied to quoted-string
            if (c == '.') {
                if (i == start)
                    throw new IllegalArgumentException("Local address starts with dot" + addr);
                if (lastc == '.')
                    throw new IllegalArgumentException("Local address contains dot-dot" + addr);
            }
            if (c == '@') {
                if (i == 0)
                    throw new IllegalArgumentException("Missing local name" + addr);
                if (lastc == '.')
                    throw new IllegalArgumentException("Local address ends with dot" + addr);
                break; // done with local part
            }
            if (c <= 040 || c == 0177)
                throw new IllegalArgumentException("Local address contains control or whitespace" + addr);
            if (specialsNoDot.indexOf(c) >= 0)
                throw new IllegalArgumentException("Local address contains illegal character" + addr);
        }
        if (inquote)
            throw new IllegalArgumentException("Unterminated quote" + addr);

            /*
             * Done with local part, now check domain.
             *
             * Note that the MimeMessage class doesn't remember addresses as separate
             * objects; it writes them out as headers and then parses the headers when
             * the addresses are requested. In order to support the case where a
             * "simple" address is used, but the address also has a personal name and
             * thus looks like it should be a valid RFC822 address when parsed, we
             * only check this if we're explicitly called from the validate method.
             */

            if (c != '@') {
                if (validate)
                    throw new IllegalArgumentException("Missing final '@domain'" + addr);
                return;
            }

            // check for illegal chars in the domain, but ignore domain literals

            start = i + 1;
            if (start >= len)
                throw new IllegalArgumentException("Missing domain" + addr);

            if (addr.charAt(start) == '.')
                throw new IllegalArgumentException("Domain starts with dot" + addr);
            boolean inliteral = false;
            for (i = start; i < len; i++) {
                c = addr.charAt(i);
                if (c == '[') {
                    if (i != start)
                        throw new IllegalArgumentException("Domain literal not at start of domain" + addr);
                    inliteral = true; // domain literal, don't validate
                } else if (c == ']') {
                    if (i != len - 1)
                        throw new IllegalArgumentException("Domain literal end not at end of domain" + addr);
                    inliteral = false;
                } else if (c <= 040 || c == 0177) {
                    throw new IllegalArgumentException("Domain contains control or whitespace" + addr);
                } else {
                    // RFC 2822 rule
                    // if (specialsNoDot.indexOf(c) >= 0)
                    /*
                     * RFC 1034 rule is more strict the full rule is:
                     *
                     * <domain> ::= <subdomain> | " " <subdomain> ::= <label> |
                     * <subdomain> "." <label> <label> ::= <letter> [ [ <ldh-str> ]
                     * <let-dig> ] <ldh-str> ::= <let-dig-hyp> | <let-dig-hyp> <ldh-str>
                     * <let-dig-hyp> ::= <let-dig> | "-" <let-dig> ::= <letter> |
                     * <digit>
                     */
                    if (!inliteral) {
                        if (!(Character.isLetterOrDigit(c) || c == '-' || c == '.'))
                            throw new IllegalArgumentException("Domain contains illegal character" + addr);
                        if (c == '.' && lastc == '.')
                            throw new IllegalArgumentException("Domain contains dot-dot" + addr);
                    }
                }
                lastc = c;
          }
          if (lastc == '.')
              throw new IllegalArgumentException("Domain ends with dot" + addr);
    }

    /**
     * Finds the index of the first occurrence of any character from a search set.
     * 
     * Return the first index of any of the characters in "any" in "s", or -1 if
     * none are found.
     * 
     * <p>
     * Scans the target string from the beginning (index 0).
     * </p>
     *
     * @param s   the string to search within
     * @param any the string containing the set of characters to search for
     * @return the zero-based index of the first matching character; {@code -1} if no match is found
     */
    private static int indexOfAny(String s, String any) {
        return indexOfAny(s, any, 0);
    }

    /**
     * Finds the index of the first occurrence of any character from a search set, starting at a specified index.
     * <p>
     * Iterates through the target string from the start index up to its length. 
     * Catches index out of bound anomalies to return a safe fallback value.
     * </p>
     *
     * @param s     the string to search within
     * @param any   the string containing the set of characters to search for
     * @param start the position in the target string to begin the search from
     * @return the zero-based index of the first matching character at or after the start position; 
     *         {@code -1} if no match is found or if the index is out of bounds
     */
    private static int indexOfAny(String s, String any, int start) {
        try {
            int len = s.length();
            for (int i = start; i < len; i++) {
                if (any.indexOf(s.charAt(i)) >= 0)
                    return i;
            }
            return -1;
        } catch (StringIndexOutOfBoundsException e) {
            return -1;
        }
    }

}
