package com.example.cacheserver.service;

import com.example.cacheserver.entity.IntervalEnabledList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ExecutorService {

    @Autowired
    IntervalEnabledList intervalEnabledList;


    Map<String, Integer> insertAction(Long value) {
        log.info("InsertAction: value={}", value);
        return new HashMap<String, Integer>() {{
            put("outputDB", 3);
            put("outputDiv", 33);
        }};
    }

    List<Integer> returnAction(List<Integer> indexes) {
        log.info("ReturnAction: indexes={}", indexes);

        return indexes;
    }

    void deleteAction(Long index) {
        log.info("DeleteAction: index={}", index);
    }

}
