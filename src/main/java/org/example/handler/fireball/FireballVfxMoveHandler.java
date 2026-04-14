package org.example.handler.fireball;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.example.context.SpellContext;
import org.example.context.SpellMoveContext;
import org.example.handler.VfxMoveHandler;

import java.util.Random;

public class FireballVfxMoveHandler implements VfxMoveHandler {

    @Override
    public void render(SpellContext context, SpellMoveContext spellMoveContext) {
        spawnTrail(spellMoveContext.getAt(), spellMoveContext.getNextLocation());
    }

    private void spawnTrail(Location start, Location end) {
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

    private Vector getPerpendicular(Vector vector) {
        Vector perpendicular;
        if (Math.abs(vector.getX()) < 0.9) {
            perpendicular = new Vector(1, 0, 0);
        } else {
            perpendicular = new Vector(0, 1, 0);
        }
        return vector.crossProduct(perpendicular).normalize();
    }

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
        world.spawnParticle(Particle.FLAME, center, (int) (5 * pulseIntensity), 0, 0, 0, 0);
        world.spawnParticle(Particle.LAVA, center, (int) (3 * pulseIntensity), 0, 0, 0, 0);

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
}
