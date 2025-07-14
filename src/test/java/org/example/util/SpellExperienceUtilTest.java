package org.example.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpellExperienceUtilTest {

    @Test
    void levelAndXpConversions() {
        // level thresholds
        assertEquals(0, SpellExperienceUtil.levelForExperience(0));
        assertEquals(1, SpellExperienceUtil.levelForExperience(7));
        assertEquals(2, SpellExperienceUtil.levelForExperience(16));

        // experience needed to next level
        int xp = 20;
        int level = SpellExperienceUtil.levelForExperience(xp);
        int remaining = SpellExperienceUtil.xpNeeded(xp);
        int nextThreshold = SpellExperienceUtil.experienceForLevel(level + 1);
        assertEquals(nextThreshold - xp, remaining);
    }
}
