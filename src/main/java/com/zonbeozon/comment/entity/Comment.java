package com.zonbeozon.comment.entity;

import com.zonbeozon.global.entity.BaseTimeEntity;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(
        indexes = {
                //댓글 조회용 인덱스
                @Index(name = "idx_comment_post_id_createdAt", columnList = "post_id, createdAt"),
                //댓글 집계용 인덱스
                @Index(name = "idx_comment_aggregated_post_id", columnList = "aggregated, post_id")
        }
)
public class Comment extends BaseTimeEntity {
    public static final int MAX_CONTENT_LENGTH = 496;
    public static final int MIN_CONTENT_LENGTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = MIN_CONTENT_LENGTH, max = MAX_CONTENT_LENGTH)
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @NotNull
    private Member author;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * 이 Comment가 PostMetric에 집계되었는지 여부.
     * false: 아직 집계되지 않음 (실시간 집계 대상)
     * true:  집계 완료
     */
    @Column(nullable = false)
    private boolean aggregated = false;


    public Comment(String content, Member author, Post post) {
        this.content = content;
        this.author = author;
        this.post = post;
    }

    public void updateContent(String content) {
        this.content = content;
    }

}
