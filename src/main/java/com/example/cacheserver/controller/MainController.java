package com.example.cacheserver.controller;

import com.example.cacheserver.service.TaskCallable;
import com.example.cacheserver.service.TaskFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    TaskFactory taskFactory;

    @Value("${profile.property}")
    public String myProfile;

    @GetMapping("/health")
    public ResponseEntity<String> healthController() {
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<String> activeProfileController() {
        return new ResponseEntity<>(myProfile, HttpStatus.OK);
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> insertController(@RequestBody Integer value) throws ExecutionException, InterruptedException {
        log.info("Calling /insert with {}", value);

        try {
            TaskCallable taskCallable = taskFactory.createInsertAction(value);
            Future resFuture = taskExecutor.submit(taskCallable);

            return new ResponseEntity<>((Map<String, Integer>) resFuture.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + " {}", value);

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteController(@PathVariable Integer id) throws ExecutionException, InterruptedException {
        log.info("Calling /delete with {}", id);

        try {
            TaskCallable taskCallable = taskFactory.createDeleteAction(id);
            Future resFuture = taskExecutor.submit(taskCallable);
            resFuture.get();

            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + " {}", id);

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/return")
    public ResponseEntity<Object> returnController(@RequestBody List<Integer> indexes) throws ExecutionException, InterruptedException {
        log.info("Calling /return with {}", indexes);

        try {
            TaskCallable taskCallable = taskFactory.createReturnAction(indexes);
            Future resFuture = taskExecutor.submit(taskCallable);
            List<Integer> resList = (List<Integer>) resFuture.get();
            return new ResponseEntity<>(resList, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + " {}", indexes);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}

