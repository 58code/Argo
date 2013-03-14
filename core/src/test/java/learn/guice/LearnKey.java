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
package learn.guice;

import com.bj58.argo.inject.ArgoSystem;
import com.google.inject.*;
import com.google.inject.name.Names;
import org.testng.annotations.Test;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class LearnKey {

    @ImplementedBy(Action1.class)
    public static interface Action {
        String process();
    }

    public static class Action1 implements Action {

        @Override
        public String process() {
            return "action1";
        }
    }

    @ArgoSystem
    public static class Action2 implements Action {

        private long time = System.nanoTime();

        @Override
        public String process() {
            return "action2"  + time;
        }
    }

    @Test
    public void test() {

        Injector injector = Guice.createInjector(new ActionModule());

        Action action1 = injector.getInstance(Action.class);

        System.out.println(action1.process());

        Action action2 = injector.getInstance(Key.get(Action.class, Names.named("ABC")));

        System.out.println(action2.process());

        Action action3 = injector.getInstance(Key.get(Action.class, ArgoSystem.class));

        System.out.println(action2.process());

    }


    public static class ActionModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(Action.class).annotatedWith(Names.named("ABC")).to(Action2.class);
        }




    }

}
