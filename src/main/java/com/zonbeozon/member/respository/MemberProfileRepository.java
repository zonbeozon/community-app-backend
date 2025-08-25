package com.zonbeozon.member.respository;

import com.zonbeozon.member.domain.MemberProfile;
import com.zonbeozon.member.domain.MemberProfileId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, MemberProfileId> {
}
