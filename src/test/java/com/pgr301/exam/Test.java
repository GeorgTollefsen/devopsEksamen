package com.pgr301.exam;

import com.pgr301.exam.model.Account;
import org.junit.jupiter.api.Assertions;

public class Test {

    @org.junit.jupiter.api.Test
    public void create(){
        Account x = new Account();
        Assertions.assertEquals("0", x.getBalance().toString());
    }
}
