///*
//*  Copyright Beijing 58 Information Technology Co.,Ltd.
//*
//*  Licensed to the Apache Software Foundation (ASF) under one
//*  or more contributor license agreements.  See the NOTICE file
//*  distributed with this work for additional information
//*  regarding copyright ownership.  The ASF licenses this file
//*  to you under the Apache License, Version 2.0 (the
//*  "License"); you may not use this file except in compliance
//*  with the License.  You may obtain a copy of the License at
//*
//*        http://www.apache.org/licenses/LICENSE-2.0
//*
//*  Unless required by applicable law or agreed to in writing,
//*  software distributed under the License is distributed on an
//*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
//*  KIND, either express or implied.  See the License for the
//*  specific language governing permissions and limitations
//*  under the License.
//*/
//package com.bj58.argo.convention;
//
//import com.bj58.argo.ArgoException;
//import com.bj58.argo.internal.DefaultGroupConvention;
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.ImmutableMap;
//import com.google.inject.Module;
//
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// * @author Service Platform Architecture Team (spat@58.com)
// */
//public class DefaultGroupConventionTest {
//
//    @Test
//    public void testParseProjectConvention() {
//
//        GroupConventionAnnotation groupConventionAnnotation = mock(GroupConventionAnnotation.class);
//
//        when(groupConventionAnnotation.projectConventionClass()).thenReturn(MyProjectWithId.class.getName());
//        ProjectConvention projectConvention = DefaultGroupConvention.parseProjectConvention(groupConventionAnnotation);
//
//        Assert.assertEquals(projectConvention.id(), "myProjectId");
//        Assert.assertEquals(projectConvention.modules(), ImmutableList.of());
//    }
//
//    @Test
//    public void testParseProjectConventionImpl() {
//
//        GroupConventionAnnotation groupConventionAnnotation = mock(GroupConventionAnnotation.class);
//
//        when(groupConventionAnnotation.projectConventionClass()).thenReturn(MyProjectImpl.class.getName());
//        ProjectConvention projectConvention = DefaultGroupConvention1.parseProjectConvention(groupConventionAnnotation);
//
//        Assert.assertEquals(projectConvention.id(), "myProjectImpl");
//        Assert.assertNull(projectConvention.modules());
//    }
//
//    @Test
//    public void testParseProjectConvention_NoExistClass() {
//        GroupConventionAnnotation groupConventionAnnotation = mock(GroupConventionAnnotation.class);
//        when(groupConventionAnnotation.projectConventionClass()).thenReturn("noExistClass");
//        ProjectConvention projectConvention = DefaultGroupConvention1.parseProjectConvention(groupConventionAnnotation);
//        Assert.assertEquals(projectConvention.id(), "");
//        Assert.assertEquals(projectConvention.modules(), ImmutableList.of());
//    }
//
//    @Test(expectedExceptions = ArgoException.class
//    		, expectedExceptionsMessageRegExp = "Class com.bj58.argo.convention.DefaultGroupConventionTest not annotate ProjectConventionAnnotation")
//    public void testParseProjectConvention_Exception() {
//        GroupConventionAnnotation groupConventionAnnotation = mock(GroupConventionAnnotation.class);
//
//        when(groupConventionAnnotation.projectConventionClass()).thenReturn(this.getClass().getName());
//        DefaultGroupConvention1.parseProjectConvention(groupConventionAnnotation);
//
//        Assert.fail();
//    }
//
//    @Test
//    public void testMatchPath() {
//
//        Map<String, String> paths = ImmutableMap.<String, String>builder()
//                .put("a", "aa")
//                .put("b", "bb")
//                .put("c", "{a}")
//                .put("d", "{c}")
//                .put("e", "{b}")
//                .build();
//
//        Map<String, String> result = DefaultGroupConvention1.matchPath(paths);
//
//        Assert.assertEquals(5, result.size());
//
//        Assert.assertEquals("aa", result.get("a"));
//        Assert.assertEquals("bb", result.get("b"));
//        Assert.assertEquals("aa", result.get("c"));
//        Assert.assertEquals("aa", result.get("d"));
//        Assert.assertEquals("bb", result.get("e"));
//
//    }
//
//    @Test(expectedExceptions = ArgoException.class)
//    public void testMatchPath_NestedExpression() {
//
//        Map<String, String> paths = ImmutableMap.<String, String>builder()
//                .put("a", "aa")
//                .put("b", "bb")
//                .put("c", "{d}")
//                .put("d", "{e}")
//                .put("e", "{c}")
//                .build();
//
//        Map<String, String> result = DefaultGroupConvention1.matchPath(paths);
//
//
//    }
//
//
//
//
//    @ProjectConventionAnnotation(id = "myProjectId")
//    public static class MyProjectWithId {
//
//    }
//
//    public static class MyProjectImpl implements ProjectConvention {
//
//		@Override
//		public String id() {
//			return "myProjectImpl";
//		}
//
//		@Override
//		public List<Module> modules() {
//			return null;
//		}
//
//    }
//
//
//}
