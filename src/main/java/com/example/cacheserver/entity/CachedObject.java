package com.example.cacheserver.entity;

import com.example.cacheserver.utils.CachedException;
import com.example.cacheserver.utils.CachedExceptionsType;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@Data
@Accessors(chain = true)
public class CachedObject {

    Map<Integer, Integer> numbers = new HashMap<>();

    Integer sum = 0;

    public void insertNumber(Integer value) {
        numbers.put( numbers.size(), value);
        sum += value;
    }

    public Integer deleteByIndex(Integer index) throws CachedException {
        if ( numbers.get(index) == -1 )
            throw new CachedException("Can't delete again index: " + index, CachedExceptionsType.INDEX_WAS_ALREADY_DELETED);
        int number = numbers.get(index);
        sum -= number;
        numbers.put(index, -1);
        return number;
    }

    public void revertDeleteNumber(Integer index, Integer value) {
        numbers.put( index, value);
        sum += value;
    }

}
