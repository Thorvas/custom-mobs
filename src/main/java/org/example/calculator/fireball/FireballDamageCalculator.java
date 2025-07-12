package org.example.calculator.fireball;

import org.example.calculator.IDamageCalculator;
import org.example.context.SpellContext;
import org.example.spell.frostbolt.FireballSpell;

/**
 * Calculates fireball damage based on the spell's experience level.
 *
 * <p>Formula: <code>(5 * exp) / (exp + 15)</code> where {@code exp}
 * is obtained from the {@link FireballSpell} instance in the context.
 * Higher experience therefore results in higher damage.</p>
 */
public class FireballDamageCalculator implements IDamageCalculator {
    @Override
    public double calculateDamage(SpellContext context) {
        FireballSpell fireballSpell = (FireballSpell) context.getSpell();
        int level = fireballSpell.getExperience();

        return (double) (5 * level) / (level + 15);
    }
}
