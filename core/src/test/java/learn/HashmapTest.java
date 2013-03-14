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

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class HashmapTest {

    @Test
    public void testTime() {
        long time = System.currentTimeMillis();

        System.out.println(time);


    }

	@Test
	public void testadd() {
		Map<Integer, String> m = new HashMap<Integer, String>();
		m.put(100, "a");
		m.put(200, "b");
		
		
		for(Map.Entry<Integer, String> entry : m.entrySet()) {
			System.out.println(entry.getValue());
		}
	}
	
//	public void testMethod() {
//		CA ca = null;
//	}
//	
//	public static class CA {
//		public String a() {
//			return null;
//		}
//	}
//	
}
