package org.example.handler.meteor;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.example.context.SpellContext;
import org.example.context.SpellMoveContext;
import org.example.handler.VfxMoveHandler;

import java.util.Random;

public class MeteorVfxMoveHandler implements VfxMoveHandler {

    private final double START_HEIGHT = 50.0;
    private final double FALL_SPEED = 2.5;

    @Override
    public void render(SpellContext context, SpellMoveContext spellMoveContext) {
        spawnEnhancedTrailParticles(context, spellMoveContext);
    }

    private void spawnEnhancedTrailParticles(SpellContext context, SpellMoveContext spellMoveContext) {


//        World world, Location target, double currentY,
//        Particle p1, Particle p2, double progress

        Location target = spellMoveContext.getTarget();

        World world = spellMoveContext.getAt().getWorld();
        double currentY = spellMoveContext.getAt().getY();
        double heightAboveTarget = Math.max(0.0, currentY - target.getY());
        double progress = 1.0 - Math.min(1.0, heightAboveTarget / START_HEIGHT);

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
}
