package com.visenergy.modbustcp;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author WuSong
 * @Date 2017-08-23
 * @Time 18:35:51
 */
public class test {
    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("6",2);
        map.put("2",2);
        map.put("3",3);
        map.put("4",4);
        map.put("9",5);
        map.put("1",6);
        Object[] objects = new Object[6];
        objects = map.keySet().toArray();
        for (int i = 0; i < objects.length; i++) {
            System.out.println(objects[i]);
        }

    }
}
