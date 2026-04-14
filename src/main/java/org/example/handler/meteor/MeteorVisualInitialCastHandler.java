//package org.example.handler.meteor;
//
//import org.bukkit.Location;
//import org.bukkit.Particle;
//import org.bukkit.World;
//import org.bukkit.entity.Player;
//import org.bukkit.util.RayTraceResult;
//import org.bukkit.util.Vector;
//import org.example.handler.VisualPipelineHandler;
//import org.example.spell.meteor.MeteorSpell;
//import org.example.spell.Spell;
//
//import java.util.Map;
//
//public class MeteorVisualInitialCastHandler implements VisualPipelineHandler {
//    @Override
//    public void execute(VisualSpellContext context) {
//
//        Map<String, Particle> particles = Map.of(
//                "FLAME", Particle.FLAME,
//                "SMOKE", Particle.SMOKE
//        );
//
//        context.addAttribute(VisualSpellContextAttributes.WORLD, world);
//        context.addAttribute(VisualSpellContextAttributes.TARGET, target);
//        context.addAttribute(VisualSpellContextAttributes.PARTICLES, particles);
//        context.addAttribute(VisualSpellContextAttributes.START_HEIGHT, 50.0);
//        context.addAttribute(VisualSpellContextAttributes.FALL_SPEED, meteorSpell.getFallSpeed());
//        context.addAttribute(VisualSpellContextAttributes.EXPLOSION_RADIUS, meteorSpell.getExplosionRadius());
//        context.addAttribute(VisualSpellContextAttributes.EXPLOSION_POWER, meteorSpell.getExplosionPower());
//        context.addAttribute(VisualSpellContextAttributes.GROUND_FLAME_COUNT, 20);
//        context.addAttribute(VisualSpellContextAttributes.METEOR_BASE_DAMAGE, meteorSpell.getMeteorBaseDamage());
//    }
//
//    @Override
//    public boolean supports(Spell spell) {
//        return spell instanceof MeteorSpell;
//    }
//}
