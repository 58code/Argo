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
//package com.bj58.argo.learn.injector;
//
//import com.google.inject.AbstractModule;
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import com.google.inject.Provides;
//import org.testng.annotations.Test;
//
//import javax.inject.Inject;
//import javax.inject.Singleton;
//
///**
// * @author Service Platform Architecture Team (spat@58.com)
// */
//public class ProvidersTest {
//
//    public final static Injector injector = Guice.createInjector(new MyModule());
//
//
//    @Test
//    public void test() {
//        C1 c1 = injector.getInstance(C1.class);
//
//        System.out.println("QQQQQQQQQQQQQQQQQQQQQQ");
//        System.out.println(c1.c3().c2());
//    }
//
//
//
//    public static class MyModule extends AbstractModule {
//
//
//
//        @Override
//        protected void configure() {
//
//
//        }
//    }
//
//    public static class C1 {
//
//        public C3 c3() {
//            return injector.getInstance(C3.class);
//        }
//
//
//        @Provides
//        @Singleton
//        public C2 providerC2() {
//            System.out.println("provider C2 1");
//            C2 c2 = new C2();
//            System.out.println("provider C2 2");
//
//            return c2;
//        }
//
//
//    }
//
//    public static class C2 {
//
//        public C2() {
//            System.out.println("XXXXXXXXXXXXXX");
//        }
//
//        final long time = System.currentTimeMillis();
//
//        public long getTime() {
//            return time;
//        }
//
//    }
//
//
//    public static class C3 {
//
//        private final C2 c2;
//
//        @Inject
//        public C3(C2 c2) {
//            System.out.println("CCCCCC3");
//            this.c2 = c2;
//        }
//
//        public C2 c2() {
//            return c2;
//        }
//
//    }
//
//}
