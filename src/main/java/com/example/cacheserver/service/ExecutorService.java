package com.example.cacheserver.service;

import com.example.cacheserver.entity.CachedObject;
import com.example.cacheserver.entity.Numbers;
import com.example.cacheserver.repository.NumberRepository;
import com.example.cacheserver.utils.CachedException;
import com.example.cacheserver.utils.CachedExceptionsType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExecutorService {
    public static final String SUM_IN_DB = "Sum in DB";
    public static final String DIV = "Div";

    @Autowired
    CachedObject cachedObject;

    @Autowired
    NumberRepository numberRepository;

    public Map<String, Integer> insertAction(Integer value) throws CachedException {
        log.debug("InsertAction: value={}", value);

        if ( value <= 0 )
            throw new CachedException("value can't be a negative number", CachedExceptionsType.NEGATIVE_VALUE);
        Numbers number = new Numbers()
                .setValue(value);
        numberRepository.saveAndFlush(number);

        cachedObject.insertNumber(value);

        Integer newSum = cachedObject.getSum();
        return new HashMap<String, Integer>() {{
            put(SUM_IN_DB, newSum);
            put(DIV, newSum / value);
        }};
    }

    public List<Integer> returnAction(List<Integer> indexes) throws CachedException {
        log.debug("ReturnAction: indexes={}", indexes);

        for ( Integer index: indexes ) {
            if ( index < 0 || index >= cachedObject.getNumbers().size())
                throw new CachedException("Index out of bound " + index,
                        CachedExceptionsType.INDEX_OUT_OF_BOUND_RETURN);
        }

        Map<Integer, Integer> numbers = cachedObject.getNumbers();

        return indexes
                .stream()
                .map(numbers::get)
                .collect(Collectors.toList());
    }

    public void deleteAction(Integer index) throws CachedException {
        log.debug("DeleteAction: index={}", index);

        if ( index < 0 || index >= cachedObject.getNumbers().size())
            throw new CachedException("Index out of bound " + index
                    , CachedExceptionsType.INDEX_OUT_OF_BOUND_DELETE);

        // "Delete" from cache
        int deletedNumber = cachedObject.deleteByIndex(index);

        // Swap DB numbers
        try {
            replaceIndexWithLast( index + 1 );
        } catch (Exception e) {
            cachedObject.revertDeleteNumber(index, deletedNumber);
            throw e;
        }
    }

    private Numbers getOrThrow(Integer index, String errorMsg) throws CachedException {
        Numbers number = numberRepository.findById(index).orElse(null);
        if ( number == null)
            throw new CachedException(errorMsg + ": " + index, CachedExceptionsType.INDEX_OUT_OF_BOUND_DELETE);
        return number;
    }

    private void replaceIndexWithLast(Integer index) throws CachedException {
        Numbers oldNumber = getOrThrow(index, "Can't fetch number from DB");
        Numbers lastNumber = getOrThrow(cachedObject.getNumbers().size(), "Can't fetch number from DB");

        Integer tmpValue = oldNumber.getValue();
        oldNumber.setValue(lastNumber.getValue());
        lastNumber.setValue(tmpValue);

        numberRepository.save(oldNumber);
        numberRepository.save(lastNumber);

        numberRepository.flush();
    }
}
