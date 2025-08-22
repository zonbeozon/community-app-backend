package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.enums.*;
import com.zonbeozon.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "channel_type")
public abstract class Channel extends BaseTimeEntity {
    public static final int MIN_TITLE_LENGTH = 2;
    public static final int MAX_TITLE_LENGTH = 32;

    public static final int MIN_DESCRIPTION_LENGTH = 0;
    public static final int MAX_DESCRIPTION_LENGTH = 256;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = MAX_TITLE_LENGTH, unique = true, nullable = false)
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(mappedBy = "channel")
    private ChannelProfile profile;

    @NotNull
    @Embedded
    private ChannelSetting setting;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelCreatorType creatorType;

    protected Channel(
            String title,
            String description,
            ChannelSetting setting,
            ChannelCreatorType creatorType
    ) {
        this.title = title;
        this.description = description;
        this.setting = setting;
        this.creatorType = creatorType;
    }

    public abstract ChannelType getChannelType();

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateChannelProfile(ChannelProfile profile) {
        this.profile = profile;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

}
