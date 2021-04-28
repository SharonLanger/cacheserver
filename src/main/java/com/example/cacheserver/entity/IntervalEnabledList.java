package com.example.cacheserver.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Accessors(chain = true)
@Slf4j
@Component
public class IntervalEnabledList {
    List<NumberObj> numbers;

}
