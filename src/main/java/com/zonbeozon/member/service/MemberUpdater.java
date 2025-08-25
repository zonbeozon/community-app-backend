package com.zonbeozon.member.service;

import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageDeleter;
import com.zonbeozon.image.service.ImageFinder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.MemberProfile;
import com.zonbeozon.member.dto.MemberResponse;
import com.zonbeozon.member.respository.MemberProfileRepository;
import com.zonbeozon.member.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberUpdater {
    private final MemberRepository memberRepository;
    private final MemberProfileRepository profileRepository;
    private final MemberFinder memberFinder;
    private final ImageFinder imageFinder;
    private final ImageDeleter imageDeleter;

    public MemberResponse updateUsername(Long memberId, String newUsername) {
        Member member = memberFinder.findByIdWithProfileElseThrow(memberId);
        if(isExistUsername(newUsername))
            throw new ConflictException(ErrorCode.DUPLICATE_USERNAME);
        member.updateUsername(newUsername);
        return MemberResponse.from(member);
    }

    private boolean isExistUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    public MemberResponse updateProfile(Long memberId,@Nullable Long imageId) {
        Member member = memberFinder.findByIdWithProfileElseThrow(memberId);
        if(member.getProfile() != null) deleteExistProfile(member);

        if(imageId == null) {
            return MemberResponse.from(member);
        }

        Image image = imageFinder.findByIdElseThrow(imageId);
        MemberProfile memberProfile = profileRepository.save(new MemberProfile(member, image));
        member.updateProfile(memberProfile);
        return MemberResponse.from(member);
    }

    private void deleteExistProfile(Member member) {
        Image existImage = member.getProfile().getImage();
        profileRepository.deleteById(member.getProfile().getId());
        member.updateProfile(null);
        imageDeleter.deleteImage(existImage.getId());
    }
}
