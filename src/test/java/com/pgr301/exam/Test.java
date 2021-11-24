package com.pgr301.exam;

import com.pgr301.exam.model.Account;
import org.junit.jupiter.api.Assertions;

public class Test {

    @org.junit.jupiter.api.Test
    public void create(){
        //tester at man starter i 0 når man oppretter en ny konto
        Account x = new Account();
        Assertions.assertEquals("0", x.getBalance().toString());
    }
}
