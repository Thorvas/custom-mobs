package org.example.listener;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.example.context.SpellContext;
import org.example.entity.SpellTarget;
import org.example.event.SpellHitEvent;

public class SpellDamageListener {

    @EventHandler
    public void handle(SpellHitEvent event, SpellContext spellContext) {

//        Location at = event.getSpellHitContext();
//        World world = at.getWorld();
//
//        for (Entity e : world.getNearbyEntities(at, explosionRadius, explosionRadius, explosionRadius)) {
//            if (e instanceof LivingEntity le) {
//                SpellTarget target = new SpellTarget();
//                target.setTarget(le);
//
//                SpellContext sc = new SpellContext(
//                        spell, target, caster,
//                        baseDamage + damageCalculator.calculateScaledDamage(
//                                ((Player) context.getCaster().getCaster()).getLevel(), 5, 15
//                        )
//                );
//                double distance = e.getLocation().distance(at);
//                double knockbackMultiplier = Math.max(0.2, 1.0 - (distance / explosionRadius));
//
//                sc.addAttribute(SpellContextAttributes.HIT_LOCATION, at);
//                sc.addAttribute(SpellContextAttributes.ACTUAL_DAMAGE, sc.getDamage());
//                sc.addAttribute(SpellContextAttributes.KNOCKBACK, knockbackMultiplier);
//                executor.handleCasting(sc);
//            }
//        }

    }
}
