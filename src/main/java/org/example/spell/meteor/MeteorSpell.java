package org.example.spell.meteor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.example.context.DamageType;
import org.example.spell.AreaSpell;
import org.example.spell.InterruptibleSpell;
import org.example.spell.UpgradeableSpell;

public class MeteorSpell implements AreaSpell, InterruptibleSpell, UpgradeableSpell {

    private int experience = 5;
    private Component title;
    private final String id = "METEOR";
    private double START_HEIGHT = 50.0;      // ile bloków nad miejscem kliknięcia wyjdzie meteor
    private double FALL_SPEED = 2.5;         // bloki na tick
    private double EXPLOSION_RADIUS = 5.0;   // zasięg obrażeń meteoru
    private float EXPLOSION_POWER = 0f;      // siła wybuchu (0 = brak zniszczeń)
    private double METEOR_BASE_DAMAGE = 10.0; // obrażenia meteoru
    private int GROUND_FLAME_COUNT = 20;     // ilość płomieni pozostawionych po wybuchu
    private double maxDistance = 20.0;

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return "Meteoryt";
    }

    @Override
    public Long getCooldown() {
        return 10_000L;
    }

    public void setTitle(Component title) {
        this.title = title;
    }

    public double getSTART_HEIGHT() {
        return START_HEIGHT;
    }

    public void setSTART_HEIGHT(double START_HEIGHT) {
        this.START_HEIGHT = START_HEIGHT;
    }

    public double getFALL_SPEED() {
        return FALL_SPEED;
    }

    public void setFALL_SPEED(double FALL_SPEED) {
        this.FALL_SPEED = FALL_SPEED;
    }

    public double getEXPLOSION_RADIUS() {
        return EXPLOSION_RADIUS;
    }

    public void setEXPLOSION_RADIUS(double EXPLOSION_RADIUS) {
        this.EXPLOSION_RADIUS = EXPLOSION_RADIUS;
    }

    public float getEXPLOSION_POWER() {
        return EXPLOSION_POWER;
    }

    public void setEXPLOSION_POWER(float EXPLOSION_POWER) {
        this.EXPLOSION_POWER = EXPLOSION_POWER;
    }

    public double getMETEOR_BASE_DAMAGE() {
        return METEOR_BASE_DAMAGE;
    }

    public void setMETEOR_BASE_DAMAGE(double METEOR_BASE_DAMAGE) {
        this.METEOR_BASE_DAMAGE = METEOR_BASE_DAMAGE;
    }

    public int getGROUND_FLAME_COUNT() {
        return GROUND_FLAME_COUNT;
    }

    public void setGROUND_FLAME_COUNT(int GROUND_FLAME_COUNT) {
        this.GROUND_FLAME_COUNT = GROUND_FLAME_COUNT;
    }

    public void setCooldown(Long cooldown) {
    }

    @Override
    public DamageType getType() {
        return DamageType.FIRE;
    }

    @Override
    public Component getTitle() {
        return Component.text("================\n", NamedTextColor.DARK_RED, TextDecoration.BOLD)
                .append(Component.text("⚡ Meteoryt ⚡\n", NamedTextColor.DARK_RED, TextDecoration.BOLD))
                .append(Component.text("================\n", NamedTextColor.DARK_RED, TextDecoration.BOLD));
    }

    @Override
    public Component getContent() {
        return Component.text("Na wskazany obszar zostaje \n", NamedTextColor.DARK_GRAY)
                .append(Component.text("przywołany płonący meteoryt.\n", NamedTextColor.DARK_GRAY));
    }

    @Override
    public int getExperience() {
        return this.experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }


    //    public MeteorSpell(JavaPlugin plugin, PipelineExecutor executor) {
//        this.plugin = plugin;
//        this.executor = executor;
//    }
//
//    private final DamageCalculator damageCalculator = new DamageCalculator();
//    private final JavaPlugin plugin;
//    private final PipelineExecutor executor;
//    private static final double START_HEIGHT = 50.0;      // ile bloków nad miejscem kliknięcia wyjdzie meteor
//    private static final double FALL_SPEED = 2.5;         // bloki na tick
//    private static final double EXPLOSION_RADIUS = 5.0;   // zasięg obrażeń meteoru
//    private static final float EXPLOSION_POWER = 0f;      // siła wybuchu (0 = brak zniszczeń)
//    private static final int GROUND_FLAME_COUNT = 20;     // ilość płomieni pozostawionych po wybuchu
//    private static final double METEOR_BASE_DAMAGE = 2.0;
//
//    @Override
    public void cast(Player player) {
//        World world = player.getWorld();
//        Location eye = player.getEyeLocation();
//        Vector dir = eye.getDirection();
//        double maxDistance = 20.0;
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
//        // Wywołanie logiki meteoru z ustawionymi typami cząsteczek
//        launchMeteor(world, target, Particle.FLAME, Particle.SMOKE, player);
    }
//
//    private void launchMeteor(World world, Location target, Particle trailParticle1, Particle trailParticle2, Player player) {
//        new BukkitRunnable() {
//            double y = target.getY() + START_HEIGHT;
//
//            @Override
//            public void run() {
//                if (y <= target.getY() + 1) {
//                    explodeMeteor(world, target, player);
//                    cancel();
//                    return;
//                }
//                spawnTrailParticles(world, target, y, trailParticle1, trailParticle2);
//                y -= FALL_SPEED;
//            }
//        }.runTaskTimer(plugin, 0L, 1L);
//    }
//
//    private void explodeMeteor(World world, Location target, Player player) {
//        // wybuch bez zniszczeń
//        world.createExplosion(target, EXPLOSION_POWER, false, false);
//
//        SpellCaster spellCaster = new SpellCaster();
//        SpellTarget spellTarget = new SpellTarget();
//        // obrażenia w promieniu
//        for (Entity e : world.getNearbyEntities(target,
//                EXPLOSION_RADIUS, EXPLOSION_RADIUS, EXPLOSION_RADIUS)) {
//            if (e instanceof LivingEntity) {
//
//                spellTarget.setTarget((LivingEntity) e);
//                SpellContext spellContext = new SpellContext(
//                        this, spellTarget, spellCaster, METEOR_BASE_DAMAGE + damageCalculator.calculateScaledDamage(player.getLevel(), 10, 20));
//
//                spellContext.addAttribute(SpellContextAttributes.HIT_LOCATION, target);
//                spellContext.addAttribute(SpellContextAttributes.ACTUAL_DAMAGE, spellContext.getDamage());
//                spellContext.addAttribute(SpellContextAttributes.KNOCKBACK, 1.0);
//
//                this.executor.handleCasting(spellContext);
//            }
//        }
//        world.spawnParticle(Particle.EXPLOSION, target, 1);
//    }
//
//    private void spawnTrailParticles(World world, Location target, double currentY,
//                                     Particle p1, Particle p2) {
//        double step = 0.1;
//        double startY = currentY;
//        for (double delta = 0; delta <= FALL_SPEED; delta += step) {
//            double yy = startY - delta;
//            Location loc = new Location(world, target.getX(), yy, target.getZ());
//            world.spawnParticle(p1, loc, 50, 0, 0, 0, 0);
//            world.spawnParticle(p2, loc, 50, 0, 0, 0, 0);
//        }
//    }
}
