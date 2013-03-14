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
package com.mycompany.sample.controllers;

import com.bj58.argo.ActionResult;
import com.bj58.argo.BeatContext;
import com.bj58.argo.annotations.Path;
import com.bj58.argo.controller.AbstractController;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
@Path("hello")
public class HomeController extends AbstractController{

    @Path("")
    public ActionResult hello() {
        return writer().write("Hello world");
    }

    @Path("argo")
    public ActionResult helloArgo() {
        return writer().write("Hello, argo");
    }

    @Path("{name}")
    public ActionResult helloWorld(String name) {
        return writer().write("Hello, %s", name);
    }

    /**
     * 这个是一个比较复杂的例子，
     * Path中的路径可以用正则表达式匹配，
     * @Path("{phoneNumber:\\d+}")和@Path("{name}")的匹配顺序是
     * 如果都匹配，先匹配模板路径长的也就是@Path("{phoneNumber:\\d+}")
     *
     * @param phoneNumber
     * @return
     */
    @Path("{phoneNumber:\\d+}")
    public ActionResult helloView(int phoneNumber) {
        BeatContext beatContext = beat();

        beatContext
                .getModel()
                .add("title", "phone")
                .add("phoneNumber", phoneNumber);

        return view("hello");

    }
}
