package com.zonbeozon.base;

import com.zonbeozon.global.s3.outbox.ImageOutboxProcessor;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
@Transactional
@Execution(ExecutionMode.CONCURRENT)
@RecordApplicationEvents
@ActiveProfiles({"test", "cache", "mocks3"})
@TestPropertySource(properties = {
        "DOCKER_S3MOCK_URL=http://s3mock:9090",
        "EXTERNAL_S3MOCK_URL=http://localhost:9090"
})
public abstract class AbstractIntegrationTest {
    @Autowired
    protected HibernateQueryInterceptor queryInterceptor;
    @Autowired
    protected TestMemberService testMemberService;
    @Autowired
    protected ApplicationEvents applicationEvents;

    //s3 실제 통신 비활성화
    @MockitoBean
    private S3Client s3Client;
    @MockitoBean
    private ImageOutboxProcessor imageOutboxProcessor;

    protected <T, E extends Exception> QueryCountAssert<T, E> assertThatDb(ThrowingProducer<T, E> call) {
        return QueryCountAssert.assertThatDb(queryInterceptor, call);
    }
}
