package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.controller.ChannelCreateRequest;
import com.zonbeozon.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE channel SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Channel extends BaseTimeEntity {
    public static final int MAX_TITLE_LENGTH = 30;
    public static final int MAX_DESCRIPTION_LENGTH = 1000;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true, length = MAX_TITLE_LENGTH)
    private String title;
    @NotNull
    @Size(max = MAX_DESCRIPTION_LENGTH)
    @Column(columnDefinition = "TEXT")
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Type channelType;
    @NotNull
    private boolean isDeleted;
    @NotNull
    @Enumerated(EnumType.STRING)
    private OpenLevel openLevel;

    private Channel(String title, String description, OpenLevel openLevel, Type channelType) {
        this.title = title;
        this.description = description;
        this.openLevel = openLevel;
        this.channelType = channelType;
        this.isDeleted = false;
    }

    public void updateInfo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void changeOpenLevel(OpenLevel openLevel) {
        this.openLevel = openLevel;
    }


    public enum Type {
        /**
         * 일반 유저가 생성하는 채널
         * chat 패키지는 Community Info 채널이 사용하는 패키지이다.
         */
        COMMUNITY_INFO,
        /**
         * 서버 운영자가 생성하는 채널
         * discussion 패키지는 Official Info 채널이 사용하는 패키지이다.
         */
        OFFICIAL_INFO;
    }

    public enum OpenLevel {
        PUBLIC, PRIVATE;
    }

    public static Channel create(ChannelCreateRequest request) {
        return new Channel(request.title(), request.description(), request.openLevel(), request.type());
    }
    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                '}';
    }
}
