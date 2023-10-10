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
package ch.weetech.network;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class IPTest {

    @Test
    public void testIPV4() {

        assertTrue(IP.isIpInSubnet("192.168.0.3", "192.168.0.0", "255.255.255.0"));
        assertTrue(IP.isIpInSubnet("192.168.2.1", "192.168.2.1", "255.255.255.255"));
        assertTrue(IP.isIpInSubnet("192.168.2.5", "192.168.2.0", "255.255.255.0"));
        assertTrue(IP.isIpInSubnet("10.10.20.3", "10.10.20.0", "255.255.255.252"));

        assertFalse(IP.isIpInSubnet("192.168.2.1", "192.168.2.0", "255.255.255.255"));
        assertFalse(IP.isIpInSubnet("10.10.20.5", "10.10.20.0", "255.255.255.252"));
        assertFalse(IP.isIpInSubnet("10.10.0.1", "192.168.0.0", "255.255.255.0"));
        assertFalse(IP.isIpInSubnet("192.200.0.0", "10.10.20.0", "255.255.255.252"));
    }

    @Test
    public void testIPV6() {
        assertTrue(IP.isIpInSubnet("fe80:0:0:0:0:0:c0a8:11", "fe80:0:0:0:0:0:c0a8:1", 120));
        assertTrue(IP.isIpInSubnet("1::1", "1::", 64));
        assertTrue(IP.isIpInSubnet("2001:db8::4", "2001:db8::", 32));

        assertFalse(IP.isIpInSubnet("fe80:0:0:0:0:0:c0a8:11", "fe80:0:0:0:0:0:c0a8:1", 128));
        assertFalse(IP.isIpInSubnet("2::1", "1::", 64));
    }

}
