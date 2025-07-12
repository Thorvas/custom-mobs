package org.example.handler.fireball;

import org.example.context.SpellContext;
import org.example.handler.CastingPipelineHandler;
import org.example.spell.Spell;
import org.example.spell.frostbolt.FireballSpell;

public class FireballApplyDamageHandlerCasting implements CastingPipelineHandler {
    @Override
    public void execute(SpellContext context) {
        context.getTarget().getTarget().damage(context.getDamage());
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof FireballSpell;
    }
}
