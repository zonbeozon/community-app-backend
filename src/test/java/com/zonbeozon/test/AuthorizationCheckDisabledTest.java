package com.zonbeozon.test;

import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.image.service.ImageOwnershipVerifier;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ActiveProfiles("test")
public class AuthorizationCheckDisabledTest {
    @MockitoBean
    protected ChannelAuthorizationCheckService channelAuthorizationCheckService;
    @MockitoBean
    protected PostAuthorizationCheckService postAuthorizationCheckService;
    @MockitoBean
    protected ImageOwnershipVerifier imageOwnershipVerifier;

    @BeforeEach
    void disableAll()  {
        Mockito.doReturn(true).when(channelAuthorizationCheckService).isAtLeastMember(Mockito.anyLong());
        Mockito.doReturn(true).when(channelAuthorizationCheckService).isOwner(Mockito.anyLong());
        Mockito.doReturn(true).when(channelAuthorizationCheckService).isAtLeastAdmin(Mockito.anyLong());
        Mockito.doReturn(true).when(channelAuthorizationCheckService).canAccessChannelContent(Mockito.anyLong());
        Mockito.doReturn(true).when(channelAuthorizationCheckService).hasHigherRoleThanTargetMember(Mockito.anyLong(), Mockito.anyLong());

        Mockito.doReturn(true).when(postAuthorizationCheckService).isAtLeastMember(Mockito.anyLong());
        Mockito.doReturn(true).when(postAuthorizationCheckService).isAuthor(Mockito.anyLong());
        Mockito.doReturn(true).when(postAuthorizationCheckService).canAccessChannelContent(Mockito.anyLong());
        Mockito.doReturn(true).when(postAuthorizationCheckService).isAuthorOrHasHigherRoleThanAuthor(Mockito.anyLong());
    }
}
