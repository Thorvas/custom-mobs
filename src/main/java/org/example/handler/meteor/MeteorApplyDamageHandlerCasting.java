package org.example.handler.meteor;

import org.example.context.SpellContext;
import org.example.handler.CastingPipelineHandler;
import org.example.spell.meteor.MeteorSpell;
import org.example.spell.Spell;

public class MeteorApplyDamageHandlerCasting implements CastingPipelineHandler {
    @Override
    public void execute(SpellContext context) {
        context.getTarget().getTarget().damage(context.getDamage());
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof MeteorSpell;
    }
}
