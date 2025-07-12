package org.example.calculator;

import net.kyori.adventure.text.Component;
import org.example.context.SpellContext;
import org.example.spell.Spell;

public interface IMetaCalculator {

    Component getMeta(SpellContext context);
    boolean supports(Spell spell);
}
