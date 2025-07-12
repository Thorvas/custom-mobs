package org.example.calculator.meteor;

import org.example.calculator.IKnockbackCalculator;
import org.example.context.SpellContext;
import org.example.spell.meteor.MeteorSpell;

public class MeteorKnockbackCalculator implements IKnockbackCalculator {
    @Override
    public double calculateKnockback(SpellContext context, double distance) {
        MeteorSpell meteorSpell = (MeteorSpell) context.getSpell();

        return Math.max(0.2, 1.0 - (distance / meteorSpell.getEXPLOSION_RADIUS()));
    }
}
