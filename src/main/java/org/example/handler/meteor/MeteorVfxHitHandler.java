package org.example.handler.meteor;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.context.SpellContext;
import org.example.context.SpellHitContext;
import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.handler.VfxHitHandler;
import org.example.registry.MeteorProjectileSpellDefinition;
import org.example.registry.SpellRegistry;

import java.util.Random;

public class MeteorVfxHitHandler implements VfxHitHandler {

    private final SpellRegistry spellRegistry;
    private final Plugin plugin;

    public MeteorVfxHitHandler(SpellRegistry spellRegistry, Plugin plugin) {
        this.spellRegistry = spellRegistry;
        this.plugin = plugin;
    }

    @Override
    public void render(SpellContext context, SpellHitContext spellHitContext) {
        explodeMeteor(context, spellHitContext);
    }

    private void explodeMeteor(SpellContext spellContext, SpellHitContext spellHitContext) {
        // SIMPLIFIED EXPLOSION SEQUENCE - Performance optimized

        World world = spellHitContext.getHitLocation().getWorld();
        Location target = spellHitContext.getHitLocation();

        MeteorProjectileSpellDefinition meteorSpellDefinition = (MeteorProjectileSpellDefinition) spellRegistry.getSpellDefinition(spellContext.getSpell().getSpellType());

        double EXPLOSION_RADIUS = meteorSpellDefinition.getMeteorExplosionRadius();

        // Phase 1: Initial Impact Flash
        world.spawnParticle(Particle.EXPLOSION, target, 2, 0, 0, 0, 0); // Reduced from 3
        world.spawnParticle(Particle.FLASH, target, 1, 0, 0, 0, 0);

        // Phase 2: Single Explosion Ring - Much simpler
        new BukkitRunnable() {
            private int ticks = 0;
            private final int maxTicks = 15; // Reduced from 30

            @Override
            public void run() {
                double progress = ticks / (double) maxTicks;
                double radius = progress * EXPLOSION_RADIUS;

                // Single ring effect - much more efficient
                int particles = Math.max(6, (int) (radius * 2)); // Fewer particles

                for (int i = 0; i < particles; i++) {
                    double angle = (i * Math.PI * 2) / particles;
                    double x = target.getX() + Math.cos(angle) * radius;
                    double z = target.getZ() + Math.sin(angle) * radius;

                    Location ringLocation = new Location(world, x, target.getY(), z);
                    world.spawnParticle(Particle.FLAME, ringLocation, 2, 0.1, 0.1, 0.1, 0); // Reduced particles

                    // Add smoke only every other particle for performance
                    if (i % 2 == 0) {
                        world.spawnParticle(Particle.SMOKE, ringLocation, 1, 0.2, 0.2, 0.2, 0);
                    }
                }

                // Reduced central eruption
                world.spawnParticle(Particle.LAVA, target, 8, 0.5, 0.5, 0.5, 0.1); // Reduced from 20
                world.spawnParticle(Particle.FLAME, target, 12, 1, 1, 1, 0.05); // Reduced from 30

                ticks++;
                if (ticks >= maxTicks) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 2L); // Every 2 ticks instead of 1 for better performance

        // Phase 3: Simplified Crater Effect
        new BukkitRunnable() {
            @Override
            public void run() {
                // Much simpler crater effect
                for (int i = 0; i < 12; i++) { // Reduced from 50
                    Random random = new Random();
                    double angle = random.nextDouble() * Math.PI * 2;
                    double radius = random.nextDouble() * (EXPLOSION_RADIUS * 0.7); // Smaller radius
                    double x = target.getX() + Math.cos(angle) * radius;
                    double z = target.getZ() + Math.sin(angle) * radius;

                    Location craterLocation = new Location(world, x, target.getY(), z);
                    world.spawnParticle(Particle.SMOKE, craterLocation, 1, 0.1, 0.1, 0.1, 0);
                }
            }
        }.runTaskLater(plugin, 30L); // Longer delay, simpler effect

        // Original explosion and damage
        world.playSound(target, Sound.ENTITY_GENERIC_EXPLODE, 5.0f, 1.0f);
        world.spawnParticle(Particle.EXPLOSION, target, 1);

        SpellCaster spellCaster = new SpellCaster();
        SpellTarget spellTarget = new SpellTarget();

        // Enhanced damage with knockback based on distance
        for (Entity e : world.getNearbyEntities(target,
                EXPLOSION_RADIUS, EXPLOSION_RADIUS, EXPLOSION_RADIUS)) {
            if (e instanceof LivingEntity) {

                // Calculate distance for knockback scaling
                double distance = e.getLocation().distance(target);
                double knockbackMultiplier = Math.max(0.2, 1.0 - (distance / EXPLOSION_RADIUS));
            }
        }
    }
}
