package com.zonbeozon.global.entity;

import com.zonbeozon.member.domain.Member;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * todo: post의 image, reaction을 해당 필드로 옮겨 comment에도 적용
 */
@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ContentEntity extends BaseTimeEntity {
    private Long viewCount = 0L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    protected Member author;

    protected ContentEntity(Member author) {
        this.author = author;
    }

    public void increaseViewCount(Long amount) {
        this.viewCount += amount;
    }

    public abstract Long getId();
}
