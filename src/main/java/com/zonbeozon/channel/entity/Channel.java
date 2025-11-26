package com.zonbeozon.channel.entity;

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
@Table(indexes = @Index(name = "idx_channel_latest_event", columnList = "latestEventOccurred DESC"))
public class Channel extends BaseTimeEntity {
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
    @Setter
    private ChannelProfile profile;

    @NotNull
    @Embedded
    private ChannelSetting setting;

    private LocalDateTime latestEventOccurred = null;

    private Long memberCount = 0L;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChannelMember> channelMembers = new HashSet<>();

    public Channel(
            String title,
            String description,
            ChannelSetting setting
    ) {
        this.title = title;
        this.description = description;
        this.setting = setting;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
