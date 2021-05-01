package com.example.cacheserver.service;

import com.example.cacheserver.entity.CallableAction;
import com.example.cacheserver.utils.CachedException;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Callable;


@Slf4j
@Data
@Accessors(chain = true)
public class TaskCallable implements Callable<Object> {

    @Autowired
    ExecutorService executorService;

    CallableAction callableAction;
    Integer newValue;
    Integer deleteIndex;
    List<Integer> returnIndexes;

    @Override
    public Object call() throws CachedException {
        log.debug("Calling TaskCallable: {}", callableAction);
        return executeAction();
    }

    private Object executeAction() throws CachedException {
        switch (callableAction) {
            case DELETE:
                executorService.deleteAction(deleteIndex);
                return null;
            case INSERT:
                return executorService.insertAction(newValue);
            case RETURN:
                return executorService.returnAction(returnIndexes);
        }
        return null;
    }

}
