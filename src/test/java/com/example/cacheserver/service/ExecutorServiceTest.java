package com.example.cacheserver.service;

import com.example.cacheserver.CacheserverApplicationTests;
import com.example.cacheserver.entity.CachedObject;
import com.example.cacheserver.entity.Numbers;
import com.example.cacheserver.repository.NumberRepository;
import com.example.cacheserver.utils.CachedException;
import com.example.cacheserver.utils.CachedExceptionsType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.stream.IntStream;

import static com.example.cacheserver.service.ExecutorService.DIV;
import static com.example.cacheserver.service.ExecutorService.SUM_IN_DB;


class ExecutorServiceTest extends CacheserverApplicationTests {

    @Autowired
    NumberRepository numberRepository;

    @Autowired
    ExecutorService executorService;

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    TaskFactory taskFactory;

    @Autowired
    CachedObject cachedObject;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        executorService.cachedObject.setNumbers(new HashMap<>());
        executorService.cachedObject.setSum(0);
    }

    @Test
    void testInsertActionSumInDB() throws CachedException {
        Map<String, Integer> res;

        res = executorService.insertAction(1);
        Assertions.assertEquals(1, res.get(SUM_IN_DB));
        res = executorService.insertAction(2);
        Assertions.assertEquals(3, res.get(SUM_IN_DB));
        res = executorService.insertAction(3);
        Assertions.assertEquals(6, res.get(SUM_IN_DB));
    }

    @Test
    void testInsertActionSumByDiv() throws CachedException {
        Map<String, Integer> res;

        res = executorService.insertAction(1);
        Assertions.assertEquals(1, res.get(DIV));
        res = executorService.insertAction(2);
        Assertions.assertEquals(1, res.get(DIV));
        res = executorService.insertAction(3);
        Assertions.assertEquals(2, res.get(DIV));
        res = executorService.insertAction(5);
        Assertions.assertEquals(2, res.get(DIV));
    }

    @Test
    void testInsertActionSumInDBNegative_ShouldThrow_CachedExceptions() {
        assertThrowsCachedException(CachedExceptionsType.NEGATIVE_VALUE, () -> executorService.insertAction(-1));
    }

    @Test
    void testReturnAction() throws CachedException {
        IntStream.range(1, 5).forEach( i -> {
            try {
                executorService.insertAction(i * 2);
            } catch (CachedException cachedException) {
                cachedException.printStackTrace();
                Assertions.assertTrue(false);
            }
        });

        IntStream.range(0, 4).forEach( i -> {
            try {
                List<Integer> resList = executorService.returnAction(Arrays.asList(i));
                Assertions.assertEquals(1, resList.size());
                Assertions.assertEquals((i+1) * 2, resList.get(0));
            } catch (CachedException cachedException) {
                cachedException.printStackTrace();
                Assertions.assertTrue(false);
            }
        });

        List<Integer> resList = executorService.returnAction(Arrays.asList(0,1));
        Assertions.assertEquals(2, resList.size());
        resList = executorService.returnAction(Arrays.asList(2,3));
        Assertions.assertEquals(2, resList.size());
    }

    @Test
    void testReturnAction_ShouldThrow_CachedExceptions() {
        assertThrowsCachedException(CachedExceptionsType.INDEX_OUT_OF_BOUND_RETURN, () -> executorService.returnAction(Collections.singletonList(1)));

        IntStream.range(1, 5).forEach(i -> {
            try {
                executorService.insertAction(i * 2);
            } catch (CachedException cachedException) {
                cachedException.printStackTrace();
                Assertions.assertTrue(false);
            }
        });

        assertThrowsCachedException(CachedExceptionsType.INDEX_OUT_OF_BOUND_RETURN, () -> executorService.returnAction(Collections.singletonList(4)));
    }

    @Test
    void testDeleteAction_ShouldThrow_CachedExceptions() {
        assertThrowsCachedException(CachedExceptionsType.INDEX_OUT_OF_BOUND_DELETE, () -> executorService.deleteAction(1));

        IntStream.range(1, 5).forEach(i -> {
            try {
                executorService.insertAction(i * 2);
            } catch (CachedException cachedException) {
                cachedException.printStackTrace();
                Assertions.assertTrue(false);
            }
        });

        assertThrowsCachedException(CachedExceptionsType.INDEX_OUT_OF_BOUND_DELETE, () -> executorService.deleteAction(4));
    }

    @Test
    void testDeleteActionCheckDBValues() throws CachedException {
        insertNumbers();

        Numbers numberLast = numberRepository.findById(50).orElse(null);
        Assertions.assertNotNull(numberLast);
        int numberLastValue = numberLast.getValue();

        int deletedIndex = 4;
        Numbers numberDeleted = numberRepository.findById(deletedIndex).orElse(null);
        Assertions.assertNotNull(numberDeleted);
        int numberDeletedValue = numberDeleted.getValue();

        executorService.deleteAction(deletedIndex - 1);

        numberLast = numberRepository.findById(50).orElse(null);
        Assertions.assertNotNull(numberLast);

        numberDeleted = numberRepository.findById(deletedIndex).orElse(null);
        Assertions.assertNotNull(numberDeleted);

        Assertions.assertEquals(numberDeletedValue, numberLast.getValue());
        Assertions.assertEquals(numberLastValue, numberDeleted.getValue());

        Assertions.assertEquals(-1, cachedObject.getNumbers().get(deletedIndex -1));
    }

    @Test
    void testDeleteActionCheckSum() throws CachedException {
        insertNumbers();

        int sum = cachedObject.getSum();

        int deletedIndex = 4;
        int deletedValue = cachedObject.getNumbers().get(deletedIndex);
        executorService.deleteAction(deletedIndex);

        Assertions.assertEquals(sum - deletedValue, cachedObject.getSum());
    }

    @Test
    void testDeleteAction_DeleteSameIndex() throws CachedException {
        insertNumbers();

        int deletedIndex = 13;
        executorService.deleteAction(deletedIndex);

        assertThrowsCachedException(CachedExceptionsType.INDEX_WAS_ALREADY_DELETED, () -> executorService.deleteAction(deletedIndex));
    }

    private void insertNumbers() {
        IntStream.range(1, 51).forEach( i -> {
            try {
                executorService.insertAction(new Random().nextInt(500) + 1);
            } catch (CachedException cachedException) {
                cachedException.printStackTrace();
                Assertions.assertTrue(false);
            }
        });
    }

    public static void assertThrowsCachedException(CachedExceptionsType cachedExceptionsType, Executable executable) {
        CachedException exception = Assertions.assertThrows(CachedException.class, executable);
        Assertions.assertEquals(cachedExceptionsType, exception.cachedExceptionsType);
    }

}