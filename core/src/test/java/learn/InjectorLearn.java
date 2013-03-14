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

import com.google.inject.*;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.lang.reflect.Constructor;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class InjectorLearn {


    public static class MyTypeListener implements TypeListener {

        @Override
        public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {


            Class<?> clazz = type.getRawType();

            if (My.class != clazz)
                return;

            Constructor<?>[] cs = clazz.getConstructors();

            Constructor c = null;
            for(Constructor<?> cn : cs) {
                if (cn.getAnnotation(Inject.class) != null) {
                    c = cn;
                    break;
                }
            }

            if (c == null)
                return;


            Class<?>[] fs = c.getParameterTypes();
            encounter.register(new MembersInjector<I>() {
                @Override
                public void injectMembers(I instance) {
                    long time = System.currentTimeMillis();
                    System.out.println("a" + time);
                }
            });
        }
    }


    public static class My {
        private String a1;

        @Inject
        public My(String a) {
            this.a1 = a;
        }

        public void test() {
            System.out.println(a1);
        }
    }

    @Test
    public void testMy1() {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {

                binder.bind(String.class).toInstance("xyz");

            }
        });

        My1 my1 = injector.getInstance(My1.class);
        my1.test();

        My my = injector.getInstance(My.class);
        my.test();
    }

    public static class My1 {
        private String a;


        public My1() {
            long time = System.currentTimeMillis();
            a = String.valueOf(time);
        }

        public void test() {
            System.out.println(a);

            Thread.currentThread().getStackTrace();
        }
    }

}
