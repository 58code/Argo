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
package com.bj58.argo;

import com.bj58.argo.client.ClientContext;
import com.bj58.argo.lifecycle.Lifecycle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 管理一个客户端请求的生命周期
 *
 */
public interface BeatContext {

    /**
     * MVC 中的Model, 以key,value形式存放，可以由Controller传个View
     *
     * @return 当前model
     */
    Model getModel();

    /**
     * 返回本次调用的 {@link HttpServletRequest}对象
     *
     * @return 当前请求
     */
    HttpServletRequest getRequest();

    /**
     * 返回本次调用的 {@link HttpServletResponse}对象
     *
     * @return 当前response
     */
    HttpServletResponse getResponse();

    /**
     * 得到ServletContext信息
     *
     * @return 当前ServletContext
     */
    ServletContext getServletContext();

    /**
     * 获得客户端的信息
     *
     * @return 客户端信息
     */
    ClientContext getClient();

//    /**
//     * 本次调用的生命周期
//     * @return
//     */
//    Lifecycle getLifecycle();

}