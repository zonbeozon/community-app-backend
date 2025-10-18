package com.zonbeozon.member.domain;

import com.zonbeozon.image.entity.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfile {
    @EmbeddedId
    private MemberProfileId id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false, insertable = false)
    @MapsId("memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false, updatable = false, insertable = false)
    @MapsId("imageId")
    private Image image;

    private boolean isSharedResource = false; //공통으로 사용하는 리소스 여부

    public MemberProfile(Member member, Image image) {
        this.member = member;
        this.image = image;
    }

    public MemberProfile(Member member, Image image, boolean isSharedResource) {
        this.member = member;
        this.image = image;
        this.isSharedResource = isSharedResource;
    }
}
