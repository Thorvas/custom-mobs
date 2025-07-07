package org.example.handler;

import org.example.context.PreCastSpellContext;
import org.example.context.SpellContext;

public interface PreCastPipelineHandler {
    boolean execute(PreCastSpellContext context);
}
