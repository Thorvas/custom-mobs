package org.example.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.example.event.SpellHitEvent;
import org.example.handler.VfxHitHandler;
import org.example.handler.fireball.FireballVfxHitHandler;
import org.example.handler.meteor.MeteorVfxHitHandler;
import org.example.type.SpellType;

import java.util.Map;

public class SpellHitListener implements Listener {

    private final Map<SpellType, VfxHitHandler> renderHandlers;

    public SpellHitListener(MeteorVfxHitHandler meteorVfxHitHandler, FireballVfxHitHandler fireballVfxHitHandler) {
        renderHandlers = Map.of(
                SpellType.FIREBALL, fireballVfxHitHandler,
                SpellType.METEOR, meteorVfxHitHandler
        );
    }

    @EventHandler
    public void handle(SpellHitEvent event) {
        renderHandlers.get(event.getSpellContext().getSpell().getSpellType()).render(event.getSpellContext(), event.getSpellHitContext());
    }
}
