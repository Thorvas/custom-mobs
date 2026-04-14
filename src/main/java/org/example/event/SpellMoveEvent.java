package org.example.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.example.context.SpellContext;
import org.example.context.SpellMoveContext;
import org.jetbrains.annotations.NotNull;

public class SpellMoveEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private SpellContext spellContext;
    private SpellMoveContext spellMoveContext;

    public SpellMoveEvent(SpellContext spellContext, SpellMoveContext spellMoveContext) {
        this.spellContext = spellContext;
        this.spellMoveContext = spellMoveContext;
    }

    public SpellMoveContext getSpellMoveContext() {
        return spellMoveContext;
    }

    public void setSpellMoveContext(SpellMoveContext spellMoveContext) {
        this.spellMoveContext = spellMoveContext;
    }

    public SpellContext getSpellContext() {
        return spellContext;
    }

    public void setSpellContext(SpellContext spellContext) {
        this.spellContext = spellContext;
    }

    @Override
    public @NotNull HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
