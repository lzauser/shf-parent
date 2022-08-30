package com.atguigu.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Demo01 {

    public static void main(String[] args) {
        String password = new BCryptPasswordEncoder().encode("xiaoli");
        System.out.println("password = " + password);
    }
    
}
