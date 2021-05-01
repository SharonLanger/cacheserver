package com.example.cacheserver.service;

import com.example.cacheserver.entity.CallableAction;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@Data
@Accessors(chain = true)
public class TaskFactory {

    @Autowired
    ExecutorService executorService;

    public TaskCallable createDeleteAction(Integer deleteIndex) {
        return new TaskCallable()
                .setExecutorService(executorService)
                .setCallableAction(CallableAction.DELETE)
                .setDeleteIndex(deleteIndex);
    }

    public TaskCallable createReturnAction(List<Integer> returnIndexes) {
        return new TaskCallable()
                .setExecutorService(executorService)
                .setCallableAction(CallableAction.RETURN)
                .setReturnIndexes(returnIndexes);
    }

    public TaskCallable createInsertAction(Integer newValue) {
        return new TaskCallable()
                .setExecutorService(executorService)
                .setCallableAction(CallableAction.INSERT)
                .setNewValue(newValue);
    }
}
