package org.example.handler;

import org.example.context.SpellContext;
import org.example.spell.Spell;

public interface CastingPipelineHandler {

    void execute(SpellContext context);
    boolean supports(Spell spell);
}
