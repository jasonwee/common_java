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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Validator<T> {

    public abstract boolean isValid(T v);

    public final List<String> TOXIC_CHAR  = List.of(
          "AND", "1=1", "OR", "iKO", "nvOpzp", "'A=0", "<", ">",
          "crlfinjection", "SELECT ", "(", ")", "/", "WHEN ", " END", "WAITFOR ", "Set-Cookie:",
          "RLIKE", "CASE", "THEN", "UNION", "NULL", "CONCAT", "HAVING", "waitfor");

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

    public boolean containToxicCharCheck1(String v, int minShouldMatch) {
       if (v == null || v.isEmpty())
           return false;

       List<String> matches = TOXIC_CHAR.stream().filter(t -> v.contains(t)).collect(Collectors.toList());

       if (matches.size() >= minShouldMatch) {
           return true;
       }
       return false;
   }

    public boolean isDigit(String v) {
        for (int i = 0; i < v.length(); i++) {
            if (!Character.isDigit(v.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // https://www.ssec.wisc.edu/~tomw/java/unicode.html
    // https://util.unicode.org/UnicodeJsps/character.jsp?a=0x1f60&B1=Show
    public boolean isPrintableChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c)) && c != 0xffff && block != null && block != Character.UnicodeBlock.SPECIALS;
    }

}
