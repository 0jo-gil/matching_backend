package com.matching.chat.domain;

import com.matching.common.domain.BaseEntity;
import com.matching.member.domain.Member;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member member;

    private String message;


    public enum MessageType {
        ENTER, QUIT, TALK
    }


    public static ChatMessage createChatMessage(ChatRoom chatRoom, Member member, String message, MessageType type) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(member)
                .message(message)
                .type(type)
                .build();
    }

}
