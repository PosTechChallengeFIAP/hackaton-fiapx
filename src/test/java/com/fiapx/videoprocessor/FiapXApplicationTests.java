package com.fiapx.videoprocessor;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@SpringBootTest
class FiapXApplicationTests {

	@MockitoBean
	private SqsAsyncClient sqsAsyncClient;

	@MockitoBean
	private SqsTemplate sqsTemplate;

	@Test
	void contextLoads() {
	}

}
