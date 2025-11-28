package com.zonbeozon.chat.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ChattingGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "chattingGroup", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>();

    public ChattingGroup(String name) {
        this.name = name;
    }
}
