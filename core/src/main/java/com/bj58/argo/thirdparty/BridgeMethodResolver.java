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
 package com.bj58.argo.thirdparty;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


 /**
  * 泛型方法的工具类，可以用来处理bridged method。
  *
  *
  * @author Service Platform Architecture Team (spat@58.com)
  */
 public abstract class BridgeMethodResolver {

     /**
      * 通过参数Method找到我们调用的原始Method
      * java 通过bridge方式来实现泛型。
      *
      *
      * @param bridgeMethod
      * @return the original method
      */
     public static Method findBridgedMethod(Method bridgeMethod) {
         if (bridgeMethod == null || !bridgeMethod.isBridge()) {
             return bridgeMethod;
         }
         // Gather all methods with matching name and parameter size.
         List<Method> candidateMethods = new ArrayList<Method>();
         Method[] methods = ReflectionUtils.getAllDeclaredMethods(bridgeMethod.getDeclaringClass());
         for (Method candidateMethod : methods) {
             if (isBridgedCandidateFor(candidateMethod, bridgeMethod)) {
                 candidateMethods.add(candidateMethod);
             }
         }
         // Now perform simple quick check.
         if (candidateMethods.size() == 1) {
             return candidateMethods.get(0);
         }
         // Search for candidate match.
         Method bridgedMethod = searchCandidates(candidateMethods, bridgeMethod);
         if (bridgedMethod != null) {
             // Bridged method found...
             return bridgedMethod;
         }
         else {
             // A bridge method was passed in but we couldn't find the bridged method.
             // Let's proceed with the passed-in method and hope for the best...
             return bridgeMethod;
         }
     }


     private static Method searchCandidates(List<Method> candidateMethods, Method bridgeMethod) {
         if (candidateMethods.isEmpty()) {
             return null;
         }
         Map<TypeVariable, Type> typeParameterMap = GenericTypeResolver.getTypeVariableMap(bridgeMethod.getDeclaringClass());
         Method previousMethod = null;
         boolean sameSig = true;
         for (Method candidateMethod : candidateMethods) {
             if (isBridgeMethodFor(bridgeMethod, candidateMethod, typeParameterMap)) {
                 return candidateMethod;
             }
             else if (previousMethod != null) {
                 sameSig = sameSig &&
                         Arrays.equals(candidateMethod.getGenericParameterTypes(), previousMethod.getGenericParameterTypes());
             }
             previousMethod = candidateMethod;
         }
         return (sameSig ? candidateMethods.get(0) : null);
     }


     private static boolean isBridgedCandidateFor(Method candidateMethod, Method bridgeMethod) {
         return (!candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod) &&
                 candidateMethod.getName().equals(bridgeMethod.getName()) &&
                 candidateMethod.getParameterTypes().length == bridgeMethod.getParameterTypes().length);
     }


     static boolean isBridgeMethodFor(Method bridgeMethod, Method candidateMethod, Map<TypeVariable, Type> typeVariableMap) {
         if (isResolvedTypeMatch(candidateMethod, bridgeMethod, typeVariableMap)) {
             return true;
         }
         Method method = findGenericDeclaration(bridgeMethod);
         return (method != null && isResolvedTypeMatch(method, candidateMethod, typeVariableMap));
     }


     private static Method findGenericDeclaration(Method bridgeMethod) {
         // Search parent types for method that has same signature as bridge.
         Class superclass = bridgeMethod.getDeclaringClass().getSuperclass();
         while (!Object.class.equals(superclass)) {
             Method method = searchForMatch(superclass, bridgeMethod);
             if (method != null && !method.isBridge()) {
                 return method;
             }
             superclass = superclass.getSuperclass();
         }

         // Search interfaces.
         Class[] interfaces = ClassUtil.getAllInterfacesForClass(bridgeMethod.getDeclaringClass());
         for (Class ifc : interfaces) {
             Method method = searchForMatch(ifc, bridgeMethod);
             if (method != null && !method.isBridge()) {
                 return method;
             }
         }

         return null;
     }


     private static boolean isResolvedTypeMatch(
             Method genericMethod, Method candidateMethod, Map<TypeVariable, Type> typeVariableMap) {

         Type[] genericParameters = genericMethod.getGenericParameterTypes();
         Class[] candidateParameters = candidateMethod.getParameterTypes();
         if (genericParameters.length != candidateParameters.length) {
             return false;
         }
         for (int i = 0; i < genericParameters.length; i++) {
             Type genericParameter = genericParameters[i];
             Class candidateParameter = candidateParameters[i];
             if (candidateParameter.isArray()) {
                 // An array type: compare the component type.
                 Type rawType = GenericTypeResolver.getRawType(genericParameter, typeVariableMap);
                 if (rawType instanceof GenericArrayType) {
                     if (!candidateParameter.getComponentType().equals(
                             GenericTypeResolver.resolveType(((GenericArrayType) rawType).getGenericComponentType(), typeVariableMap))) {
                         return false;
                     }
                     break;
                 }
             }
             // A non-array type: compare the type itself.
             Class resolvedParameter = GenericTypeResolver.resolveType(genericParameter, typeVariableMap);
             if (!candidateParameter.equals(resolvedParameter)) {
                 return false;
             }
         }
         return true;
     }


     private static Method searchForMatch(Class type, Method bridgeMethod) {
         return ReflectionUtils.findMethod(type, bridgeMethod.getName(), bridgeMethod.getParameterTypes());
     }

 }

