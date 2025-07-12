package org.example.calculator.fireball;

import org.example.calculator.IKnockbackCalculator;
import org.example.spell.Spell;
import org.example.spell.frostbolt.FireballSpell;

public class FireballKnockbackCalculator implements IKnockbackCalculator {
    @Override
    public double calculateKnockback(Spell spell, double distance) {
        FireballSpell fireballSpell = (FireballSpell) spell;

        return Math.max(0.2, 1.0 - (distance / fireballSpell.getExplosionRadius()));
    }
}
