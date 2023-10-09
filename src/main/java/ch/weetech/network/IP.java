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

public class IP {

    /**
     * https://stackoverflow.com/a/54514071/775849
     *
     * Check if IP is within an Subnet defined by Network Address and Network Mask
     *
     * @param ip
     * @param net
     * @param mask
     * @return
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
     * @param ip
     * @param net
     * @param mask
     * @return
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

}
