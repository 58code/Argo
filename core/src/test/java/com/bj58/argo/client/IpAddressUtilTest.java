/*
*  Copyright Beijing 58 Information Technology Co.,Ltd.
*
*  Licensed to the Apache Software Foundation (ASF) under one
*  or more contributor license agreements.  See the NOTICE file
*  distributed with this work for additional information
*  regarding copyright ownership.  The ASF licenses this file
*  to you under the Apache License, Version 2.0 (the
*  "License"); you may not use this file except in compliance
*  with the License.  You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package com.bj58.argo.client;


import com.google.common.net.InetAddresses;
import org.testng.Assert;
import org.testng.annotations.Test;
import sun.net.util.IPAddressUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class IpAddressUtilTest {

    @Test
    public void testIpV4() {

        byte[] ip = IPAddressUtil.textToNumericFormatV4("211.151.115.9");

        Assert.assertEquals(ip, new byte[]{211 - 256, 151 - 256, 115, 9});

        ip = IPAddressUtil.textToNumericFormatV4("bj.58.com");

        Assert.assertNull(ip);
    }

    @Test
    public void test() {

    }

    @Test
    public void testIsPrivateAddress() throws UnknownHostException {
//        byte[] rawAddress = { 10, 0, 0, 5 };
//
//        Inet4Address inet4Address = (Inet4Address) InetAddress.getByAddress(rawAddress);
//
//        System.out.println(inet4Address.isAnyLocalAddress());
//        System.out.println(inet4Address.isSiteLocalAddress());
//
//        rawAddress = new byte[] { 192 - 256, 168 - 256, 0, 5 };
//
//        inet4Address = (Inet4Address) InetAddress.getByAddress(rawAddress);
//
//        System.out.println(inet4Address.isAnyLocalAddress());
//        System.out.println(inet4Address.isSiteLocalAddress());
//
//        rawAddress = new byte[] { 211 - 256, 151 - 256, 70, 5 };
//
//        inet4Address = (Inet4Address) InetAddress.getByAddress(rawAddress);
//
//        System.out.println(inet4Address.isAnyLocalAddress());
//        System.out.println(inet4Address.isSiteLocalAddress());
    }
}
