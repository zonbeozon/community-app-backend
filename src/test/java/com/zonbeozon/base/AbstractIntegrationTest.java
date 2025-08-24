package com.zonbeozon.base;

import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageS3AsyncDeleter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Execution(ExecutionMode.CONCURRENT)
@AutoConfigureTestDatabase
@RecordApplicationEvents
@ActiveProfiles({"test", "cache"})
public abstract class AbstractIntegrationTest {
    @Autowired
    protected HibernateQueryInterceptor queryInterceptor;
    @Autowired
    protected TestMemberService testMemberService;
    @Autowired
    protected ApplicationEvents applicationEvents;
    @MockitoBean
    protected ImageS3AsyncDeleter imageS3AsyncDeleter;

    @BeforeEach
    void disableImageS3AsyncService() {
        Mockito.doNothing().when(imageS3AsyncDeleter).deleteAsync(Mockito.anyList());
        Mockito.doNothing().when(imageS3AsyncDeleter).deleteAsync(Mockito.any(Image.class));
    }

    protected <T, E extends Exception> QueryCountAssert<T, E> assertThatDb(ThrowingProducer<T, E> call) {
        return QueryCountAssert.assertThatDb(queryInterceptor, call);
    }


}
