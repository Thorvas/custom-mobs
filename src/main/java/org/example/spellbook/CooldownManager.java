package org.example.spellbook;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private Map<UUID, Long> cooldowns = new HashMap<>();

    public Map<UUID, Long> getCooldowns() {
        return cooldowns;
    }

    public void setCooldowns(Map<UUID, Long> cooldowns) {
        this.cooldowns = cooldowns;
    }

    public boolean isOnCooldown(UUID playerId) {
        Long cooldownEnd = cooldowns.get(playerId);
        if (cooldownEnd == null) {
            return false; // No cooldown set
        }
        return System.currentTimeMillis() < cooldownEnd; // Check if current time is less than cooldown end time
    }

    public void startCooldown(UUID playerId, long cooldownDuration) {
        long cooldownEnd = System.currentTimeMillis() + cooldownDuration;
        cooldowns.put(playerId, cooldownEnd);
    }
}
