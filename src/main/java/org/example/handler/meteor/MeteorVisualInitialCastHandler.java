package org.example.handler.meteor;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.example.context.VisualSpellContext;
import org.example.context.VisualSpellContextAttributes;
import org.example.handler.VisualPipelineHandler;
import org.example.spell.meteor.MeteorSpell;
import org.example.spell.Spell;

import java.util.Map;

public class MeteorVisualInitialCastHandler implements VisualPipelineHandler {
    @Override
    public void execute(VisualSpellContext context) {

        Player player = (Player) context.getCaster().getCaster();

        World world = player.getWorld();
        Location eye = player.getEyeLocation();
        Vector dir = eye.getDirection();

        MeteorSpell meteorSpell = (MeteorSpell) context.getSpell();
        double maxDistance = meteorSpell.getMaxDistance();

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

        Map<String, Particle> particles = Map.of(
                "FLAME", Particle.FLAME,
                "SMOKE", Particle.SMOKE
        );

        context.addAttribute(VisualSpellContextAttributes.WORLD, world);
        context.addAttribute(VisualSpellContextAttributes.TARGET, target);
        context.addAttribute(VisualSpellContextAttributes.PARTICLES, particles);
        context.addAttribute(VisualSpellContextAttributes.START_HEIGHT, 50.0);
        context.addAttribute(VisualSpellContextAttributes.FALL_SPEED, meteorSpell.getFALL_SPEED());
        context.addAttribute(VisualSpellContextAttributes.EXPLOSION_RADIUS, meteorSpell.getEXPLOSION_RADIUS());
        context.addAttribute(VisualSpellContextAttributes.EXPLOSION_POWER, meteorSpell.getEXPLOSION_POWER());
        context.addAttribute(VisualSpellContextAttributes.GROUND_FLAME_COUNT, 20);
        context.addAttribute(VisualSpellContextAttributes.METEOR_BASE_DAMAGE, meteorSpell.getMETEOR_BASE_DAMAGE());
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof MeteorSpell;
    }
}
