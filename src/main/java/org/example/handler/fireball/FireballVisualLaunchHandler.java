package org.example.handler.fireball;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.example.calculator.DamageCalculator;
import org.example.context.VisualSpellContext;
import org.example.context.VisualSpellContextAttributes;
import org.example.context.SpellContext;
import org.example.context.SpellContextAttributes;
import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.handler.PipelineExecutor;
import org.example.handler.VisualPipelineHandler;
import org.example.spell.Spell;
import org.example.spell.frostbolt.FireballSpell;

import java.util.Random;
import java.util.UUID;

public class FireballVisualLaunchHandler implements VisualPipelineHandler {

    @Override
    public void execute(VisualSpellContext context) {
        World world       = context.getAttr(VisualSpellContextAttributes.WORLD);
        Location origin   = context.getAttr(VisualSpellContextAttributes.ORIGIN);
        Vector direction  = context.getAttr(VisualSpellContextAttributes.DIRECTION);
        double speed      = context.getAttr(VisualSpellContextAttributes.SPEED);
        double maxRange   = context.getAttr(VisualSpellContextAttributes.MAX_RANGE);
        double explosionRadius = context.getAttr(VisualSpellContextAttributes.EXPLOSION_RADIUS);
        float explosionPower   = context.getAttr(VisualSpellContextAttributes.EXPLOSION_POWER);
        double baseDamage      = context.getAttr(VisualSpellContextAttributes.BASE_DAMAGE);
        Particle trailParticle = context.getAttr(VisualSpellContextAttributes.PARTICLE_TRAIL);
        Particle hitParticle   = context.getAttr(VisualSpellContextAttributes.PARTICLE_HIT);
        Plugin plugin          = context.getAttr(VisualSpellContextAttributes.PLUGIN);
        Spell spell            = context.getSpell();
        SpellCaster caster      = new SpellCaster();
        DamageCalculator damageCalculator   = context.getAttr(VisualSpellContextAttributes.DAMAGE_CALCULATOR);
        PipelineExecutor executor           = context.getAttr(VisualSpellContextAttributes.EXECUTOR);
        UUID casterId          = context.getCaster().getCaster().getUniqueId();

        new BukkitRunnable() {
            private Location loc = origin.clone();
            private double traveled = 0.0;

            // Replace the collision detection section in your run() method with this:

            @Override
            public void run() {
                // Calculate movement step
                Vector move = direction.clone().multiply(speed);
                Location next = loc.clone().add(move);
                traveled += move.length();

                // Spawn trail particles
                spawnTrail(loc, next, trailParticle);

                // IMPROVED COLLISION DETECTION
                // 1. Check for entity collisions along the path (not just at the end)
                RayTraceResult hitEntity = world.rayTraceEntities(
                        loc,                    // start position
                        direction,              // direction
                        speed,                  // max distance to check
                        1.0,                    // entity bounding box inflation (makes detection more forgiving)
                        entity -> (entity instanceof LivingEntity)
                                && !entity.getUniqueId().equals(casterId)
                );

                if (hitEntity != null) {
                    Location hitLoc = hitEntity.getHitPosition().toLocation(world);
                    explode(hitLoc);
                    cancel();
                    return;
                }

                // 2. Check for block collisions along the path
                RayTraceResult hitBlock = world.rayTraceBlocks(loc, direction, speed);
                if (hitBlock != null && hitBlock.getHitBlock().getType() != Material.SHORT_DRY_GRASS
                        && hitBlock.getHitBlock().getType() != Material.SHORT_GRASS
                        && hitBlock.getHitBlock().getType() != Material.TALL_GRASS
                        && hitBlock.getHitBlock().getType() != Material.TALL_DRY_GRASS) {
                    explode(hitBlock.getHitPosition().toLocation(world));
                    cancel();
                    return;
                }

                // 3. Additional proximity check with larger radius for safety
                // Check both current position and next position
                for (Location checkLoc : new Location[]{loc, next}) {
                    for (Entity e : world.getNearbyEntities(checkLoc, 1.0, 1.0, 1.0)) {
                        if (e instanceof LivingEntity && !e.getUniqueId().equals(casterId)) {
                            explode(checkLoc);
                            cancel();
                            return;
                        }
                    }
                }

                // Update position
                loc = next;

                // Check max range
                if (traveled >= maxRange) {
                    explode(loc);
                    cancel();
                }
            }

            // Enhanced spawnTrail function with multiple visual effects

            private void spawnTrail(Location start, Location end, Particle particle) {
                Vector direction = end.toVector().subtract(start.toVector());
                double length = direction.length();
                Vector step = direction.clone().normalize().multiply(0.05); // smaller steps for smoother effect

                Location point = start.clone();
                double traveled = 0;

                // Get perpendicular vectors for spiral/helix effects
                Vector perpendicular1 = getPerpendicular(direction.clone().normalize());
                Vector perpendicular2 = direction.clone().normalize().crossProduct(perpendicular1);

                while (traveled < length) {
                    // OPTION 1: Spiral Trail
                    spawnSpiralTrail(point, perpendicular1, perpendicular2, traveled);

                    // OPTION 2: Pulsing Core with Outer Ring
                    spawnPulsingTrail(point, traveled);

                    // OPTION 3: Flame-like Effect with Random Spread
                    spawnFlameTrail(point, direction.clone().normalize());

                    point.add(step);
                    traveled += 0.05;
                }
            }

            // OPTION 1: Spiral/Helix Trail
            private void spawnSpiralTrail(Location center, Vector perp1, Vector perp2, double progress) {
                World world = center.getWorld();
                double radius = 0.3;
                int helixCount = 2; // number of spirals

                for (int i = 0; i < helixCount; i++) {
                    double angle = (progress * 4) + (i * Math.PI / helixCount); // 4 controls spiral tightness

                    Vector offset = perp1.clone().multiply(Math.cos(angle) * radius)
                            .add(perp2.clone().multiply(Math.sin(angle) * radius));

                    Location spiralPoint = center.clone().add(offset);

                    // Main fire particles
                    world.spawnParticle(Particle.FLAME, spiralPoint, 1, 0, 0, 0, 0);
                    world.spawnParticle(Particle.LAVA, spiralPoint, 1, 0, 0, 0, 0);

                    // Smoke trail
                    world.spawnParticle(Particle.SMOKE, spiralPoint, 1, 0.1, 0.1, 0.1, 0);
                }
            }

            // OPTION 2: Pulsing Core with Outer Ring
            private void spawnPulsingTrail(Location center, double progress) {
                World world = center.getWorld();

                // Pulsing intensity based on progress
                double pulseIntensity = 0.5 + 0.5 * Math.sin(progress * 8); // 8 controls pulse frequency

                // Core - bright and intense
                world.spawnParticle(Particle.FLAME, center, (int)(5 * pulseIntensity), 0, 0, 0, 0);
                world.spawnParticle(Particle.LAVA, center, (int)(3 * pulseIntensity), 0, 0, 0, 0);

                // Outer ring - dispersed
                double ringRadius = 0.4 * pulseIntensity;
                for (int i = 0; i < 8; i++) {
                    double angle = (i * Math.PI * 2) / 8;
                    double x = Math.cos(angle) * ringRadius;
                    double z = Math.sin(angle) * ringRadius;

                    Location ringPoint = center.clone().add(x, 0, z);
                    world.spawnParticle(Particle.FLAME, ringPoint, 1, 0.1, 0.1, 0.1, 0);
                }

                // Trailing smoke
                world.spawnParticle(Particle.SMOKE, center, 2, 0.2, 0.2, 0.2, 0);
            }

            // OPTION 3: Flame-like Effect with Random Spread
            private void spawnFlameTrail(Location center, Vector direction) {
                World world = center.getWorld();
                Random random = new Random();

                // Main fireball core
                world.spawnParticle(Particle.FLAME, center, 8, 0.1, 0.1, 0.1, 0);
                world.spawnParticle(Particle.LAVA, center, 3, 0.05, 0.05, 0.05, 0);

                // Random flame particles around the core
                for (int i = 0; i < 12; i++) {
                    double offsetX = (random.nextDouble() - 0.5) * 0.6;
                    double offsetY = (random.nextDouble() - 0.5) * 0.6;
                    double offsetZ = (random.nextDouble() - 0.5) * 0.6;

                    Location flamePoint = center.clone().add(offsetX, offsetY, offsetZ);
                    world.spawnParticle(Particle.FLAME, flamePoint, 1, 0, 0, 0, 0);

                    // Add some backward drift to simulate air resistance
                    Vector backDrift = direction.clone().multiply(-0.2);
                    Location driftPoint = flamePoint.clone().add(backDrift);
                    world.spawnParticle(Particle.SMOKE, driftPoint, 1, 0.1, 0.1, 0.1, 0);
                }

                // Sparks flying off
                for (int i = 0; i < 3; i++) {
                    double sparkX = (random.nextDouble() - 0.5) * 0.8;
                    double sparkY = (random.nextDouble() - 0.5) * 0.8;
                    double sparkZ = (random.nextDouble() - 0.5) * 0.8;

                    Location sparkPoint = center.clone().add(sparkX, sparkY, sparkZ);
                    world.spawnParticle(Particle.CRIT, sparkPoint, 1, 0, 0, 0, 0);
                }
            }

            // OPTION 4: Advanced Layered Effect (Combine multiple effects)
            private void spawnAdvancedTrail(Location start, Location end, double progress) {
                World world = start.getWorld();
                Vector direction = end.toVector().subtract(start.toVector()).normalize();

                // Layer 1: Core fireball
                world.spawnParticle(Particle.FLAME, start, 10, 0.1, 0.1, 0.1, 0);
                world.spawnParticle(Particle.LAVA, start, 5, 0.05, 0.05, 0.05, 0);

                // Layer 2: Rotating ring
                double ringRadius = 0.3;
                int ringParticles = 6;
                for (int i = 0; i < ringParticles; i++) {
                    double angle = (progress * 6) + (i * Math.PI * 2 / ringParticles);

                    Vector perpendicular = getPerpendicular(direction);
                    Vector offset = perpendicular.clone().multiply(Math.cos(angle) * ringRadius);
                    offset.add(direction.clone().crossProduct(perpendicular).multiply(Math.sin(angle) * ringRadius));

                    Location ringPoint = start.clone().add(offset);
                    world.spawnParticle(Particle.FLAME, ringPoint, 2, 0, 0, 0, 0);
                }

                // Layer 3: Trailing embers
                for (int i = 0; i < 8; i++) {
                    Vector trailOffset = direction.clone().multiply(-(i * 0.1));
                    Location trailPoint = start.clone().add(trailOffset);

                    // Add some random spread to trailing particles
                    Random random = new Random();
                    double spreadX = (random.nextDouble() - 0.5) * 0.4;
                    double spreadY = (random.nextDouble() - 0.5) * 0.4;
                    double spreadZ = (random.nextDouble() - 0.5) * 0.4;

                    trailPoint.add(spreadX, spreadY, spreadZ);
                    world.spawnParticle(Particle.SMOKE, trailPoint, 1, 0.1, 0.1, 0.1, 0);
                }

                // Layer 4: Heat distortion effect
                world.spawnParticle(Particle.ENCHANT, start, 15, 0.5, 0.5, 0.5, 0);
            }

            // Helper method to get a perpendicular vector
            private Vector getPerpendicular(Vector vector) {
                Vector perpendicular;
                if (Math.abs(vector.getX()) < 0.9) {
                    perpendicular = new Vector(1, 0, 0);
                } else {
                    perpendicular = new Vector(0, 1, 0);
                }
                return vector.crossProduct(perpendicular).normalize();
            }

            // Enhanced explosion effect
            private void explode(Location at) {
                World world = at.getWorld();

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
                        double radius = (ticks / (double) maxTicks) * explosionRadius;
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

                // Original explosion and damage code...
                world.playSound(at, Sound.ENTITY_GENERIC_EXPLODE, 5.0f, 1.0f);
                world.spawnParticle(Particle.EXPLOSION, at, 1);


                // Handle damage to entities...
                for (Entity e : world.getNearbyEntities(at, explosionRadius, explosionRadius, explosionRadius)) {
                    if (e instanceof LivingEntity le) {
                        SpellTarget target = new SpellTarget();
                        target.setTarget(le);

                        SpellContext sc = new SpellContext(
                                spell, target, caster,
                                baseDamage + damageCalculator.calculateScaledDamage(
                                        ((Player) context.getCaster().getCaster()).getLevel(), 5, 15
                                )
                        );
                        double distance = e.getLocation().distance(at);
                        double knockbackMultiplier = Math.max(0.2, 1.0 - (distance / explosionRadius));

                        sc.addAttribute(SpellContextAttributes.HIT_LOCATION, at);
                        sc.addAttribute(SpellContextAttributes.ACTUAL_DAMAGE, sc.getDamage());
                        sc.addAttribute(SpellContextAttributes.KNOCKBACK, knockbackMultiplier);
                        executor.handleCasting(sc);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof FireballSpell;
    }
}
