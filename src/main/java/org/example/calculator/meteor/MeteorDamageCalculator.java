package org.example.calculator.meteor;

import org.example.calculator.IDamageCalculator;
import org.example.context.SpellContext;
import org.example.spell.meteor.MeteorSpell;

public class MeteorDamageCalculator implements IDamageCalculator {
    @Override
    public double calculateDamage(SpellContext context) {
        MeteorSpell meteorSpell = (MeteorSpell) context.getSpell();
        int level = meteorSpell.getExperience();

        return (double) (5 * level) / (level + 15);
    }
}
