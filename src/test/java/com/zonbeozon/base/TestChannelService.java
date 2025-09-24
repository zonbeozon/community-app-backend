package com.zonbeozon.base;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.ChannelProfile;
import com.zonbeozon.channel.entity.ChannelSetting;
import com.zonbeozon.channel.enums.*;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelProfileRepository;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.image.ImageRepository;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestChannelService {
    protected static final ChannelSetting DEFAULT_CHANNEL_SETTING = new ChannelSetting(ChannelContentVisibility.PUBLIC, ChannelJoinPolicy.OPEN);
    protected static final ChannelCreatorType DEFAULT_CREATOR_TYPE = ChannelCreatorType.COMMUNITY;
    protected static final String DEFAULT_NAME = "test-channel";

    @Autowired
    private ChannelMemberRepository channelMemberRepository;
    @Autowired
    private ChannelProfileRepository channelProfileRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ImageRepository imageRepository;

    public ChannelMember joinAsMember(Channel channel, Member member) {
        ChannelMember channelMember = ChannelMember.create(member, channel, ChannelRole.CHANNEL_MEMBER);
        return channelMemberRepository.save(channelMember);
    }

    public ChannelMember joinAsAdmin(Channel channel, Member member) {
        ChannelMember channelMember = ChannelMember.create(member, channel, ChannelRole.CHANNEL_ADMIN);
        return channelMemberRepository.save(channelMember);
    }

    public ChannelMember findByChannelAndMemberElseThrow(Channel channel, Member member) {
        return channelMemberRepository.findByChannelIdAndMemberId(channel.getId(), member.getId()).orElseThrow();
    }

    /**
     * 호출시 주의: 채널 당 owner는 한명만 가능하다.
     */
    public ChannelMember joinAsOwner(Channel channel, Member member) {
        ChannelMember channelMember = ChannelMember.create(member, channel, ChannelRole.CHANNEL_OWNER);
        return channelMemberRepository.save(channelMember);
    }

    public void changeRole(ChannelMember channelMember, ChannelRole channelRole) {
        channelMember.updateRole(channelRole);
    }

    public ChannelProfile setChannelProfile(Channel channel, Image image) {
        ChannelProfile channelProfile = new ChannelProfile(channel ,image);
        ChannelProfile profile = channelProfileRepository.save(channelProfile);
        channel.setProfile(channelProfile);
        return profile;
    }

    public void clearAll() {
        channelMemberRepository.deleteAll();
        channelProfileRepository.deleteAll();
        channelRepository.deleteAll();
    }
}
