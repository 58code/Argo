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
//package com.bj58.argo.logs;
//
//import com.google.inject.*;
//import com.google.inject.matcher.Matchers;
//import org.testng.annotations.Test;
//
//import javax.inject.Inject;
//
///**
// * @author Service Platform Architecture Team (spat@58.com)
// */
//public class LoggerTest {
//
//    private Module module = new AbstractModule() {
//        @Override
//        protected void configure() {
//            this.bindListener(Matchers.any(), new LoggerTypeListener());
//        }
//    };
//
//    @Test
//    public void test() {
//        Injector injector = Guice.createInjector(module);
//
//        CI c = injector.getInstance(CI.class);
//
//        c.test();
//
//
//    }
//
//    @ImplementedBy(C.class)
//    public static interface CI {
//        void test();
//    }
//
//    public static class C implements CI{
//        private Logger logger;
//
//
////        private final Logger logger2;
////
////        @Inject
////        public C (Logger logger2) {
////            this.logger2 = logger2;
////        }
//
//        public void test() {
//            System.out.println("log1");
//            System.out.println(logger.toString());
//            System.out.println("log2");
////            System.out.println(logger2.toString());
//        }
//    }
//
////    private static class MyModule extends AbstractModule {
////
////        @Override
////        protected void configure() {
////
////        }
////
////        public
////    }
//}
