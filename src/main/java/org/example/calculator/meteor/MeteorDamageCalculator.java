package org.example.calculator.meteor;

import org.example.calculator.IDamageCalculator;
import org.example.spell.Spell;
import org.example.spell.meteor.MeteorSpell;

public class MeteorDamageCalculator implements IDamageCalculator {
    @Override
    public double calculateDamage(Spell spell) {
        MeteorSpell meteorSpell = (MeteorSpell) spell;
        int level = meteorSpell.getExperience();

        return (double) (5 * level) / (level + 15);
    }
}
