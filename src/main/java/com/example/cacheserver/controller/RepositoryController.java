package com.example.cacheserver.controller;

import com.example.cacheserver.entity.Numbers;
import com.example.cacheserver.repository.NumberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("db")
@Slf4j
public class RepositoryController {

    @Autowired
    NumberRepository numberRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Numbers> getNumber(@PathVariable Integer id) {
        Numbers numbers = numberRepository.findById(id).orElse(null);
        return new ResponseEntity<>(numbers, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Numbers>> getNumbers() {
        List<Numbers> numbers = numberRepository.findAll();
        return new ResponseEntity<List<Numbers>>(numbers, HttpStatus.OK);
    }

    @PostMapping("/init-db")
    public ResponseEntity<String> initDb() {
        numberRepository.deleteAll();
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    @PostMapping("/insert-number/{value}")
    public ResponseEntity<Numbers> insertNumber(@PathVariable Integer value) {
        Numbers numbersRes = numberRepository.saveAndFlush(new Numbers().setValue(value));
        return new ResponseEntity<>(numbersRes, HttpStatus.OK);
    }
}
