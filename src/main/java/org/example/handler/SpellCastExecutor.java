package org.example.handler;

import org.bukkit.plugin.java.JavaPlugin;
import org.example.context.SpellContext;
import org.example.handler.fireball.ProjectileSimulator;
import org.example.registry.SpellRegistry;
import org.example.type.SpellType;

import java.util.Map;

public class SpellCastExecutor {

    private final JavaPlugin plugin;
    private final ProjectileSimulator projectileSimulator;
    private final Map<SpellType, SpellHandler> handlers;

    public SpellCastExecutor(JavaPlugin plugin, SpellRegistry registry) {
        this.plugin = plugin;
        this.projectileSimulator = new ProjectileSimulator(plugin, registry);

        this.handlers = Map.of(
                SpellType.FIREBALL, projectileSimulator,
                SpellType.METEOR, projectileSimulator
        );
    }

    public void handleSpellCast(SpellContext context) {
        SpellType type = context.getSpell().getSpellType();
        SpellHandler handler = handlers.get(type);

        if (handler == null) {
            throw new IllegalStateException("No handler registered for spell type: " + type);
        }

        handler.execute(context);
    }
}

