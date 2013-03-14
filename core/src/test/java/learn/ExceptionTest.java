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
package learn;

import org.testng.annotations.Test;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ExceptionTest {

    @Test
    public void test() {
        Exception e1 = new Exception("e1");
//        System.out.println("e1==================================");
//        e1.printStackTrace();

//        System.out.println("e2==================================");

        Exception e2 = new Exception("e2",e1);
//        e2.printStackTrace();

//        System.out.println("e3==================================");

        Exception e3 = new MyException(e1);
        e3.printStackTrace();
    }

    private static class MyException extends Exception {
        public MyException(Throwable e) {
//            super("My exception's message", e, false, false);
        }
    }

}
