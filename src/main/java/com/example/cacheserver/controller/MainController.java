package com.example.cacheserver.controller;

import com.example.cacheserver.entity.CallableAction;
import com.example.cacheserver.service.ThreadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("main")
@Slf4j
public class MainController {

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    ThreadService threadService;

    @GetMapping("/health")
    public ResponseEntity<String> healthController() {
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    @PostMapping("/insert")
    public ResponseEntity<Map<String, Integer>> insertController(@RequestBody Long value) throws ExecutionException, InterruptedException {
        log.info("Calling /insert with {}", value);

        threadService
                .setValue(value)
                .setCallableAction(CallableAction.INSERT);
        Future resFuture = taskExecutor.submit(threadService);

        return new ResponseEntity<>((Map<String, Integer>) resFuture.get(), HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteController(@PathVariable Long id) throws ExecutionException, InterruptedException {
        log.info("Calling /delete with {}", id);

        threadService
                .setValue(id)
                .setCallableAction(CallableAction.DELETE);
        Future resFuture = taskExecutor.submit(threadService);
        resFuture.get();

        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @GetMapping("/return")
    public ResponseEntity<List<Integer>> returnController(@RequestBody List<Integer> indexes) throws ExecutionException, InterruptedException {
        log.info("Calling /return with {}", indexes);

        threadService
                .setIndexes(indexes)
                .setCallableAction(CallableAction.RETURN);
        Future resFuture = taskExecutor.submit(threadService);

        return new ResponseEntity<>((List<Integer>) resFuture.get(), HttpStatus.OK);
    }

}

