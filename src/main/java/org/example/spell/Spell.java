package org.example.spell;

import net.kyori.adventure.text.Component;
import org.example.calculator.ICalculateManager;
import org.example.context.DamageType;

public interface Spell {
    String getId();
    String getName();
    Long getCooldown();
    DamageType getType();
    Component getTitle();
    Component getContent();
    int getExperience();

}
