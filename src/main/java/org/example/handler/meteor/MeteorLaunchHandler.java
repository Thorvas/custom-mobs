//package org.example.handler.meteor;
//
//import org.bukkit.Location;
//import org.bukkit.Particle;
//import org.bukkit.Sound;
//import org.bukkit.World;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.LivingEntity;
//import org.bukkit.entity.Player;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.util.RayTraceResult;
//import org.bukkit.util.Vector;
//import org.example.context.SpellContext;
//import org.example.entity.SpellCaster;
//import org.example.entity.SpellTarget;
//import org.example.handler.SpellHandler;
//import org.example.spell.meteor.MeteorSpell;
//import org.example.spell.Spell;
//
//import java.util.Map;
//import java.util.Random;
//
//public class MeteorLaunchHandler implements SpellHandler {
//
//    private Spell spell;
//
//    @Override
//    public void execute(SpellContext context) {
//
//
//        Player player = (Player) context.getCaster().getCaster();
//
//        World world = player.getWorld();
//        Location eye = player.getEyeLocation();
//        Vector dir = eye.getDirection();
//
//        MeteorSpell meteorSpell = (MeteorSpell) context.getSpell();
//        double maxDistance = meteorSpell.getMaxDistance();
//
//        // Ray-trace bloków w kierunku patrzenia
//        RayTraceResult trace = world.rayTraceBlocks(eye, dir, maxDistance);
//        Location target;
//        if (trace != null && trace.getHitPosition() != null) {
//            // trafiono w blok, użyj miejsca uderzenia
//            Vector hit = trace.getHitPosition();
//            target = new Location(world, hit.getX(), hit.getY(), hit.getZ());
//        } else {
//            // jeśli nie trafiono, ustaw meteoru punkt nad pierwszym blokiem w zasięgu
//            Location endPoint = eye.add(dir.multiply(maxDistance));
//            int blockX = endPoint.getBlockX();
//            int blockZ = endPoint.getBlockZ();
//            int highestY = world.getHighestBlockYAt(blockX, blockZ);
//            // meteoru startuje nad powierzchnią (blok + 1)
//            target = new Location(world, blockX + 0.5, highestY + 1, blockZ + 0.5);
//        }
//
//        Location target = context.getAttr(VisualSpellContextAttributes.TARGET);
//        World world = context.getAttr(VisualSpellContextAttributes.WORLD);
//        Player player = (Player) context.getCaster().getCaster();
//        Particle trailParticle1 = ((Map<String, Particle>) context.getAttr(VisualSpellContextAttributes.PARTICLES)).get("FLAME");
//        Particle trailParticle2 = ((Map<String, Particle>) context.getAttr(VisualSpellContextAttributes.PARTICLES)).get("SMOKE");
//        START_HEIGHT = context.getAttr(VisualSpellContextAttributes.START_HEIGHT);
//        FALL_SPEED = context.getAttr(VisualSpellContextAttributes.FALL_SPEED);
//        EXPLOSION_RADIUS = context.getAttr(VisualSpellContextAttributes.EXPLOSION_RADIUS);
//        EXPLOSION_POWER = context.getAttr(VisualSpellContextAttributes.EXPLOSION_POWER);
//        METEOR_BASE_DAMAGE = context.getAttr(VisualSpellContextAttributes.METEOR_BASE_DAMAGE);
//        spell = context.getSpell();
//        damageCalculator = context.getAttr(VisualSpellContextAttributes.DAMAGE_CALCULATOR);
//        executor = context.getAttr(VisualSpellContextAttributes.EXECUTOR);
//        plugin = context.getAttr(VisualSpellContextAttributes.PLUGIN);
//
//        new BukkitRunnable() {
//            double y = target.getY() + START_HEIGHT;
//            double totalFallDistance = START_HEIGHT;
//            Random random = new Random();
//
//            @Override
//            public void run() {
//                if (y <= target.getY() + 1) {
//                    explodeMeteor(world, target, player);
//                    cancel();
//                    return;
//                }
//
//                // Calculate progress (0.0 at start, 1.0 at impact)
//                double progress = 1.0 - ((y - target.getY()) / totalFallDistance);
//
//                // Enhanced trail particles with multiple effects
//                spawnEnhancedTrailParticles(world, target, y, trailParticle1, trailParticle2, progress);
//
//                y -= FALL_SPEED;
//            }
//        }.runTaskTimer(plugin, 0L, 1L);
//    }
//
//    private void spawnEnhancedTrailParticles(World world, Location target, double currentY,
//                                             Particle p1, Particle p2, double progress) {
//        // More performance-friendly with larger steps
//        double step = 0.2; // Larger steps, fewer particles
//        double startY = currentY;
//
//        // Condensed intensity - less spread, more focused
//        double intensity = 0.8 + (progress * 0.7); // 0.8 to 1.5 intensity (more conservative)
//        Random random = new Random();
//
//        for (double delta = 0; delta <= FALL_SPEED; delta += step) {
//            double yy = startY - delta;
//            Location coreLocation = new Location(world, target.getX(), yy, target.getZ());
//
//            // CONDENSED CORE: Tighter, more focused meteor
//            int coreParticles = (int)(8 * intensity); // Reduced from 15
//            world.spawnParticle(Particle.LAVA, coreLocation, coreParticles, 0.05, 0.05, 0.05, 0); // Tighter spread
//            world.spawnParticle(Particle.FLAME, coreLocation, coreParticles, 0.1, 0.1, 0.1, 0); // Tighter spread
//
//            // SIMPLIFIED TRAIL: Only core trail + minimal smoke
//            // Reduced smoke trail - much tighter
//            int smokeParticles = (int)(3 * intensity); // Reduced from 10
//            for (int i = 0; i < smokeParticles; i++) {
//                double offsetX = (random.nextGaussian() * 0.2); // Much tighter spread
//                double offsetZ = (random.nextGaussian() * 0.2); // Much tighter spread
//                double offsetY = random.nextDouble() * 1.5; // Reduced upward spread
//
//                Location smokeLocation = new Location(world,
//                        target.getX() + offsetX, yy + offsetY, target.getZ() + offsetZ);
//
//                world.spawnParticle(Particle.SMOKE, smokeLocation, 1, 0.1, 0.1, 0.1, 0);
//            }
//
//            // OPTIONAL: Small sparks only when close to impact
//            if (progress > 0.6) {
//                for (int i = 0; i < 2; i++) { // Only 2 sparks
//                    double offsetX = (random.nextGaussian() * 0.3);
//                    double offsetZ = (random.nextGaussian() * 0.3);
//
//                    Location sparkLocation = new Location(world,
//                            target.getX() + offsetX, yy, target.getZ() + offsetZ);
//
//                    world.spawnParticle(Particle.CRIT, sparkLocation, 1, 0, 0, 0, 0);
//                }
//            }
//        }
//
//        // GROUND WARNING: Only when very close, much simpler
//        if (progress > 0.85) {
//            double warningRadius = 2.0; // Smaller radius
//            int warningParticles = 8; // Fewer particles
//
//            for (int i = 0; i < warningParticles; i++) {
//                double angle = (i * Math.PI * 2) / warningParticles;
//                double x = target.getX() + Math.cos(angle) * warningRadius;
//                double z = target.getZ() + Math.sin(angle) * warningRadius;
//
//                Location warningLocation = new Location(world, x, target.getY(), z);
//                world.spawnParticle(Particle.FLAME, warningLocation, 2, 0.1, 0.1, 0.1, 0); // Reduced particles
//            }
//        }
//    }
//
//    // Removed the complex spawnExplosionRing method - no longer needed
//
//    @Override
//    public boolean supports(Spell spell) {
//        return spell instanceof MeteorSpell;
//    }
//}