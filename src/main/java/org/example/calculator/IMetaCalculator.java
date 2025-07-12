package org.example.calculator;

import net.kyori.adventure.text.Component;
import org.example.spell.Spell;

public interface IMetaCalculator {

    Component getMeta(Spell spell);
    boolean supports(Spell spell);
}
