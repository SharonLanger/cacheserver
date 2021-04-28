package com.example.cacheserver;

import com.example.cacheserver.entity.CallableAction;
import com.example.cacheserver.entity.NumberObj;
import com.example.cacheserver.repository.NumberRepository;
import com.example.cacheserver.service.ThreadService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
@SpringBootTest
class CacheserverApplicationTests {

	@Autowired
	NumberRepository numberRepository;

	@Autowired
	ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	ThreadService threadService;

	@Test
	void testRepositorySanity() {
		NumberObj num = new NumberObj()
				.setId(1)
				.setValue(1);

		System.out.println(num);
		numberRepository.saveAndFlush(num);

		NumberObj n1 = numberRepository.findById(1L).orElse(null);
		System.out.println(n1);
	}

	@Test
	void testThreadService() {

		LongStream.range(1,5).forEach(i -> {
			try {
				log.info("testThreadService: input {}", i);

				threadService
						.setValue(i)
						.setCallableAction(CallableAction.DELETE);
				Future resFuture = taskExecutor.submit(threadService);
				log.info("testThreadService: output {}", resFuture.get());

				threadService
						.setValue(i)
						.setCallableAction(CallableAction.INSERT);
				resFuture = taskExecutor.submit(threadService);
				log.info("testThreadService: output {}", resFuture.get());

				threadService
						.setValue(i)
						.setCallableAction(CallableAction.RETURN);
				resFuture = taskExecutor.submit(threadService);
				log.info("testThreadService: output {}", resFuture.get());

			} catch (InterruptedException e) {
				e.printStackTrace();
				Assert.isTrue(false);
			} catch (ExecutionException e) {
				e.printStackTrace();
				Assert.isTrue(false);
			}
		});
	}
}
