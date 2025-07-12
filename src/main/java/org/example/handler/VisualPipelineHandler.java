package org.example.handler;

import org.example.context.VisualSpellContext;
import org.example.spell.Spell;

public interface VisualPipelineHandler {

    void execute(VisualSpellContext context);
    boolean supports(Spell spell);
}
