package com.skywalx.simpleplayerauthentication.listener;

import com.skywalx.simpleplayerauthentication.listener.exclusions.BlacklistedRegisterEventExclusion;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BlacklistedRegisterEventExclusionTest {

    @Test
    void isNotBlacklisted_withLoginCommand_shouldReturnTrue() {
        BlacklistedRegisterEventExclusion blacklistedRegisterEventExclusion = new BlacklistedRegisterEventExclusion();
        PlayerCommandPreprocessEvent commandEvent = mock(PlayerCommandPreprocessEvent.class);
        when(commandEvent.getMessage()).thenReturn("/register test test");

        boolean notBlacklisted = blacklistedRegisterEventExclusion.isNotBlacklisted(commandEvent);

        assertThat(notBlacklisted).isTrue();
    }

}