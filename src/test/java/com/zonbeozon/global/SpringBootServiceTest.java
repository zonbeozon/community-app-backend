package com.zonbeozon.global;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Profile("test")
@SpringBootTest
@Transactional
public class SpringBootServiceTest {
}
