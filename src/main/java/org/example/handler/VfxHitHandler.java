package org.example.handler;

import org.example.context.SpellContext;
import org.example.context.SpellHitContext;

public interface VfxHitHandler {
    void render(SpellContext context, SpellHitContext spellHitContext);
}
