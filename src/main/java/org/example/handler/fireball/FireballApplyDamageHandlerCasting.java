package org.example.handler.fireball;

import org.example.calculator.fireball.FireballCalculateManager;
import org.example.calculator.fireball.FireballDamageCalculator;
import org.example.context.SpellContext;
import org.example.context.SpellContextAttributes;
import org.example.handler.CastingPipelineHandler;
import org.example.spell.Spell;
import org.example.spell.frostbolt.FireballSpell;

public class FireballApplyDamageHandlerCasting implements CastingPipelineHandler {

    public FireballApplyDamageHandlerCasting(FireballCalculateManager fireballDamageCalculator) {
        this.fireballDamageCalculator = fireballDamageCalculator;
    }

    private final FireballCalculateManager fireballDamageCalculator;

    @Override
    public void execute(SpellContext context) {
        context.getTarget().getTarget().damage(fireballDamageCalculator.calculateDamage(context));
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof FireballSpell;
    }
}
