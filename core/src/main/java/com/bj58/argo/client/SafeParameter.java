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
package com.bj58.argo.client;

import com.google.inject.ImplementedBy;

import javax.inject.Singleton;

/**
 * 从queryString或者form过来的参数进行安全清洗
 * 开发者可以自己重新注入 SafeParameter 的实现，来实现适合自己的安全参数
 *
 */
@ImplementedBy(SafeParameter.DefaultSafeParameter.class)
public interface SafeParameter {

    /**
     * 将原始参数值安全编码后返回
     *
     * @param parameterValue 原始参数值
     * @return 安全参数值
     */
    String encoding(String parameterValue);

    @Singleton
    public static class DefaultSafeParameter implements SafeParameter {

        @Override
        public String encoding(String parameterValue) {
            return parameterValue;
        }
    }

}
