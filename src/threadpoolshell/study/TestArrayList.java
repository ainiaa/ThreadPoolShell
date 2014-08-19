/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadpoolshell.study;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class TestArrayList {

    public static void main(String[] args) {
        ArrayList<Integer> al = new ArrayList<Integer>();
        al.add(0, 0);
        al.add(1, 1);
        al.add(2, 2);
        al.add(3, 3);
        al.add(4, 4);
        al.add(5, 5);

        al.remove(2);//
        al.remove(3);

        Integer int1[] = al.toArray(new Integer[al.size()]);
        for (int i = 0; i < int1.length; i++) {
            System.out.println(i + "=>" + int1[i]);
        }
    }
}
