package com.zonbeozon.member.service;

import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageDeleter;
import com.zonbeozon.image.service.ImageFinder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.MemberProfile;
import com.zonbeozon.member.respository.MemberProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberProfileService {
    private final MemberProfileRepository profileRepository;
    private final MemberFinder memberFinder;
    private final ImageDeleter imageDeleter;
    private final ImageFinder imageFinder;
    private final DefaultMemberProfileProvider defaultMemberProfileProvider;

    public void setAsDefaultProfile(Long memberId) {
        Image defaultProfile = defaultMemberProfileProvider.getDefaultProfile();
        updateProfile(memberId, defaultProfile.getId(), true);
    }

    public void updateProfile(Long memberId, Long imageId, boolean isSharedResource) {
        Member member = memberFinder.findByIdWithProfileElseThrow(memberId);
        if(member.getProfile() != null) deleteProfile(member);
        if(imageId == null) return;
        Image image = imageFinder.findByIdElseThrow(imageId);
        MemberProfile memberProfile = profileRepository.save(new MemberProfile(member, image, isSharedResource));
        member.setProfile(memberProfile);
    }

    private void deleteProfile(Member member) {
        MemberProfile profile = member.getProfile();
        Image image = profile.getImage();
        member.setProfile(null);
        profileRepository.deleteById(profile.getId());
        if (!profile.isSharedResource()) imageDeleter.deleteImage(image.getId());
    }
}
