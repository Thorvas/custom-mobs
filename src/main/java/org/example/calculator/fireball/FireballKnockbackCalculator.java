package org.example.calculator.fireball;

import org.example.calculator.IKnockbackCalculator;
import org.example.context.SpellContext;
import org.example.spell.frostbolt.FireballSpell;

public class FireballKnockbackCalculator implements IKnockbackCalculator {
    @Override
    public double calculateKnockback(SpellContext context, double distance) {
        FireballSpell fireballSpell = (FireballSpell) context.getSpell();

        return Math.max(0.2, 1.0 - (distance / fireballSpell.getExplosionRadius()));
    }
}
