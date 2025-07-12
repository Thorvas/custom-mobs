package org.example.handler;

import org.example.context.PreCastSpellContext;
import org.example.spell.Spell;

public interface PreCastPipelineHandler {
    boolean execute(PreCastSpellContext context);
    boolean supports(Spell spell);
}
