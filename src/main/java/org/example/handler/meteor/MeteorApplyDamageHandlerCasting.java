package org.example.handler.meteor;

import org.example.calculator.meteor.MeteorCalculateManager;
import org.example.context.SpellContext;
import org.example.handler.CastingPipelineHandler;
import org.example.spell.meteor.MeteorSpell;
import org.example.spell.Spell;

public class MeteorApplyDamageHandlerCasting implements CastingPipelineHandler {

    private final MeteorCalculateManager meteorCalculateManager;

    public MeteorApplyDamageHandlerCasting(MeteorCalculateManager meteorCalculateManager) {
        this.meteorCalculateManager = meteorCalculateManager;
    }

    @Override
    public void execute(SpellContext context) {
        context.getTarget().getTarget().damage(meteorCalculateManager.calculateDamage(context));
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof MeteorSpell;
    }
}
