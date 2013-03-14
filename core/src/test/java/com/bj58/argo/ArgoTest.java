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
//import static org.mockito.Mockito.mock;
//
//import java.io.File;
//
//import javax.servlet.ServletContext;
//
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
//import com.bj58.argo.convention.GroupConvention;
//
///**
// * @author Service Platform Architecture Team (spat@58.com)
// */
//public class ArgoTest {
//
////    @Test(expectedExceptions = ArgoException.class
////            , expectedExceptionsMessageRegExp = "Argo has been initialized.")
////    public void testInitMore() {
////        Argo.instance.init();
////    }
//	@Test
//	public void testSingleton() {
//		Argo argo1 = Argo.instance;
//		Argo argo2 = Argo.instance;
//
//		Assert.assertEquals(argo1, argo2);
//	}
//
//	@Test(expectedExceptions=ArgoException.class,expectedExceptionsMessageRegExp="Argo has been initialized.")
//	public void testInitOnlyOnce() {
//		ServletContext servletContext = mock(ServletContext.class);
//		GroupConvention groupConvention = mock(GroupConvention.class);
//		Argo.instance.init(servletContext, groupConvention);
//		Argo.instance.init(servletContext, groupConvention);
//	}
//
//	public static class MyGroupConvention implements GroupConvention {
//
//		@Override
//		public File rootFolder() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public File logPath() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public File projectConfigFolder() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String currentProjectId() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public void assemblyInject(AssemblyInjector injector) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public File viewsFolder() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public File staticResourcesFolder() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//	}
//}
