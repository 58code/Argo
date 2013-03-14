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
package com.bj58.argo.utils;

import com.bj58.argo.ArgoException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class PathUtilsTest {

    @Test
    public void testCombine() {
        Assert.assertEquals(PathUtils.combine("", ""), "/");
        Assert.assertEquals(PathUtils.combine("/", ""), "/");
        Assert.assertEquals(PathUtils.combine("/", "/"), "/");
        Assert.assertEquals(PathUtils.combine("", "/"), "/");
        Assert.assertEquals(PathUtils.combine("/opt", "web"), "/opt/web");
        Assert.assertEquals(PathUtils.combine("/opt/", "web"), "/opt/web");
        Assert.assertEquals(PathUtils.combine("/opt/", "/web"), "/opt/web");
        Assert.assertEquals(PathUtils.combine("/opt", "/web"), "/opt/web");
        Assert.assertEquals(PathUtils.combine("opt", "/web"), "opt/web");
        Assert.assertEquals(PathUtils.combine("opt", "/web/"), "opt/web/");
    }

    @Test
    public void testSimplyWithoutPrefix() {
        String url = "/";
        Assert.assertEquals(PathUtils.simplyWithoutPrefix(url), "");
        Assert.assertEquals(PathUtils.simplyWithoutSuffix(url), "");

        url = "/opt";
        Assert.assertEquals(PathUtils.simplyWithoutPrefix(url), "opt");
        Assert.assertEquals(PathUtils.simplyWithoutSuffix(url), "/opt");

        url = "/opt/";
        Assert.assertEquals(PathUtils.simplyWithoutPrefix(url), "opt/");
        Assert.assertEquals(PathUtils.simplyWithoutSuffix(url), "/opt");

        url = "opt/";
        Assert.assertEquals(PathUtils.simplyWithoutPrefix(url), "opt/");
        Assert.assertEquals(PathUtils.simplyWithoutSuffix(url), "opt");
        
        url = "";
        Assert.assertEquals(PathUtils.simplyWithoutPrefix(url), "");
        Assert.assertEquals(PathUtils.simplyWithoutSuffix(url), "");
    }

    @Test
    public void testRelativePath() {
        File parent = new File("/");
        File child = new File("/");

        Assert.assertEquals(PathUtils.relativePath(parent, child), "/");

        parent = new File("/");
        child = new File("/opt");
        Assert.assertEquals(PathUtils.relativePath(parent, child), "/opt");

        parent = new File("/");
        child = new File("/opt/");
        Assert.assertEquals(PathUtils.relativePath(parent, child), "/opt");

        parent = new File("/opt/");
        child = new File("/opt");
        Assert.assertEquals(PathUtils.relativePath(parent, child), "/");

        parent = new File("/opt/");
        child = new File("/opt/web");
        Assert.assertEquals(PathUtils.relativePath(parent, child), "/web");

        parent = new File("/opt/");
        child = new File("/opt/web/");
        Assert.assertEquals(PathUtils.relativePath(parent, child), "/web");


        parent = new File("/opt/");
        child = new File("/");
        Throwable throwable = testRelativePathException(parent, child);
        Assert.assertNotNull(throwable);

        parent = new File("/opt/");
        child = new File("/op1");
        throwable = testRelativePathException(parent, child);
        Assert.assertNotNull(throwable);

        parent = new File("/opt/");
        child = new File("/op1/");
        throwable = testRelativePathException(parent, child);
        Assert.assertNotNull(throwable);
    }

    public Throwable testRelativePathException(File parent, File child) {
        try {
            PathUtils.relativePath(parent, child);
        } catch (Throwable e) {
            return e;
        }

        fail();
        return null;

    }

    @Test
    public void testContains() {
        File parent = new File("/");
        File child = new File("/");

        Assert.assertTrue(PathUtils.contains(parent, child));

        parent = new File("/");
        child = new File("/opt");
        Assert.assertTrue(PathUtils.contains(parent, child));

        parent = new File("/");
        child = new File("/opt/");
        Assert.assertTrue(PathUtils.contains(parent, child));

        parent = new File("/opt/");
        child = new File("/opt");
        Assert.assertTrue(PathUtils.contains(parent, child));

        parent = new File("/opt/");
        child = new File("/opt/web");
        Assert.assertTrue(PathUtils.contains(parent, child));

        parent = new File("/opt/");
        child = new File("/opt/web/");
        Assert.assertTrue(PathUtils.contains(parent, child));

        parent = new File("/opt/");
        child = new File("/");
        Assert.assertFalse(PathUtils.contains(parent, child));

        parent = new File("/opt/");
        child = new File("/op1");
        Assert.assertFalse(PathUtils.contains(parent, child));

        parent = new File("/opt/");
        child = new File("/op1/");
        Assert.assertFalse(PathUtils.contains(parent, child));

        parent = new File("/op/");
        child = new File("/opt/");
        Assert.assertFalse(PathUtils.contains(parent, child));
    }

//    @Test
//    public void testConventPathMock() {
//        File file = mock(File.class);
//
//
//        when(file.getAbsolutePath()).thenReturn(File.separator);
//        Assert.assertEquals(PathUtils.conventPath(file), File.separator);
//
//        when(file.getAbsolutePath()).thenReturn("/opt/web");
//        Assert.assertEquals(PathUtils.conventPath(file), "/opt/web" + File.separator);
//
//        when(file.getAbsolutePath()).thenReturn("/opt/web" + File.separator);
//        Assert.assertEquals(PathUtils.conventPath(file), "/opt/web" + File.separator);
//
//    }

    @Test
    public void testConventPath() {
        File file = new File("/");
        String path = PathUtils.conventPath(file);
        Assert.assertTrue(path.endsWith(File.separator));

        if (path.length() >= 2)
            Assert.assertNotEquals(path.charAt(path.length() - 2), File.separatorChar);

        file = new File("/opt/web");
        path = PathUtils.conventPath(file);
        Assert.assertTrue(path.endsWith(File.separator));
        Assert.assertNotEquals(path.charAt(path.length() - 2), File.separatorChar);

        file = new File("/opt/web/");
        path = PathUtils.conventPath(file);
        Assert.assertTrue(path.endsWith(File.separator));
        Assert.assertNotEquals(path.charAt(path.length() - 2), File.separatorChar);

    }

    @Test
    public void testSimplyPath() {

        Assert.assertEquals("", PathUtils.simply(""));

        Assert.assertEquals("/abc.txt", PathUtils.simply("\\abc.txt"));
        Assert.assertEquals("/pathFolder/abc.txt", PathUtils.simply("\\pathFolder\\abc.txt"));

        Assert.assertEquals("/abc.txt", PathUtils.simply("/abc.txt"));
        Assert.assertEquals("/abc.txt/", PathUtils.simply("/abc.txt/"));
        Assert.assertEquals("abc.txt", PathUtils.simply("abc.txt"));
        Assert.assertEquals(".abc.txt", PathUtils.simply(".abc.txt"));
        Assert.assertEquals(".abc.txt.", PathUtils.simply(".abc.txt."));
        Assert.assertEquals(".abc.txt", PathUtils.simply(".abc.txt?"));
        Assert.assertEquals("/abc.txt", PathUtils.simply("/abc.txt?"));
        Assert.assertEquals("abc.txt", PathUtils.simply("abc.txt?"));
        Assert.assertEquals(".abc.txt", PathUtils.simply(".abc.txt?abcd"));
        Assert.assertEquals(".abc.txt", PathUtils.simply(".abc.txt#abcd"));

        Assert.assertEquals("/pathFolder/abc.txt", PathUtils.simply("/pathFolder/abc.txt"));
        Assert.assertEquals("/.pathFolder/abc.txt", PathUtils.simply("/.pathFolder/abc.txt"));
        Assert.assertEquals("/....pathFolder../abc.txt", PathUtils.simply("/....pathFolder../abc.txt"));
        Assert.assertEquals("/....pathFolder../abc.txt", PathUtils.simply("/....pathFolder../abc.txt?"));
        Assert.assertEquals("/....pathFolder../abc.txt", PathUtils.simply("/....pathFolder../abc.txt?abc"));
        Assert.assertEquals("/....pathFolder../abc.txt", PathUtils.simply("/....pathFolder../abc.txt#abc"));
        Assert.assertEquals("/....pathFolder../abc.txt", PathUtils.simply("/....pathFolder../abc.txt#abc<>"));
        Assert.assertEquals("/春节/", PathUtils.simply("/春节/"));
        
        ArgoException e = throwCombine("..");
        Assert.assertEquals("Illegal URL path!\ncontext: {url=..}", e.getMessage());
        Assert.assertNotNull(e);
        Assert.assertNotNull(throwCombine("."));
        Assert.assertNotNull(throwCombine( ".\\"));
        Assert.assertNotNull(throwCombine("<"));
        Assert.assertNotNull(throwCombine(">"));

        Assert.assertNotNull(throwCombine("/aaa/../abc"));

        Assert.assertNotNull(throwCombine("//aaa"));

        Assert.assertNotNull(throwCombine("aaa//bb"));
        Assert.assertNotNull(throwCombine("aaa//"));

        int count = 0;
        for(char c = 'a'; c <= 'z'; c++, count ++)
            Assert.assertEquals("" + c, PathUtils.simply(c + ""));

        Assert.assertEquals(26, count);

        count = 0;
        for(char c = 'A'; c <= 'Z'; c++, count ++)
            Assert.assertEquals("" + c, PathUtils.simply(c + ""));

        Assert.assertEquals(26, count);

        count = 0;
        for(char c = '0'; c <= '9'; c++, count ++)
            Assert.assertEquals("" + c, PathUtils.simply(c + ""));

        Assert.assertEquals(10, count);
    }

    private ArgoException throwCombine(String url) {
        try {
            PathUtils.simply(url);

        }catch (ArgoException e) {
            return e;
        }
        fail();
        return null;
    }
}
