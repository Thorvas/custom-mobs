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
        for (PreCastPipelineHandler handler : preCastPipelineHandlers) {
            if (handler.execute(context)) {
                return false; // If any handler returns true, stop the pre-cast phase
            }
        }
        return true;
    }

    public void handleCasting(SpellContext context) {
        // Logic for handling casting phase
        castingPipelineHandlers.forEach(handler -> {
            handler.execute(context);
        });
    }
}
