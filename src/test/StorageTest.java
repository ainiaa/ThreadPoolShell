/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class StorageTest {

    private HashMap storageModel;

    public static void main(String args[]) {

        StorageTest st = new StorageTest();

//        HashMap s = st.initDbIndexHashSet();
//        System.out.println("Hello World!");
        st.isListReSortAfterRemove();

        /*
        
         */
    }

    /**
     * list 是否在remove之后重新排序
     *
     * @return
     */
    private boolean isListReSortAfterRemove() {
        boolean status;
        List<Integer> list = new ArrayList();
        for (int index = 0; index < 10; index++) {
            list.add(index);
        }

        list.remove(1);
        status = list.get(1) != 1;

        System.out.println("1 => " + list.get(1));

        if (status) {
            System.out.println("删除list之后会重新设定键值对");
        } else {
            System.out.println("删除list之后会重新设定键值对");
        }

        return status;
    }

    /**
     * map 是否以引用的方式传递 (TRUE)
     *
     * @return
     */
    private boolean isMapPassedByRef() {
        StorageTest st = new StorageTest();
        HashMap map = new HashMap();
        boolean status = false;
        for (int index = 0; index < 10; index++) {
            map.put(index, index++);
        }

        st.removeHashMap(1, map);
        if (map.containsKey(1)) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }

    public void removeHashMap(int index, HashMap map) {
        map.remove(index);
    }

    private HashMap initDbIndexHashSet() {
        int tableIndex = 0;
        this.storageModel = new HashMap();
        for (int index = 0; index < 40; index++) {
            if (index % 4 == 0 && index != 0) {
                tableIndex++;
            }
            this.storageModel.put(index, new ArrayList(Arrays.asList(new Integer[]{index % 4, tableIndex})));
        }
        return this.storageModel;
        /*
         this.dbIndexHashSet = new HashSet();
         this.dbIndexHashSet.add(0);//0,0
         this.dbIndexHashSet.add(1);//1,0
         this.dbIndexHashSet.add(2);//2,0
         this.dbIndexHashSet.add(3);//3,0
         this.dbIndexHashSet.add(4);//0,1
         this.dbIndexHashSet.add(5);//1,1
         this.dbIndexHashSet.add(6);//2,1
         this.dbIndexHashSet.add(7);//3,1
         this.dbIndexHashSet.add(8);//0,2
         this.dbIndexHashSet.add(9);//1,2
         this.dbIndexHashSet.add(10);//2,2
         this.dbIndexHashSet.add(11);//3,2
         this.dbIndexHashSet.add(12);//0,3
         this.dbIndexHashSet.add(13);//1,3
         this.dbIndexHashSet.add(14);//2,3
         this.dbIndexHashSet.add(15);//3,3
         this.dbIndexHashSet.add(16);//0,4
         this.dbIndexHashSet.add(17);//1,4
         this.dbIndexHashSet.add(18);//2,4
         this.dbIndexHashSet.add(19);//3,4
         this.dbIndexHashSet.add(20);//0,5
         this.dbIndexHashSet.add(21);//1,5
         this.dbIndexHashSet.add(22);//2,5
         this.dbIndexHashSet.add(23);//3,5
         this.dbIndexHashSet.add(24);//0,6
         this.dbIndexHashSet.add(25);//1,6
         this.dbIndexHashSet.add(26);//2,6
         this.dbIndexHashSet.add(27);//3,6
         this.dbIndexHashSet.add(28);//0,7
         this.dbIndexHashSet.add(29);//1,7
         this.dbIndexHashSet.add(30);//2,7
         this.dbIndexHashSet.add(31);//3,7
         this.dbIndexHashSet.add(32);//0,8
         this.dbIndexHashSet.add(33);//1,8
         this.dbIndexHashSet.add(34);//2,8
         this.dbIndexHashSet.add(35);//3,8
         this.dbIndexHashSet.add(36);//0,9
         this.dbIndexHashSet.add(37);//1,9
         this.dbIndexHashSet.add(38);//2,9
         this.dbIndexHashSet.add(39);//3,9
         */
    }
}
