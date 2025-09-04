package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.enums.*;
import com.zonbeozon.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "channel_type")
@Table(indexes = @Index(name = "idx_channel_latest_event", columnList = "latestEventOccurred DESC"))
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

    @Setter
    private LocalDateTime latestEventOccurred;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChannelMember> channelMembers = new HashSet<>();

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
