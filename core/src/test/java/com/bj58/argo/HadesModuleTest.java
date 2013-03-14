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
//package com.bj58.argo;
//
//import BeatContext;
//import GroupConvention;
//import AssemblyInjector;
//import ArgoModule;
//import com.google.inject.Injector;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.File;
//
//import static org.mockito.Mockito.*;
//
///**
// * @author Service Platform Architecture Team (spat@58.com)
// */
//public class HadesModuleTest {
//
//    @Test
//    public void testModule() {
//
//        Argo argo = mock(Argo.class);
//        GroupConvention groupConvention = mock(GroupConvention.class);
//        BeatContext beatContext = mock(BeatContext.class);
//
//        HadesModule module = new HadesModule(argo, groupConvention);
//
//        Injector injector = module.getInjector();
//
//        when(argo.beatContext()).thenReturn(beatContext);
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(argo.request()).thenReturn(request);
//
//        Assert.assertEquals(module.provideBeatContext(), beatContext);
//        Assert.assertEquals(module.provideHttpServletRequest(), request);
//
//
//        BeatContext actualBeat = injector.getInstance(BeatContext.class);
//        Assert.assertEquals(actualBeat, beatContext);
//
//    }
//
//
//
//
//    GroupConvention groupConvention = new GroupConvention() {
//        @Override
//        public File groupConfigFolder() {
//            return new File("/opt/argo");
//        }
//
//        @Override
//        public File projectLogFolder() {
//            return new File("/opt/argo/logs");
//        }
//
//        @Override
//        public File projectConfigFolder() {
//            return new File("/opt/argo/me");
//        }
//
//        @Override
//        public String currentProjectId() {
//            return "myprojectId";
//        }
//
//        @Override
//        public void assemblyInject(AssemblyInjector injector) {
//
//        }
//
//        @Override
//        public File viewsFolder() {
//            return new File("/opt/web/my/WEB-INF/classes/create");
//        }
//
//        @Override
//        public File staticResourcesFolder() {
//            return new File("/opt/web/my/WEB-INF/classes/static");
//        }
//    };
//}
