package com.zonbeozon.channel.repository;

import com.zonbeozon.config.QueryDslConfig;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import com.zonbeozon.member.respository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
public class ChannelCustomRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("findJoinedChannels 테스트")
    class FindJoinedChannelsTest {
        private Member member;

        @BeforeEach
        void setup() {
            member = new Member("albas", "test@gmail.com", "emptyProfile", ServerRole.USER);
            memberRepository.save(member);
        }

        @Test
        @DisplayName("member가 속한 채널만 가져와야 한다.")
        void d() {
            channelRepository.findJoinedChannels(member);


        }

        @Test
        @DisplayName("최근 Post 작성일 기준 Desc Order로 가져와야 한다.")
        void djlj() {


        }

        @Test
        @DisplayName("최근 Post content, creater_at필드를 가져와야 한다.")
        void djkj() {

        }

        @Test
        @DisplayName("Post가 없다면 Null값이 들어가야한다.")
        void kkkd() {

        }
    }
}
