package org.example.calculator;

import net.kyori.adventure.text.Component;
import org.example.spell.Spell;

public interface ICalculateManager {

    boolean supports(Spell spell);

}
