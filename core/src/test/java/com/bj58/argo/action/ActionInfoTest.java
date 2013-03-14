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
//package com.bj58.argo.action;
//
//import com.bj58.argo.BeatContext;
//import com.bj58.argo.client.ClientContext;
//import com.bj58.argo.internal.ActionInfo;
//import com.bj58.argo.route.RouteBag;
//import com.bj58.argo.thirdparty.AntPathMatcher;
//import com.bj58.argo.thirdparty.PathMatcher;
//import com.google.common.collect.Maps;
//import junit.framework.Assert;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import javax.servlet.http.HttpServletRequest;
//
//import java.util.Map;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyMapOf;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// * @author Service Platform Architecture Team (spat@58.com)
// */
//public class ActionInfoTest {
//    @Mock
//    private ActionInfo actionInfo;
//
//    @BeforeMethod
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testMatch2() {
//    	PathMatcher pathMatcher = new AntPathMatcher();
//    	when(actionInfo.getPathMatcher()).thenReturn(pathMatcher);
//    	when(actionInfo.getPathPattern()).thenReturn("/{x}/{y}");
//
//
//    	RouteBag routeBag = mock(RouteBag.class);
//    	when(routeBag.getSimplyPath()).thenReturn("abc/def");
//
//    	Map<String, String> variables = Maps.newHashMap();
//
//    	when(actionInfo.match(any(routeBag.getClass()), anyMapOf(String.class, String.class))).thenCallRealMethod();
//    	//when(actionInfo.match(routeBag, variables)).thenCallRealMethod();
//    	Assert.assertFalse(actionInfo.match(routeBag, variables));
//
//    	when(actionInfo.getPathPattern()).thenReturn("**");
//    	Assert.assertTrue(actionInfo.match(routeBag, variables));
//    	Assert.assertEquals(0, variables.size());
//
//    	when(actionInfo.getPathPattern()).thenReturn("");
//    	Assert.assertFalse(actionInfo.match(routeBag, variables));
//
//    	when(routeBag.getSimplyPath()).thenReturn("");
//    	when(actionInfo.getPathPattern()).thenReturn("{x}/{y}/");
//    	Assert.assertFalse(actionInfo.match(routeBag, variables));
//
//    	when(routeBag.getSimplyPath()).thenReturn("xxx/yyy/");
//    	when(actionInfo.getPathPattern()).thenReturn("{x}/{y}/");
//    	Assert.assertTrue(actionInfo.match(routeBag, variables));
//
//    	when(routeBag.getSimplyPath()).thenReturn("xxx/yyy/");
//    	when(actionInfo.getPathPattern()).thenReturn("{x}/{y}");
//    	Assert.assertFalse(actionInfo.match(routeBag, variables));
//
//    	when(routeBag.getSimplyPath()).thenReturn("xxx/yyy");
//    	when(actionInfo.getPathPattern()).thenReturn("{x}/{y}/");
//    	Assert.assertFalse(actionInfo.match(routeBag, variables));
//
//    	when(routeBag.getSimplyPath()).thenReturn("xxx/yyy/");
//    	when(actionInfo.getPathPattern()).thenReturn("{x}/{y}/*");
//    	Assert.assertTrue(actionInfo.match(routeBag, variables));
//
//    	when(routeBag.getSimplyPath()).thenReturn("xxx/yyy/c");
//    	when(actionInfo.getPathPattern()).thenReturn("{x}/{y}/c/a");
//    	Assert.assertFalse(actionInfo.match(routeBag, variables));
//
//    	when(routeBag.getSimplyPath()).thenReturn("xxx/");
//    	when(actionInfo.getPathPattern()).thenReturn("{x}/{y}/{z}");
//    	Assert.assertFalse(actionInfo.match(routeBag, variables));
//
//    	when(routeBag.getSimplyPath()).thenReturn("xxx/");
//    	when(actionInfo.getPathPattern()).thenReturn("{x}/*/{z}");
//    	Assert.assertFalse(actionInfo.match(routeBag, variables));
//
//    	when(routeBag.getSimplyPath()).thenReturn("xxx");
//    	when(actionInfo.getPathPattern()).thenReturn("{x}/*/{z}");
//    	Assert.assertFalse(actionInfo.match(routeBag, variables));
//
//    	when(routeBag.getSimplyPath()).thenReturn("xxx/yyy");
//    	when(actionInfo.getPathPattern()).thenReturn("{x}/{y}/*");
//    	Assert.assertFalse(actionInfo.match(routeBag, variables));
//
//    	when(routeBag.getSimplyPath()).thenReturn("xxx/yyy/");
//    	when(actionInfo.getPathPattern()).thenReturn("{x}/{y}/{z}");
//    	Assert.assertFalse(actionInfo.match(routeBag, variables));
//
//    	when(routeBag.getSimplyPath()).thenReturn("xxx/yyy/zzz");
//    	when(actionInfo.getPathPattern()).thenReturn("**/**");
//    	Assert.assertTrue(actionInfo.match(routeBag, variables));
//
//    }
//
//    @Test
//    public void testMatch() {
//        BeatContext beatContext = mock(BeatContext.class);
//
//        ClientContext clientContext = mock(ClientContext.class);
//        when(beatContext.getClient()).thenReturn(clientContext);
//        when(clientContext.getRelativeUrl()).thenReturn("/abc/efg/");
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(beatContext.getRequest()).thenReturn(request);
//        when(request.getMethod()).thenReturn("GET");
//
//        RouteBag bag = RouteBag.getLogger(beatContext);
//
//        Assert.assertEquals(bag.isGet(), true);
//        Assert.assertEquals(bag.isPost(), false);
//        Assert.assertEquals(bag.getPath(), "/abc/efg/");
//        Assert.assertEquals(bag.getSimplyPath(), "/abc/efg");
//
//        PathMatcher pathMatcher = new AntPathMatcher();
//        when(actionInfo.getPathMatcher()).thenReturn(pathMatcher);
//
//        when(actionInfo.match(any(bag.getClass()), anyMapOf(String.class, String.class))).thenCallRealMethod();
//
//        Map<String, String> variables = Maps.newHashMap();
//
//        when(actionInfo.getPathPattern()).thenReturn("/{x}/{y}");
//        Assert.assertTrue(actionInfo.match(bag, variables));
//
//        Assert.assertEquals(variables.get("x"), "abc");
//        Assert.assertEquals(variables.get("y"), "efg");
//
//        variables = Maps.newHashMap();
//        when(actionInfo.getPathPattern()).thenReturn("/abc/{y}");
//        Assert.assertTrue(actionInfo.match(bag, variables));
//        Assert.assertEquals(variables.get("y"), "efg");
//
//        variables = Maps.newHashMap();
//        when(actionInfo.getPathPattern()).thenReturn("/abc/efg");
//        Assert.assertTrue(actionInfo.match(bag, variables));
//        Assert.assertEquals(variables.size(), 0);
//
//        variables = Maps.newHashMap();
//        when(actionInfo.getPathPattern()).thenReturn("/abc111/efg");
//        Assert.assertFalse(actionInfo.match(bag, variables));
//
//    }
//
//    @Test
//    public void testMatchHttpMethod() {
////        ActionInfo actionInfo = mock(ActionInfo.class);
//        RouteBag bag = mock(RouteBag.class);
//        when(actionInfo.matchHttpMethod(bag)).thenCallRealMethod();
//
//        when(actionInfo.isGet()).thenReturn(true);
//        when(actionInfo.isPost()).thenReturn(false);
//        when(bag.isGet()).thenReturn(true);
//        when(bag.isPost()).thenReturn(false);
//
//        boolean result = actionInfo.matchHttpMethod(bag);
//        Assert.assertTrue(result);
//
//        when(actionInfo.isGet()).thenReturn(false);
//        Assert.assertFalse(actionInfo.matchHttpMethod(bag));
//
//        when(actionInfo.isPost()).thenReturn(true);
//        Assert.assertFalse(actionInfo.matchHttpMethod(bag));
//
//        when(bag.isGet()).thenReturn(false);
//        when(bag.isPost()).thenReturn(true);
//        Assert.assertTrue(actionInfo.matchHttpMethod(bag));
//
//    }
//
//
//
//}
