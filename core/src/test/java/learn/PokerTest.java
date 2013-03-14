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

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class PokerTest {
    Random rdm = new Random(System.currentTimeMillis());

    @Test
    public void test() {
        int count = 0;
        for(int i = 1; i < 100 * 100 * 100 * 100; i++) {
            count += occasion() ? 1 : 0;
            if (i % 1000000 == 0)
                System.out.println("i:" + i + ", count: " + count + ", " + (count * 1.0)/i);

        }

        System.out.println("i:" + 100 * 100 * 100 + ", count: " + count + ", " + (count * 1.0)/(100 * 100 * 100));
    }

    public boolean occasion() {
        List<Integer> origin = new ArrayList<Integer>(54);
        for(int i = 1; i <= 54; i++)
            origin.add(i);

        int[] data = new int[54];

        for(int i = 53; i >= 1; i--) {
            int index = rdm.nextInt(i);
            data[i] = origin.remove(index);
            Assert.assertTrue(data[i] > 0);
        }

        data[0] = origin.remove(0);
        Assert.assertTrue(data[0] > 0);
        Assert.assertTrue(origin.size() == 0);

        int first = -1;
        for(int i = 0; i < 51; i++) {
            if (first != -1 && first/17 != i/17)
                return false;

            if (data[i] < 3) {
                if (first != -1)
                    return true;
                first = i;
             }
        }

        return false;

    }
}
