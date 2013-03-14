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
package com.bj58.argo;

import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ArgoExceptionTest {

    @Test
    public void testMessage() {
        ArgoException ex = ArgoException.newBuilder("This is a message.")
                .build();

        Assert.assertEquals("This is a message.", ex.getMessage());
        Assert.assertNull(ex.getCause());
    }


    @Test
    public void testCause() {
        Throwable ta = new RuntimeException("runtime exception.");
        ArgoException ex = ArgoException.newBuilder(ta).build();
        Assert.assertEquals("", ex.getMessage());
        Assert.assertEquals(ta, ex.getCause());
    }

    @Test
    public void testBoth() {
        Throwable ta = new RuntimeException();
        String message = "This is a message.";

        ArgoException ex = ArgoException.newBuilder(message, ta)
                .build();

        Assert.assertEquals(message, ex.getMessage());
        Assert.assertEquals(ta, ex.getCause());

    }

    @Test
    public void testContext() {
        ArgoException ex = ArgoException.newBuilder()
                .addContextVariable("url", "http://weibo.com/duoway/")
                .addContextVariable("email", "jun.ren@gmail.com")
                .build();

        Assert.assertEquals("\ncontext: {url=http://weibo.com/duoway/, email=jun.ren@gmail.com}", ex.getMessage());

    }
}
