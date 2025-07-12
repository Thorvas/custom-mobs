package org.example.handler;

import org.example.context.PreCastSpellContext;
import org.example.context.SpellContext;

import java.util.List;

public class PipelineExecutor {

    public PipelineExecutor(List<PreCastPipelineHandler> preCastPipelineHandlers,
                            List<CastingPipelineHandler> castingPipelineHandlers) {
        this.castingPipelineHandlers = castingPipelineHandlers;
        this.preCastPipelineHandlers = preCastPipelineHandlers;
    }

    private final List<CastingPipelineHandler> castingPipelineHandlers;
    private final List<PreCastPipelineHandler> preCastPipelineHandlers;

    public boolean handlePreCast(PreCastSpellContext context) {

         return preCastPipelineHandlers.stream()
                .filter(handler -> handler.supports(context.getSpell()))
                .noneMatch(handler -> handler.execute(context));
    }

    public void handleCasting(SpellContext context) {

        castingPipelineHandlers.stream()
                .filter(handler -> handler.supports(context.getSpell()))
                .forEach(handler -> handler.execute(context));
    }
}
