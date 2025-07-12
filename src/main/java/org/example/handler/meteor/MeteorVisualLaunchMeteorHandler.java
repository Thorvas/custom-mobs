package org.example.handler.meteor;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.calculator.DamageCalculator;
import org.example.context.SpellContext;
import org.example.context.SpellContextAttributes;
import org.example.context.VisualSpellContext;
import org.example.context.VisualSpellContextAttributes;
import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.handler.PipelineExecutor;
import org.example.handler.VisualPipelineHandler;
import org.example.spell.meteor.MeteorSpell;
import org.example.spell.Spell;

import java.util.Map;
import java.util.Random;

public class MeteorVisualLaunchMeteorHandler implements VisualPipelineHandler {

    private double START_HEIGHT;
    private double FALL_SPEED;
    private double EXPLOSION_RADIUS;
    private float EXPLOSION_POWER;
    private double METEOR_BASE_DAMAGE;
    private Spell spell;
    private DamageCalculator damageCalculator;
    private PipelineExecutor executor;
    private Plugin plugin;

    @Override
    public void execute(VisualSpellContext context) {

        Location target = context.getAttr(VisualSpellContextAttributes.TARGET);
        World world = context.getAttr(VisualSpellContextAttributes.WORLD);
        Player player = (Player) context.getCaster().getCaster();
        Particle trailParticle1 = ((Map<String, Particle>) context.getAttr(VisualSpellContextAttributes.PARTICLES)).get("FLAME");
        Particle trailParticle2 = ((Map<String, Particle>) context.getAttr(VisualSpellContextAttributes.PARTICLES)).get("SMOKE");
        START_HEIGHT = context.getAttr(VisualSpellContextAttributes.START_HEIGHT);
        FALL_SPEED = context.getAttr(VisualSpellContextAttributes.FALL_SPEED);
        EXPLOSION_RADIUS = context.getAttr(VisualSpellContextAttributes.EXPLOSION_RADIUS);
        EXPLOSION_POWER = context.getAttr(VisualSpellContextAttributes.EXPLOSION_POWER);
        METEOR_BASE_DAMAGE = context.getAttr(VisualSpellContextAttributes.METEOR_BASE_DAMAGE);
        spell = context.getSpell();
        damageCalculator = context.getAttr(VisualSpellContextAttributes.DAMAGE_CALCULATOR);
        executor = context.getAttr(VisualSpellContextAttributes.EXECUTOR);
        plugin = context.getAttr(VisualSpellContextAttributes.PLUGIN);

        new BukkitRunnable() {
            double y = target.getY() + START_HEIGHT;
            double totalFallDistance = START_HEIGHT;
            Random random = new Random();

            @Override
            public void run() {
                if (y <= target.getY() + 1) {
                    explodeMeteor(world, target, player);
                    cancel();
                    return;
                }

                // Calculate progress (0.0 at start, 1.0 at impact)
                double progress = 1.0 - ((y - target.getY()) / totalFallDistance);

                // Enhanced trail particles with multiple effects
                spawnEnhancedTrailParticles(world, target, y, trailParticle1, trailParticle2, progress);

                y -= FALL_SPEED;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void spawnEnhancedTrailParticles(World world, Location target, double currentY,
                                             Particle p1, Particle p2, double progress) {
        // More performance-friendly with larger steps
        double step = 0.2; // Larger steps, fewer particles
        double startY = currentY;

        // Condensed intensity - less spread, more focused
        double intensity = 0.8 + (progress * 0.7); // 0.8 to 1.5 intensity (more conservative)
        Random random = new Random();

        for (double delta = 0; delta <= FALL_SPEED; delta += step) {
            double yy = startY - delta;
            Location coreLocation = new Location(world, target.getX(), yy, target.getZ());

            // CONDENSED CORE: Tighter, more focused meteor
            int coreParticles = (int)(8 * intensity); // Reduced from 15
            world.spawnParticle(Particle.LAVA, coreLocation, coreParticles, 0.05, 0.05, 0.05, 0); // Tighter spread
            world.spawnParticle(Particle.FLAME, coreLocation, coreParticles, 0.1, 0.1, 0.1, 0); // Tighter spread

            // SIMPLIFIED TRAIL: Only core trail + minimal smoke
            // Reduced smoke trail - much tighter
            int smokeParticles = (int)(3 * intensity); // Reduced from 10
            for (int i = 0; i < smokeParticles; i++) {
                double offsetX = (random.nextGaussian() * 0.2); // Much tighter spread
                double offsetZ = (random.nextGaussian() * 0.2); // Much tighter spread
                double offsetY = random.nextDouble() * 1.5; // Reduced upward spread

                Location smokeLocation = new Location(world,
                        target.getX() + offsetX, yy + offsetY, target.getZ() + offsetZ);

                world.spawnParticle(Particle.SMOKE, smokeLocation, 1, 0.1, 0.1, 0.1, 0);
            }

            // OPTIONAL: Small sparks only when close to impact
            if (progress > 0.6) {
                for (int i = 0; i < 2; i++) { // Only 2 sparks
                    double offsetX = (random.nextGaussian() * 0.3);
                    double offsetZ = (random.nextGaussian() * 0.3);

                    Location sparkLocation = new Location(world,
                            target.getX() + offsetX, yy, target.getZ() + offsetZ);

                    world.spawnParticle(Particle.CRIT, sparkLocation, 1, 0, 0, 0, 0);
                }
            }
        }

        // GROUND WARNING: Only when very close, much simpler
        if (progress > 0.85) {
            double warningRadius = 2.0; // Smaller radius
            int warningParticles = 8; // Fewer particles

            for (int i = 0; i < warningParticles; i++) {
                double angle = (i * Math.PI * 2) / warningParticles;
                double x = target.getX() + Math.cos(angle) * warningRadius;
                double z = target.getZ() + Math.sin(angle) * warningRadius;

                Location warningLocation = new Location(world, x, target.getY(), z);
                world.spawnParticle(Particle.FLAME, warningLocation, 2, 0.1, 0.1, 0.1, 0); // Reduced particles
            }
        }
    }

    private void explodeMeteor(World world, Location target, Player player) {
        // SIMPLIFIED EXPLOSION SEQUENCE - Performance optimized

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
                int particles = Math.max(6, (int)(radius * 2)); // Fewer particles

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

                spellTarget.setTarget((LivingEntity) e);
                SpellContext spellContext = new SpellContext(
                        spell, spellTarget, spellCaster,
                        METEOR_BASE_DAMAGE + damageCalculator.calculateScaledDamage(player.getLevel(), 10, 20));

                spellContext.addAttribute(SpellContextAttributes.HIT_LOCATION, target);
                spellContext.addAttribute(SpellContextAttributes.ACTUAL_DAMAGE, spellContext.getDamage());
                spellContext.addAttribute(SpellContextAttributes.KNOCKBACK, 2.0 * knockbackMultiplier);

                this.executor.handleCasting(spellContext);
            }
        }
    }

    // Removed the complex spawnExplosionRing method - no longer needed

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof MeteorSpell;
    }
}