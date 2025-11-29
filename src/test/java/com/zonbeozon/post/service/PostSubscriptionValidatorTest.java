package com.zonbeozon.post.service;

import com.zonbeozon.test.AbstractIntegrationTest;
import com.zonbeozon.post.PostSubscriptionValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostSubscriptionValidatorTest extends AbstractIntegrationTest {
    @Autowired
    private PostSubscriptionValidator validator;

    @Test
    @DisplayName("지원하는 url이라면 true를 리턴한다.")
    void ReturnTrueWhenUrlIsSupported() {
        boolean isSupport = validator.isSupport("/topic/channels/1/posts");
        Assertions.assertThat(isSupport).isTrue();
    }
}
