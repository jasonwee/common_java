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
package ch.weetech.network;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utility class for validating Internet Protocol (IP) address formats.
 * <p>
 * This class leverages core Java network routing APIs to differentiate between IPv4 and IPv6 structures 
 * while filtering out hostname aliases or unintended DNS-resolved strings.
 * </p>
 */
@SuppressWarnings("doclint:missing")
public class IP {

    /**
     * https://stackoverflow.com/a/54514071/775849
     *
     * Check if IP is within an Subnet defined by Network Address and Network Mask
     *
     * @param ip ip address
     * @param net network address
     * @param prefix network mask in integer
     * @return true if ip address whthin network address range
     */
    public static final boolean isIpInSubnet(final String ip, final String net, final int prefix) {
        try {
            final byte[] ipBin = java.net.InetAddress.getByName(ip).getAddress();
            final byte[] netBin = java.net.InetAddress.getByName(net).getAddress();
            if (ipBin.length != netBin.length)
                return false;
            int p = prefix;
            int i = 0;
            while (p >= 8) {
                if (ipBin[i] != netBin[i])
                    return false;
                ++i;
                p -= 8;
            }
            final int m = (65280 >> p) & 255;
            if ((ipBin[i] & m) != (netBin[i] & m))
                return false;

            return true;
        } catch (final Throwable t) {
            return false;
        }
    }

    /**
     * https://stackoverflow.com/a/54514071/775849
     *
     * Check if IP is within an Subnet defined by Network Address and Network Mask
     *
     * @param ip ip address
     * @param net network address
     * @param mask network address mask
     * @return true if ip address whthin network address range
     */
    public static final boolean isIpInSubnet(final String ip, final String net, final String mask) {
        try {
            final byte[] ipBin = java.net.InetAddress.getByName(ip).getAddress();
            final byte[] netBin = java.net.InetAddress.getByName(net).getAddress();
            final byte[] maskBin = java.net.InetAddress.getByName(mask).getAddress();
            if (ipBin.length != netBin.length)
                return false;
            if (netBin.length != maskBin.length)
                return false;
            for (int i = 0; i < ipBin.length; ++i)
                if ((ipBin[i] & maskBin[i]) != (netBin[i] & maskBin[i]))
                    return false;
            return true;
        } catch (final Throwable t) {
            return false;
        }
    }
    
    /**
     * Determines whether the provided input string is a strictly formatted IPv4 address.
     * <p>
     * This validation confirms that the input is a valid IP payload and forces an exact structural 
     * match with the canonical dot-decimal format (e.g., "192.168.1.1"). This prevents false-positive 
     * evaluations on alphanumeric hostnames that resolve to IPv4 backends.
     * </p>
     *
     * @param input the text string to validate
     * @return {@code true} if the string is a valid, raw dot-decimal IPv4 representation; {@code false} otherwise
     */
    public static boolean isIPv4(String input) {
        try {
          InetAddress inetAddress = InetAddress.getByName(input);
          return (inetAddress instanceof Inet4Address) && inetAddress.getHostAddress().equals(input);
        } catch (UnknownHostException ex) {
          return false;
        }
    }

    /**
     * Determines whether the provided input string resolves to or represents an IPv6 address.
     * <p>
     * Note: Because this method lacks the exact string equivalence check found in {@link #isIPv4(String)}, 
     * passing an alphanumeric hostname (e.g., "localhost" or a domain name) that resolves to an 
     * IPv6 record over DNS will cause this method to return {@code true}.
     * </p>
     *
     * @param input the text string or hostname to evaluate
     * @return {@code true} if the input evaluates or resolves to an IPv6 network profile; {@code false} otherwise
     */
    public static boolean isIPv6(String input) {
        try {
          InetAddress inetAddress = InetAddress.getByName(input);
          return (inetAddress instanceof Inet6Address);
        } catch (UnknownHostException ex) {
          return false;
        }
    }

}
