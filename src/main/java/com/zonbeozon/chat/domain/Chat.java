package com.zonbeozon.chat.domain;

import com.zonbeozon.global.entity.BaseTimeEntity;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"parent", "children", "chatImages", "chattingGroup"})
public class Chat extends BaseTimeEntity {
    public final static int MAX_IMAGE_COUNT = 3;
    public final static int MAX_CONTENT_COUNT = 512;
    public final static int MIN_CONTENT_COUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_group_id")
    private ChattingGroup chattingGroup;

    @Column(nullable = false)
    @Setter
    private String content;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @Setter
    private List<ChatImage> chatImages = new ArrayList<>();

    /**
     * if parent is null, this is root chat
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Chat parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("createdAt DESC, id DESC")
    private List<Chat> children = new ArrayList<>();

    public Chat(ChattingGroup chattingGroup, Member author, String content, Chat parent) {
        this.chattingGroup = chattingGroup;
        this.author = author;
        this.content = content;
        this.parent = parent;
    }

    public Chat(ChattingGroup chattingGroup, Member author, String content) {
        this.chattingGroup = chattingGroup;
        this.author = author;
        this.content = content;
    }

    public boolean canAddImage(int sizeToAdd) {
        return chatImages.size() + sizeToAdd <= MAX_IMAGE_COUNT;
    }

    public void addImages(List<ChatImage> images) {
        chatImages.addAll(images);
    }

    public boolean isRoot() {
        return parent == null;
    }

    public List<Image> getImages() {
        return chatImages.stream().map(ChatImage::getImage).toList();
    }

    public List<Long> getImageIds() {
        return getImages().stream().map(Image::getId).toList();
    }
}
