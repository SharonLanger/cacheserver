package com.example.cacheserver.controller;

import com.example.cacheserver.entity.NumberObj;
import com.example.cacheserver.repository.NumberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("db")
@Slf4j
public class RepositoryController {

    @Autowired
    NumberRepository numberRepository;

    @GetMapping("/{id}")
    public ResponseEntity<NumberObj> getNumberObj(@PathVariable Long id) {
        NumberObj numberObj = numberRepository.findById(id).orElse(null);
        return new ResponseEntity<>(numberObj, HttpStatus.OK);
    }

    @PostMapping("/init-db")
    public ResponseEntity<String> initDb() {
        numberRepository.deleteAll();
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    @PostMapping("/insert-number/{value}")
    public ResponseEntity<NumberObj> insertNumber(@PathVariable Long value) {
        NumberObj numberObjRes = numberRepository.saveAndFlush(new NumberObj().setValue(value));
        return new ResponseEntity<>(numberObjRes, HttpStatus.OK);
    }
}
