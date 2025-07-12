package org.example.calculator.meteor;

import org.example.calculator.IDamageCalculator;
import org.example.context.SpellContext;
import org.example.spell.meteor.MeteorSpell;

/**
 * Calculates meteor damage based on the spell's experience level.
 *
 * <p>Formula: <code>(5 * exp) / (exp + 15)</code> using the experience
 * from the {@link MeteorSpell} instance contained in the {@link SpellContext}.</p>
 */
public class MeteorDamageCalculator implements IDamageCalculator {
    @Override
    public double calculateDamage(SpellContext context) {
        MeteorSpell meteorSpell = (MeteorSpell) context.getSpell();
        int level = meteorSpell.getExperience();

        return (double) (5 * level) / (level + 15);
    }
}
