package org.example.calculator.fireball;

import org.example.calculator.IDamageCalculator;
import org.example.context.SpellContext;
import org.example.spell.frostbolt.FireballSpell;

public class FireballDamageCalculator implements IDamageCalculator {
    @Override
    public double calculateDamage(SpellContext context) {
        FireballSpell fireballSpell = (FireballSpell) context.getSpell();
        int level = fireballSpell.getExperience();

        return (double) (5 * level) / (level + 15);
    }
}
