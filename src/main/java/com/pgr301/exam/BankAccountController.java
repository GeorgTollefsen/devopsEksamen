package com.pgr301.exam;

import com.pgr301.exam.model.Account;
import com.pgr301.exam.model.Transaction;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.math.BigDecimal.*;
import static java.util.Optional.ofNullable;

@RestController
public class BankAccountController implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private BankingCoreSystmeService bankService;

    private static long counter;

    @PostMapping(path = "/account/{fromAccount}/transfer/{toAccount}", consumes = "application/json", produces = "application/json")
    public void transfer(@RequestBody Transaction tx, @PathVariable String fromAccount, @PathVariable String toAccount) {
        long startTimer = System.currentTimeMillis();
        //vi kapsulerer alle metodene i try catch for å kunne telle alle exceptions

        try{
            bankService.transfer(tx, fromAccount, toAccount);
            Metrics.timer("Timer.Post.transfer", "milliseconds",
                    String.valueOf(System.currentTimeMillis()-startTimer))
                    .record(System.currentTimeMillis()-startTimer, TimeUnit.MILLISECONDS);
        }catch (BackEndException b){
            Metrics.timer("Timer.Post.transfer", "milliseconds",
                    String.valueOf(System.currentTimeMillis()-startTimer))
                    .record(System.currentTimeMillis()-startTimer, TimeUnit.MILLISECONDS);
            Metrics.counter("Counter.Malfunction.BackEndException", "TotalCrashes",
                    String.valueOf(counter++)).increment();
            throw new BackEndException(); //i tilfelle du virkelig ville ha backendException for en grunn
        }
    }

    @PostMapping(path = "/account", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> updateAccount(@RequestBody Account a) {
        long startTimer = System.currentTimeMillis();
        //vi kapsulerer alle metodene i try catch for å kunne telle alle exceptions

        try{
            bankService.updateAccount(a);
            Metrics.timer("Timer.Post.account", "milliseconds",
                    String.valueOf(System.currentTimeMillis()-startTimer))
                    .record(System.currentTimeMillis()-startTimer, TimeUnit.MILLISECONDS);
            return new ResponseEntity<>(a, HttpStatus.OK);
        }catch (BackEndException b){
            Metrics.timer("Timer.Post.account", "milliseconds",
                    String.valueOf(System.currentTimeMillis()-startTimer))
                    .record(System.currentTimeMillis()-startTimer, TimeUnit.MILLISECONDS);
            Metrics.counter("Counter.Malfunction.BackEndException", "TotalCrashes",
                    String.valueOf(counter++)).increment();
            throw new BackEndException(); //i tilfelle du virkelig ville ha backendException for en grunn
        }
    }

    @GetMapping(path = "/account/{accountId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> balance(@PathVariable String accountId) {
        long startTimer = System.currentTimeMillis();
        //vi kapsulerer alle metodene i try catch for å kunne telle alle exceptions
        try {
            Account account = ofNullable(bankService.getAccount(accountId)).orElseThrow(AccountNotFoundException::new);
            Metrics.timer("Timer.Get.account", "milliseconds",
                    String.valueOf(System.currentTimeMillis()-startTimer))
                    .record(System.currentTimeMillis()-startTimer, TimeUnit.MILLISECONDS);
            return new ResponseEntity<>(account, HttpStatus.OK);
        }catch (BackEndException b){
            Metrics.counter("Counter.Malfunction.BackEndException", "TotalCrashes",
                    String.valueOf(counter++)).increment();
            Metrics.timer("Timer.Get.account", "milliseconds",
                    String.valueOf(System.currentTimeMillis()-startTimer))
                    .record(System.currentTimeMillis()-startTimer, TimeUnit.MILLISECONDS);
            throw new BackEndException(); //i tilfelle du virkelig ville ha backendException for en grunn
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "video not found")
    public static class AccountNotFoundException extends RuntimeException {
    }
}

