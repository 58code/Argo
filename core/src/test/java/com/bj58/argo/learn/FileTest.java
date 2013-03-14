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
package com.bj58.argo.learn;

import org.testng.annotations.Test;

import java.io.File;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class FileTest {

    @Test
    public void testContain() {
        File src = new File("D:\\新建文件夹\\facade\\");
        File src1 = new File("D:\\新建文件夹\\facade");
        File dest = new File("D:/新建文件夹/facade/新建文件夹/aa");
        File dest1 = new File("D:/新建文件夹/facadeXXXX/新建文件夹/aa");

        System.out.println(dest.getAbsolutePath().indexOf(src.getAbsolutePath()) == 0);
        System.out.println(dest1.getAbsolutePath().indexOf(src.getAbsolutePath()) == 0);

        System.out.println(src.getAbsolutePath());
        System.out.println(src1.getAbsolutePath());
        System.out.println(dest.getAbsolutePath());
    }
}
