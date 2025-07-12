package org.example.calculator.fireball;

import org.example.calculator.IDamageCalculator;
import org.example.spell.Spell;
import org.example.spell.frostbolt.FireballSpell;

public class FireballDamageCalculator implements IDamageCalculator {
    @Override
    public double calculateDamage(Spell spell) {
        FireballSpell fireballSpell = (FireballSpell) spell;
        int level = fireballSpell.getExperience();

        return (double) (5 * level) / (level + 15);
    }
}
