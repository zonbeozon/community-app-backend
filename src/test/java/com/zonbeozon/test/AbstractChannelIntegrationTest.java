package com.zonbeozon.test;

import org.springframework.beans.factory.annotation.Autowired;

public class AbstractChannelIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    protected TestPostService testPostService;
    @Autowired
    protected TestChannelService testChannelService;
    @Autowired
    protected TestPostReactionService testPostReactionService;
    @Autowired
    protected TestCommentService testCommentService;
    @Autowired
    protected TestPostMetricService testPostMetricService;
}
