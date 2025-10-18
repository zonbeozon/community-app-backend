package com.zonbeozon.member.service;

import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageDeleter;
import com.zonbeozon.image.service.ImageFinder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.MemberProfile;
import com.zonbeozon.member.respository.MemberProfileRepository;
import com.zonbeozon.member.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberUpdater {
    private final MemberRepository memberRepository;
    private final MemberFinder memberFinder;

    public void updateUsername(Long memberId, String newUsername) {
        Member member = memberFinder.findByIdWithProfileElseThrow(memberId);
        if(isExistUsername(newUsername))
            throw new ConflictException(ErrorCode.DUPLICATE_USERNAME);
        member.updateUsername(newUsername);
    }

    private boolean isExistUsername(String username) {
        return memberRepository.existsByUsername(username);
    }
}
