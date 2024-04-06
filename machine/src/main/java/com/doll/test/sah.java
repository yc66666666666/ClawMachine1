package com.doll.test;

import org.junit.Test;
import org.springframework.util.DigestUtils;

public class sah {
    @Test
    public  void  sv(){
        String password="0123456789";
        System.out.println(password.substring(password.length() - 8));

    }
    private  int a=10;
    private static  int b=20;
    static {
        System.out.println("1324");
        System.out.println(sah.b);
    }

}

