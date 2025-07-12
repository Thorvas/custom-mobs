package org.example.calculator.fireball;

import org.example.calculator.IRangeCalculator;
import org.example.context.SpellContext;
import org.example.spell.frostbolt.FireballSpell;

public class FireballRangeCalculator implements IRangeCalculator {
    @Override
    public double calculateRange(SpellContext context) {
        FireballSpell fireballSpell = (FireballSpell) context.getSpell();

        return fireballSpell.getMaxRange();
    }
}
