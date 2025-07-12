package org.example.calculator.meteor;

import org.example.calculator.IKnockbackCalculator;
import org.example.spell.Spell;
import org.example.spell.meteor.MeteorSpell;

public class MeteorKnockbackCalculator implements IKnockbackCalculator {
    @Override
    public double calculateKnockback(Spell spell, double distance) {
        MeteorSpell meteorSpell = (MeteorSpell) spell;

        return Math.max(0.2, 1.0 - (distance / meteorSpell.getEXPLOSION_RADIUS()));
    }
}
