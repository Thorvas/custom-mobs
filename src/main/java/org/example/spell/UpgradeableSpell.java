package org.example.spell;

public interface UpgradeableSpell extends Spell {

    @Override
    int getExperience();

    void setExperience(int experience);

    default void addExperience(int amount) {
        setExperience(getExperience() + amount);
    }
}
