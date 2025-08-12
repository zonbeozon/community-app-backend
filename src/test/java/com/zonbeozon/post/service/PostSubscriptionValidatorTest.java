package com.zonbeozon.post.service;

import com.zonbeozon.post.PostSubscriptionValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PostSubscriptionValidatorTest {
    @Autowired
    private PostSubscriptionValidator validator;

    @Test
    @DisplayName("지원하는 url이라면 true를 리턴한다.")
    void ReturnTrueWhenUrlIsSupported() {
        boolean isSupport = validator.isSupport("/topic/channel/1/post");
        Assertions.assertThat(isSupport).isTrue();
    }
}
