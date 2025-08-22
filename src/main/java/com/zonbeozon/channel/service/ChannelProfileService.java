package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelProfile;
import com.zonbeozon.channel.repository.ChannelProfileRepository;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageFinder;
import com.zonbeozon.image.service.ImageOwnershipVerifier;
import com.zonbeozon.image.service.ImageDeleter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChannelProfileService {
    private final ImageFinder imageFinder;
    private final ChannelFinder channelFinder;
    private final ChannelProfileRepository channelProfileRepository;
    private final ImageDeleter imageDeleter;

    public void updateImage(Long channelId, Long imageId) {
        Channel channel = channelFinder.findByIdElseThrow(channelId);

        if(imageId == null) {
            deleteProfile(channelId);
            return;
        }

        Image image = imageFinder.findByIdElseThrow(imageId);

        //기존 프로필이 없다면
        if(channel.getProfile() == null) {
            ChannelProfile channelProfile = new ChannelProfile(channel, image);
            channelProfileRepository.save(channelProfile);
            channel.updateChannelProfile(channelProfile);
            return;
        }
        //이전과 같은 상태
        if(channel.getProfile().getImage().getId().equals(imageId)) {
            return;
        }
        //프로필이 있지만 이미지 업데이트가 필요한 경우
        channel.getProfile().updateImage(image);
    }

    public void deleteProfile(Long channelId) {
        Channel channel = channelFinder.findChannelByIdWithChannelProfileElseThrow(channelId);
        if(channel.getProfile() == null) return;
        Long imageId = channel.getProfile().getImage().getId();
        channelProfileRepository.delete(channel.getProfile());
        channel.updateChannelProfile(null);
        imageDeleter.deleteImage(imageId);
    }
}
