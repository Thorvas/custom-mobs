package org.example.handler.fireball;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.example.context.SpellContext;
import org.example.context.SpellHitContext;
import org.example.context.SpellMoveContext;
import org.example.event.SpellHitEvent;
import org.example.event.SpellMoveEvent;
import org.example.handler.CastInput;
import org.example.handler.SpellHandler;
import org.example.handler.projectile.FireballStrategy;
import org.example.handler.projectile.MeteorStrategy;
import org.example.registry.ProjectileSpellDefinition;
import org.example.registry.SpellRegistry;
import org.example.spell.Spell;

import java.util.UUID;

public class ProjectileSimulator implements SpellHandler {

    private final JavaPlugin plugin;
    private final SpellRegistry spellRegistry;
    private final MeteorStrategy meteorStrategy = new MeteorStrategy();
    private final FireballStrategy fireballStrategy = new FireballStrategy();

    public ProjectileSimulator(JavaPlugin plugin, SpellRegistry spellRegistry) {
        this.plugin = plugin;
        this.spellRegistry = spellRegistry;
    }

    @Override
    public void execute(SpellContext spellContext) {

        Player player = (Player) spellContext.getCaster().getCaster();
        Spell spell = spellContext.getSpell();
        World world = player.getWorld();
        CastInput castInput = resolveCastInput(spellContext);
        Location origin = castInput.getFrom();
        Vector direction = castInput.getDirection();
        ProjectileSpellDefinition spellDefinition = (ProjectileSpellDefinition) spellRegistry.getSpellDefinition(spell.getSpellType());
        double speed = spellDefinition.getProjectileBaseSpeed();
        double maxRange = spellDefinition.getProjectileMaxRange();
        UUID casterId = spellContext.getCaster().getCaster().getUniqueId();


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

                SpellMoveContext spellMoveContext = new SpellMoveContext();
                spellMoveContext.setDelta(move);
                spellMoveContext.setNextLocation(next);
                spellMoveContext.setAt(loc);
                spellMoveContext.setTarget(castInput.getTarget());

                Bukkit.getPluginManager().callEvent(new SpellMoveEvent(spellContext, spellMoveContext));

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
                    LivingEntity target = (LivingEntity) hitEntity.getHitEntity();
                    explode(hitLoc, target);
                    cancel();
                    return;
                }

                // 2. Check for block collisions along the path
                RayTraceResult hitBlock = world.rayTraceBlocks(loc, direction, speed);
                if (hitBlock != null && hitBlock.getHitBlock().getType() != Material.SHORT_DRY_GRASS
                        && hitBlock.getHitBlock().getType() != Material.SHORT_GRASS
                        && hitBlock.getHitBlock().getType() != Material.TALL_GRASS
                        && hitBlock.getHitBlock().getType() != Material.TALL_DRY_GRASS) {

                    explode(hitBlock.getHitPosition().toLocation(world), null);
                    cancel();
                    return;
                }

                // 3. Additional proximity check with larger radius for safety
                // Check both current position and next position
                for (Location checkLoc : new Location[]{loc, next}) {
                    for (Entity e : world.getNearbyEntities(checkLoc, 1.0, 1.0, 1.0)) {
                        if (e instanceof LivingEntity && !e.getUniqueId().equals(casterId)) {
                            explode(checkLoc, (LivingEntity) e);
                            cancel();
                            return;
                        }
                    }
                }

                // Update position
                loc = next;

                // Check max range
                if (traveled >= maxRange) {
                    explode(loc, null);
                    cancel();
                }
            }

            private void explode(Location at, LivingEntity target) {

                // Original explosion and damage code...
                world.playSound(at, Sound.ENTITY_GENERIC_EXPLODE, 5.0f, 1.0f);

                SpellHitContext spellHitContext = new SpellHitContext();
                spellHitContext.setHitLocation(at);

                Bukkit.getPluginManager().callEvent(new SpellHitEvent(spellContext, spellHitContext));
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    CastInput resolveCastInput(SpellContext spellContext) {
        return switch (spellContext.getSpell().getSpellType()) {
            case FIREBALL -> fireballStrategy.build(spellContext);
            case METEOR ->  meteorStrategy.build(spellContext);
        };
    }
}
