package com.example.cacheserver.repository;

import com.example.cacheserver.CacheserverApplicationTests;
import com.example.cacheserver.entity.Numbers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class NumberRepositoryTest extends CacheserverApplicationTests {

    @Autowired
    NumberRepository numberRepository;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        numberRepository.deleteAll();
        numberRepository.flush();
    }

    @Test
    void testNumberRepositorySanity() {
        Numbers num = new Numbers()
                .setValue(1);

        numberRepository.saveAndFlush(num);

        Numbers returnedNumber = numberRepository.findById(1).orElse(null);
        Assertions.assertEquals(num.getValue(), returnedNumber.getValue());
        Assertions.assertEquals(num.getId(), returnedNumber.getId());
    }

}