package org.example.calculator.fireball;

import org.example.calculator.ICalculateManager;
import org.example.context.SpellContext;
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

    public double calculateDamage(SpellContext context) {
        return fireballDamageCalculator.calculateDamage(context);
    }

    public double calculateKnockback(SpellContext context, double distance) {
        return fireballKnockbackCalculator.calculateKnockback(context, distance);
    }

    public double calculateRange(SpellContext context) {
        return fireballRangeCalculator.calculateRange(context);
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof FireballSpell;
    }
}
