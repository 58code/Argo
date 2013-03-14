package learn;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import org.junit.Test;




public class LearnJssist {
	
	
	/**
	 * 得到方法参数名称数组
	 * 由于java没有提供获得参数名称的api，利用了javassist来实现
	 * @return
	 */
	public String[] getMethodParamNames(Class<?> clazz, String methodName) {
		Method method =null;
		try {
			ClassPool pool = ClassPool.getDefault();
			
			
			pool.insertClassPath(new ClassClassPath(clazz));

			CtClass cc = pool.get(clazz.getName());
			
			//DEBUG, 函数名相同的方法重载的信息读不到 2011-03-21
			CtMethod cm = cc.getDeclaredMethod(method.getName());
			
			//2011-03-21
//			String[] paramTypeNames = new String[method.getParameterTypes().length]; 
//			for (int i = 0; i < paramTypes.length; i++)  
//	            paramTypeNames[i] = paramTypes[i].getName();  
//			CtMethod cm = cc.getDeclaredMethod(method.getName(), pool.get(new String[] {}));
			
			// 使用javaassist的反射方法获取方法的参数名
			MethodInfo methodInfo = cm.getMethodInfo();
			
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			if (attr == null) {
				throw new RuntimeException("class:"+clazz.getName()
						+", have no LocalVariableTable, please use javac -g:{vars} to compile the source file");
			}
			
//			for(int i  = 0 ; i< attr.length() ; i++){
//				System.out.println(i);
//				try {
//					System.out.println("===="+attr.nameIndex(i));
//					System.out.println("===="+attr.index(i));
////					System.out.println("===="+attr.nameIndex(i));
//					System.out.println(clazz.getName()+"================"+i+attr.variableName(i));
//					
//					
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			//addContextVariable by lzw 用于兼容jdk 编译时 LocalVariableTable顺序问题
			int startIndex = getStartIndex(attr);
			String[] paramNames = new String[cm.getParameterTypes().length];
			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			
			for (int i = 0; i < paramNames.length; i++)
				paramNames[i] = attr.variableName(startIndex + i + pos);
			// paramNames即参数名
			for (int i = 0; i < paramNames.length; i++) {
				System.out.println(paramNames[i]);
			}
			
			return paramNames;

		} catch (NotFoundException e) {
			e.printStackTrace();
			return new String[0];
		}
	}
	
	private int getStartIndex(LocalVariableAttribute attr){
		
//		attr.st
		
		int startIndex = 0;
		for(int i  = 0 ; i< attr.length() ; i++){
			if("this".equals(attr.variableName(i))){
				startIndex = i;
				break;
			}
		}
		return startIndex;
	}
}
