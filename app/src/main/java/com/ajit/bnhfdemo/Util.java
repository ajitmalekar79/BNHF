package com.ajit.bnhfdemo;

import java.util.Random;
import java.util.UUID;

public class Util {


    public Util() {


    }

    String getUUID(){

        Random r =new Random();
        String id = String.valueOf(r.nextInt(10000));

        return id;

    }

}
