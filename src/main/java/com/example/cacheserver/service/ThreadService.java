package com.example.cacheserver.service;

import com.example.cacheserver.entity.CallableAction;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;

@Component
@Slf4j
@Data
@Accessors(chain = true)
public class ThreadService implements Callable<Object> {

    @Autowired
    ExecutorService executorService;

    CallableAction callableAction;
    Long value;
    List<Integer> indexes;

    @Override
    public Object call() throws Exception {
        log.info("Calling ExecutorService: {}", callableAction);
        return executeAction();
    }

    private Object executeAction() {
        switch (callableAction) {
            case DELETE:
                executorService.deleteAction(value);
                return null;
            case INSERT:
                return executorService.insertAction(value);
            case RETURN:
                return executorService.returnAction(indexes);
        }
        return null;
    }

}
