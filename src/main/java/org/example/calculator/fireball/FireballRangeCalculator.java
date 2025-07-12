package org.example.calculator.fireball;

import org.example.calculator.IRangeCalculator;
import org.example.spell.Spell;
import org.example.spell.frostbolt.FireballSpell;

public class FireballRangeCalculator implements IRangeCalculator {
    @Override
    public double calculateRange(Spell spell) {
        FireballSpell fireballSpell = (FireballSpell) spell;

        return fireballSpell.getMaxRange();
    }
}
