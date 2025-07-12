package org.example.calculator.fireball;

import org.example.calculator.ICalculateManager;
import org.example.spell.Spell;
import org.example.spell.frostbolt.FireballSpell;

public class FireballCalculateManager implements ICalculateManager {

    private final FireballDamageCalculator fireballDamageCalculator;
    private final FireballKnockbackCalculator fireballKnockbackCalculator;
    private final FireballRangeCalculator fireballRangeCalculator;

    public FireballCalculateManager(FireballDamageCalculator fireballDamageCalculator,
                                    FireballKnockbackCalculator fireballKnockbackCalculator,
                                    FireballRangeCalculator fireballRangeCalculator) {
        this.fireballDamageCalculator = fireballDamageCalculator;
        this.fireballKnockbackCalculator = fireballKnockbackCalculator;
        this.fireballRangeCalculator = fireballRangeCalculator;
    }

    public double calculateDamage(Spell spell) {
        return fireballDamageCalculator.calculateDamage(spell);
    }

    public double calculateKnockback(Spell spell, double distance) {
        return fireballKnockbackCalculator.calculateKnockback(spell, distance);
    }

    public double calculateRange(Spell spell) {
        return fireballRangeCalculator.calculateRange(spell);
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof FireballSpell;
    }
}
