package org.example.handler.fireball;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.context.SpellContext;
import org.example.context.SpellHitContext;
import org.example.handler.VfxHitHandler;
import org.example.registry.ProjectileSpellDefinition;
import org.example.registry.SpellRegistry;

public class FireballVfxHitHandler implements VfxHitHandler {

    private JavaPlugin plugin;
    private SpellRegistry spellRegistry;

    public FireballVfxHitHandler(JavaPlugin plugin, SpellRegistry registry) {
        this.plugin = plugin;
        this.spellRegistry = registry;
    }

    @Override
    public void render(SpellContext context, SpellHitContext spellHitContext) {
        spawnExplosion(spellHitContext.getHitLocation(), context);
    }

    public void spawnExplosion(Location at, SpellContext spellContext) {

        World world = at.getWorld();

        ProjectileSpellDefinition def = (ProjectileSpellDefinition) spellRegistry.getSpellDefinition(spellContext.getSpell().getSpellType());

        // Multi-layered explosion effect
        // Layer 1: Initial flash
        world.spawnParticle(Particle.EXPLOSION, at, 1, 0, 0, 0, 0);
        world.spawnParticle(Particle.FLAME, at, 50, 2, 2, 2, 0.1);

        // Layer 2: Expanding ring of fire
        new BukkitRunnable() {
            private int ticks = 0;
            private final int maxTicks = 20;

            @Override
            public void run() {
                double radius = (ticks / (double) maxTicks) * def.getProjectileExplosionRadius();
                int particles = (int) (radius * 8);

                for (int i = 0; i < particles; i++) {
                    double angle = (i * Math.PI * 2) / particles;
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;

                    Location firePoint = at.clone().add(x, 0, z);
                    world.spawnParticle(Particle.FLAME, firePoint, 3, 0.1, 0.1, 0.1, 0);
                    world.spawnParticle(Particle.SMOKE, firePoint, 1, 0.2, 0.2, 0.2, 0);
                }

                ticks++;
                if (ticks >= maxTicks) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);

        world.spawnParticle(Particle.EXPLOSION, at, 1);
    }
}
