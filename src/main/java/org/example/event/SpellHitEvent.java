package org.example.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.example.context.SpellContext;
import org.example.context.SpellHitContext;
import org.jetbrains.annotations.NotNull;

public class SpellHitEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private SpellContext spellContext;
    private SpellHitContext spellHitContext;

    public SpellHitEvent(SpellContext spellContext, SpellHitContext spellHitContext) {
        this.spellHitContext = spellHitContext;
        this.spellContext = spellContext;
    }

    public SpellHitContext getSpellHitContext() {
        return spellHitContext;
    }

    public void setSpellHitContext(SpellHitContext spellHitContext) {
        this.spellHitContext = spellHitContext;
    }

    public SpellContext getSpellContext() {
        return spellContext;
    }

    public void setSpellType(SpellContext spellContext) {
        this.spellContext = spellContext;
    }

    @Override
    public @NotNull HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
