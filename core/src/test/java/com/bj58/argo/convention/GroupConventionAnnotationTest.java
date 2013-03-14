//package com.bj58.argo.convention;
//
//import org.testng.Assert;
//
//import org.testng.annotations.Test;
//
//public class GroupConventionAnnotationTest {
//
//	//采用注解默认值
//	@GroupConventionAnnotation
//	public static class AnnotationDefaultClass {				
//	}
//	
//	@Test(description="default")
//	public void testDefaultAnnotation() {
//		GroupConventionAnnotation ga = AnnotationDefaultClass.class.getAnnotation(GroupConventionAnnotation.class);
//		Assert.assertEquals(ga.groupConfigFolder(),"/opt/argo");
//		Assert.assertEquals(ga.projectConfigFolder(),"{groupConfigFolder}/{projectId}");
//		Assert.assertEquals(ga.projectLogFolder(),"{groupConfigFolder}/logs/{projectId}");
//		Assert.assertEquals(ga.groupPackagesPrefix(),"com.bj58.argo");
//		Assert.assertEquals(ga.projectConventionClass(),"com.bj58.argo.MyProjectConvention");
//		Assert.assertEquals(ga.controllerPattern(),"com\\.bj58\\..*\\.controllers\\..*Controller");
//		Assert.assertEquals(ga.viewsFolder(),"/WEB-INF/classes/views/");
//		Assert.assertEquals(ga.staticResourcesFolder(),"/WEB-INF/classes/static/");		
//	}
//	
//	//采用注解自定义值
//	@GroupConventionAnnotation(
//			groupConfigFolder ="rootFolderCustom",
//			projectConfigFolder ="configFolderCustom",
//			projectLogFolder ="logPathCustom",
//			groupPackagesPrefix ="packagesPrefixCustom",
//			projectConventionClass="projectConventionClassCustom",
//			controllerPattern="controllerPatternCustom",
//			viewsFolder="viewsFolderCustom",
//			staticResourcesFolder="staticResourcesFolderCustom"			
//			)
//	public static class AnnotationCustomClass {
//		
//	}
//	
//	@Test(description="custom")
//	public void testCustomAnnotation() {
//		GroupConventionAnnotation ga = AnnotationCustomClass.class.getAnnotation(GroupConventionAnnotation.class);
//		Assert.assertEquals(ga.groupConfigFolder(),"rootFolderCustom");
//		Assert.assertEquals(ga.projectConfigFolder(),"configFolderCustom");
//		Assert.assertEquals(ga.projectLogFolder(),"logPathCustom");
//		Assert.assertEquals(ga.groupPackagesPrefix(),"packagesPrefixCustom");
//		Assert.assertEquals(ga.projectConventionClass(),"projectConventionClassCustom");
//		Assert.assertEquals(ga.controllerPattern(),"controllerPatternCustom");
//		Assert.assertEquals(ga.viewsFolder(),"viewsFolderCustom");
//		Assert.assertEquals(ga.staticResourcesFolder(),"staticResourcesFolderCustom");	
//	}
//	
//
//	
//
//}
