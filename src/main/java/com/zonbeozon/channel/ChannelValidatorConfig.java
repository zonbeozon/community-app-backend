package com.zonbeozon.channel;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelAction;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.ChannelRole;
import com.zonbeozon.channel.service.dto.ChannelActionContext;
import com.zonbeozon.member.domain.ServerRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ChannelValidatorConfig {

    static boolean allowAll(ChannelContext context) {
        return true;
    }

    static boolean denyAll(ChannelContext context) {
        return false;
    }

    static boolean isChannelOwner(ChannelContext context) {
        return context.getChannelMember().getRole() == ChannelRole.CHANNEL_OWNER;
    }

    static boolean isChannelAdmin(ChannelContext context) {
        return context.getChannelMember().getRole() == ChannelRole.CHANNEL_ADMIN;
    }

    static boolean isServerAdmin(ChannelContext context) {
        return context.getMember().isAdmin();
    }

    static boolean isHigherLevel(ChannelContext context, ChannelMember target) {
        return context.getChannelMember().getRole().isHigherThan(target.getRole());
    }

    @Bean
    ChannelPermissionValidator channelPermissionValidator() {
        return new ChannelPermissionValidatorRouter(List.of(
                communityInfoChannelPermissionValidator(),
                officialInfoChannelPermissionValidator())
        );
    }

    ChannelPermissionValidatorImpl communityInfoChannelPermissionValidator() {
        Map<ChannelAction, ChannelPermissionValidateHandler> handlers = createCommunityInfoChannelPermissionValidateHandlers();
        return new ChannelPermissionValidatorImpl(handlers, Channel.Type.COMMUNITY_INFO);
    }

    Map<ChannelAction, ChannelPermissionValidateHandler> createCommunityInfoChannelPermissionValidateHandlers() {
        Map<ChannelAction, ChannelPermissionValidateHandler> handlers = new HashMap<>();;
        handlers.put(ChannelAction.CREATION, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::allowAll
        ));
        //Channel Owner만 허용
        handlers.put(ChannelAction.DELETION, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::isChannelOwner
        ));
        handlers.put(ChannelAction.INFO_MODIFICATION, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::isChannelOwner
        ));
        //더 높은 채널 role을 가진 사람이면 허용
        handlers.put(ChannelAction.MEMBER_KICK, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::isHigherLevel
        ));
        handlers.put(ChannelAction.MEMBER_PROMOTION, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::isChannelOwner
        ));
        handlers.put(ChannelAction.MEMBER_DEMOTION, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::isChannelOwner
        ));
        handlers.put(ChannelAction.JOIN, ChannelPermissionValidateHandler.of(
                (context) -> context.getChannel().getOpenLevel() != Channel.OpenLevel.PRIVATE
        ));
        handlers.put(ChannelAction.INVITE_JOIN, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::allowAll
        ));
        handlers.put(ChannelAction.INVITE, ChannelPermissionValidateHandler.of(
                (context) -> {
                    if(context.getChannel().getOpenLevel() == Channel.OpenLevel.PRIVATE) {
                        return isChannelAdmin(context) || isChannelOwner(context);
                    } else {
                        return allowAll(context);
                    }
                }
        ));
        return handlers;
    }

    ChannelPermissionValidatorImpl officialInfoChannelPermissionValidator() {
        Map<ChannelAction, ChannelPermissionValidateHandler> handlers = createOfficialInfoChannelPermissionValidateHandlers();
        return new ChannelPermissionValidatorImpl(handlers, Channel.Type.OFFICIAL_INFO);
    }

    Map<ChannelAction, ChannelPermissionValidateHandler> createOfficialInfoChannelPermissionValidateHandlers() {
        Map<ChannelAction, ChannelPermissionValidateHandler> handlers = new HashMap<>();
        handlers.put(ChannelAction.CREATION, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::isServerAdmin
        ));
        //서버 어드민만 허용
        handlers.put(ChannelAction.DELETION, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::isServerAdmin
        ));
        handlers.put(ChannelAction.INFO_MODIFICATION, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::isServerAdmin
        ));
        handlers.put(ChannelAction.MEMBER_KICK, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::isServerAdmin
        ));
        handlers.put(ChannelAction.MEMBER_PROMOTION, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::denyAll
        ));
        handlers.put(ChannelAction.MEMBER_DEMOTION, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::denyAll
        ));
        handlers.put(ChannelAction.JOIN, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::allowAll
        ));
        handlers.put(ChannelAction.INVITE_JOIN, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::allowAll
        ));
        handlers.put(ChannelAction.INVITE, ChannelPermissionValidateHandler.of(
                ChannelValidatorConfig::allowAll
        ));
        return handlers;
    }
}
