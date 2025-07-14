package org.example.util;

/**
 * Utility class for converting spell experience (XP) to levels and back.
 * <p>
 * The formulas mirror Minecraft's experience system so that spells can share a
 * similar progression curve.
 * </p>
 */
public class SpellExperienceUtil {

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
     * Calculates the level for the given total experience.
     */
    public static int levelForExperience(int experience) {
        int level = 0;
        int xpForNext = experienceToNextLevel(level);
        while (experience >= xpForNext) {
            experience -= xpForNext;
            level++;
            xpForNext = experienceToNextLevel(level);
        }
        return level;
    }

    /**
     * Returns the remaining XP needed to reach the next level from the current
     * total experience value.
     */
    public static int xpNeeded(int currentXP) {
        int level = levelForExperience(currentXP);
        int xpIntoCurrent = currentXP - experienceForLevel(level);
        int xpForNext = experienceToNextLevel(level);
        return xpForNext - xpIntoCurrent;
    }
}
