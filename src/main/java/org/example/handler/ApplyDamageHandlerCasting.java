package org.example.handler;

import org.example.context.SpellContext;

public class ApplyDamageHandlerCasting implements CastingPipelineHandler {
    @Override
    public void execute(SpellContext context) {
        context.getTarget().getTarget().damage(1);
    }
}
