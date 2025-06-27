package com.zonbeozon.channel.service;

import com.zonbeozon.member.BaseMemberTest;
import com.zonbeozon.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static com.zonbeozon.channel.service.ChannelFixture.*;

public abstract class BaseChannelTest extends BaseMemberTest {
    @Autowired
    private ChannelService channelService;

    protected Member channel_1_owner;
    protected Member channel_2_owner;
    protected Member channel_3_owner;

    protected Long channel_1_id;
    protected Long channel_2_id;
    protected Long channel_3_id;


    @BeforeEach
    void setUp() {
        channel_1_owner = member_1;
        channel_2_owner = member_2;
        channel_3_owner = admin_1;
        channel_1_id = channelService.addChannel(channelCreateCommand_1, channel_1_owner);
        channel_2_id = channelService.addChannel(channelCreateCommand_2, channel_2_owner);
        channel_3_id = channelService.addChannel(channelCreateCommand_3, channel_3_owner);
    }
}
