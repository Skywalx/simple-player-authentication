package com.skywalx.simpleplayerauthentication.listener;

import com.skywalx.simpleplayerauthentication.listener.exclusions.BlacklistedLoginEventExclusion;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BlacklistedLoginEventExclusionTest {

    @Test
    void isNotBlacklisted_withLoginCommand_shouldReturnTrue() {
        BlacklistedLoginEventExclusion blacklistedLoginEventExclusion = new BlacklistedLoginEventExclusion();
        PlayerCommandPreprocessEvent commandEvent = mock(PlayerCommandPreprocessEvent.class);
        when(commandEvent.getMessage()).thenReturn("/login test");

        boolean notBlacklisted = blacklistedLoginEventExclusion.isNotBlacklisted(commandEvent);

        assertThat(notBlacklisted).isTrue();
    }

}