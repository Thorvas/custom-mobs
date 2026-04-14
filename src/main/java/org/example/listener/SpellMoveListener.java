package org.example.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.example.event.SpellMoveEvent;
import org.example.handler.VfxMoveHandler;
import org.example.handler.fireball.FireballVfxMoveHandler;
import org.example.handler.meteor.MeteorVfxMoveHandler;
import org.example.type.SpellType;

import java.util.Map;

public class SpellMoveListener implements Listener {

    private final Map<SpellType, VfxMoveHandler> renderHandlers;

    public SpellMoveListener(FireballVfxMoveHandler fireballHandler, MeteorVfxMoveHandler meteorHandler) {
        this.renderHandlers = Map.of(
                SpellType.FIREBALL, fireballHandler,
                SpellType.METEOR, meteorHandler
        );
    }

    @EventHandler
    public void handle(SpellMoveEvent event) {
        renderHandlers.get(event.getSpellContext().getSpell().getSpellType()).render(event.getSpellContext(), event.getSpellMoveContext());
    }
}
