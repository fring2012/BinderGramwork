package com.example.fring.bindergramwork;

import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        HashMap<Integer,String> hashMap = new HashMap<>();
        hashMap.put(1,"zhangsan");
        System.out.println(hashMap.put(1,"lisi"));
    }
}
