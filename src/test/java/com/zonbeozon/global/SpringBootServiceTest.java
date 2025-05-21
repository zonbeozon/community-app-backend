package com.zonbeozon.global;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Profile("test")
@SpringBootTest
@Transactional
public class SpringBootServiceTest {
}
