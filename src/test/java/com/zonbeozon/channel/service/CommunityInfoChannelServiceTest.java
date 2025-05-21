package com.zonbeozon.channel.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@ExtendWith(MockitoExtension.class)
public class CommunityInfoChannelServiceTest {
//    @Autowired
//    private CommunityInfoChannelRepository communityInfoChannelRepository;
//    @MockitoBean
//    private ChannelPermissionValidatorImpl channelPermissionValidator;
//    @MockitoBean
//    private ChannelMemberService channelMemberService;
//    @MockitoBean
//    private ChannelInvitationService channelInvitationService;
//    @InjectMocks
//    private CommunityInfoChannelService communityInfoChannelService;
//
//    @BeforeEach
//    void setUp() {
//        doNothing().when(channelPermissionValidator).validate(any(), any());
//    }
//
//    @DisplayName("채널 추가")
//    @Nested
//    class ChannelAdd {
//
//        @BeforeEach
//        void setUp() {
//            doReturn(true).when(communityInfoChannelRepository).save(any());
//        }
//
//        @DisplayName("채널을 저장해야 한다.")
//        @Test
//        void shouldRegisterChannel() {
//            //given
//            ChannelCreateRequest request = new ChannelCreateRequest(
//                    "채널명",
//                    "설명 생략",
//                    Channel.OpenLevel.PUBLIC
//            );
//            Member member = new Member("영희", "1234@gmail.com", "profile.com", ServerRole.USER);
//            //when
//            communityInfoChannelService.addCommunityInfoChannel(request, member);
//            //then
//        }
//
//        @DisplayName("채널 생성자를 Owner로 등록해야 한다.")
//        @Test
//        void shouldRegisterCreatorAsOwner() {
//            //given
//            ChannelCreateRequest request = new ChannelCreateRequest(
//                    "채널명",
//                    "설명 생략",
//                    Channel.OpenLevel.PUBLIC
//            );
//            Member member = new Member("영희", "1234@gmail.com", "profile.com", ServerRole.USER);
//            //when
//            communityInfoChannelService.addCommunityInfoChannel(request, member);
//            //then
//            Mockito.verify(channelMemberService).subscribeChannel(member, any(CommunityInfoChannel.class), ChannelRole.CHANNEL_OWNER);
//        }
//
//    }
}
