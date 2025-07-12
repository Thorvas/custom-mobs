package org.example.util;

import org.bukkit.entity.Player;

public class ExperienceUtil {

    /**
     * Calculates the total experience of a player using Minecraft's XP formula.
     */
    public static int getTotalExperience(Player player) {
        int level = player.getLevel();
        float progress = player.getExp();
        int expForLevel = experienceForLevel(level);
        int expToLevel = experienceToNextLevel(level);
        return expForLevel + Math.round(expToLevel * progress);
    }

    /**
     * Experience needed to reach the start of the given level.
     */
    public static int experienceForLevel(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        } else if (level <= 31) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
    }

    /**
     * Experience required to progress from the given level to the next level.
     */
    public static int experienceToNextLevel(int level) {
        if (level <= 15) {
            return 2 * level + 7;
        } else if (level <= 30) {
            return 5 * level - 38;
        } else {
            return 9 * level - 158;
        }
    }

    /**
     * Sets the player's total experience, adjusting level and progress.
     */
    public static void setTotalExperience(Player player, int xp) {
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);

        int level = 0;
        int xpForNext = experienceToNextLevel(level);
        while (xp >= xpForNext) {
            xp -= xpForNext;
            level++;
            xpForNext = experienceToNextLevel(level);
        }

        player.setLevel(level);
        player.setExp(xp / (float) xpForNext);
        player.setTotalExperience(experienceForLevel(level) + xp);
    }
}
