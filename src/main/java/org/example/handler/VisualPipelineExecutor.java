package org.example.handler;

import org.example.context.VisualSpellContext;

import java.util.List;

public class VisualPipelineExecutor {

    public VisualPipelineExecutor(List<VisualPipelineHandler> visualPipelineHandlers) {
        this.visualPipelineHandlers = visualPipelineHandlers;
    }

    private final List<VisualPipelineHandler> visualPipelineHandlers;

    public void handleSpellRendering(VisualSpellContext context) {
        visualPipelineHandlers.stream()
                .filter(handler -> handler.supports(context.getSpell()))
                .forEach(handler -> handler.execute(context));
    }
}
