package com.pgr301.exam;

import com.pgr301.exam.model.Account;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

public class Test {


    @org.junit.jupiter.api.Test
    public void create(){
        int x = 1;
        org.junit.jupiter.api.Assertions.assertTrue(x==2);
    }
}
