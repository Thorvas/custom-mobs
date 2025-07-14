package org.example.spell;

public interface UpgradeableSpell extends Spell {

    @Override
    int getExperience();

    void setExperience(int experience);

    default void addExperience(int amount) {
        setExperience(getExperience() + amount);
    }

    /**
     * Returns the current level derived from the stored experience value.
     */
    default int getLevel() {
        return org.example.util.SpellExperienceUtil.levelForExperience(getExperience());
    }

    /**
     * Remaining experience points required to reach the next level.
     */
    default int experienceToNextLevel() {
        return org.example.util.SpellExperienceUtil.xpNeeded(getExperience());
    }
}
