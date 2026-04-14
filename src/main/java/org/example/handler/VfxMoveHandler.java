package org.example.handler;

import org.example.context.SpellContext;
import org.example.context.SpellMoveContext;

public interface VfxMoveHandler {
    void render(SpellContext context, SpellMoveContext spellMoveContext);
}
