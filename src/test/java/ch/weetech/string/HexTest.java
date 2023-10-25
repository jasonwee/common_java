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
package ch.weetech.string;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

public class HexTest {

    @Test
    public void toByteArray() {
        byte[] tested = Hex.toByteArray("474946383961010001008000001f85e300000021f90400000000002c00000000010001000002024401003b");
        byte[] expected = new BigInteger("474946383961010001008000001f85e300000021f90400000000002c00000000010001000002024401003b", 16).toByteArray();

        assertArrayEquals(expected, tested);
    }

    @Test
    public void toHexString() {
        String expected = "474946383961010001008000001f85e300000021f90400000000002c00000000010001000002024401003b".toUpperCase();
        byte[] input = new BigInteger(expected, 16).toByteArray();
        String tested = Hex.toHexString(input);

        assertEquals(expected, tested);
    }

}
