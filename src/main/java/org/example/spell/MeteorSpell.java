package org.example.spell;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.example.context.SpellContext;
import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.handler.PipelineExecutor;

public class MeteorSpell implements AreaSpell {

    public MeteorSpell(JavaPlugin plugin, PipelineExecutor executor) {
        this.plugin = plugin;
        this.executor = executor;
    }

    private final JavaPlugin plugin;
    private final PipelineExecutor executor;
    private static final double START_HEIGHT = 50.0;      // ile bloków nad miejscem kliknięcia wyjdzie meteor
    private static final double FALL_SPEED = 2.5;         // bloki na tick
    private static final double EXPLOSION_RADIUS = 5.0;   // zasięg obrażeń meteoru
    private static final float EXPLOSION_POWER = 0f;      // siła wybuchu (0 = brak zniszczeń)
    private static final int GROUND_FLAME_COUNT = 20;     // ilość płomieni pozostawionych po wybuchu

    @Override
    public void cast(Player player) {
        World world = player.getWorld();
        Location eye = player.getEyeLocation();
        Vector dir = eye.getDirection();
        double maxDistance = 20.0;

        // Ray-trace bloków w kierunku patrzenia
        RayTraceResult trace = world.rayTraceBlocks(eye, dir, maxDistance);
        Location target;
        if (trace != null && trace.getHitPosition() != null) {
            // trafiono w blok, użyj miejsca uderzenia
            Vector hit = trace.getHitPosition();
            target = new Location(world, hit.getX(), hit.getY(), hit.getZ());
        } else {
            // jeśli nie trafiono, ustaw meteoru punkt nad pierwszym blokiem w zasięgu
            Location endPoint = eye.add(dir.multiply(maxDistance));
            int blockX = endPoint.getBlockX();
            int blockZ = endPoint.getBlockZ();
            int highestY = world.getHighestBlockYAt(blockX, blockZ);
            // meteoru startuje nad powierzchnią (blok + 1)
            target = new Location(world, blockX + 0.5, highestY + 1, blockZ + 0.5);
        }

        // Wywołanie logiki meteoru z ustawionymi typami cząsteczek
        launchMeteor(world, target, Particle.FLAME, Particle.SMOKE, player);
    }

    private void launchMeteor(World world, Location target, Particle trailParticle1, Particle trailParticle2, Player player) {
        new BukkitRunnable() {
            double y = target.getY() + START_HEIGHT;

            @Override
            public void run() {
                if (y <= target.getY() + 1) {
                    explodeMeteor(world, target, player);
                    cancel();
                    return;
                }
                spawnTrailParticles(world, target, y, trailParticle1, trailParticle2);
                y -= FALL_SPEED;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void explodeMeteor(World world, Location target, Player player) {
        // wybuch bez zniszczeń
        world.createExplosion(target, EXPLOSION_POWER, false, false);

        SpellCaster spellCaster = new SpellCaster();
        SpellTarget spellTarget = new SpellTarget();
        // obrażenia w promieniu
        for (Entity e : world.getNearbyEntities(target,
                EXPLOSION_RADIUS, EXPLOSION_RADIUS, EXPLOSION_RADIUS)) {
            if (e instanceof LivingEntity) {

                spellTarget.setTarget((LivingEntity) e);
                SpellContext spellContext = new SpellContext(
                        this, spellTarget, spellCaster, target, 10.0, 1.0);

                this.executor.handleCasting(spellContext);
            }
        }
        world.spawnParticle(Particle.EXPLOSION, target, 1);
    }

    private void spawnTrailParticles(World world, Location target, double currentY,
                                     Particle p1, Particle p2) {
        double step = 0.1;
        double startY = currentY;
        for (double delta = 0; delta <= FALL_SPEED; delta += step) {
            double yy = startY - delta;
            Location loc = new Location(world, target.getX(), yy, target.getZ());
            world.spawnParticle(p1, loc, 50, 0, 0, 0, 0);
            world.spawnParticle(p2, loc, 50, 0, 0, 0, 0);
        }
    }
}
