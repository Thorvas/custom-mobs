package org.example.spell;

import net.kyori.adventure.text.Component;
import org.example.calculator.ICalculateManager;
import org.example.context.DamageType;
import org.example.type.SpellType;

public interface Spell {
    String getId();
    String getName();
    Long getCooldown();
    SpellType getSpellType();
    DamageType getType();
    Component getTitle();
    Component getContent();
    int getExperience();

}
