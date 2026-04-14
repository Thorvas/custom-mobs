package org.example.handler.projectile;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.example.context.SpellContext;
import org.example.handler.CastInput;
import org.example.handler.ProjectileStrategy;

public class MeteorStrategy implements ProjectileStrategy {

    //TODO: CHANGE THIS TO BE RESOLVED FROM REGISTRY
    private final double START_HEIGHT = 50.0;
    private final double MAX_DISTANCE = 20.0;


    @Override
    public CastInput build(SpellContext ctx) {

        Player player = (Player) ctx.getCaster().getCaster();
        World world = player.getWorld();
        Location eye = player.getEyeLocation();
        Vector dir = eye.getDirection();

        Location target = resolveTargetByRaytrace(world, eye, dir, MAX_DISTANCE);

        // Spawn the meteor above the resolved target and point it downwards
        Location origin = target.clone().add(0, START_HEIGHT, 0);
        Vector fallDirection = target.clone().subtract(origin).toVector().normalize();

        return new CastInput(
                origin,
                fallDirection,
                target
        );
    }

    private Location resolveTargetByRaytrace(World world, Location eye, Vector dir, double maxDistance) {

        RayTraceResult trace = world.rayTraceBlocks(eye, dir, maxDistance);

        Location target;
        if (trace != null && trace.getHitPosition() != null) {
            // trafiono w blok, użyj miejsca uderzenia
            Vector hit = trace.getHitPosition();
            target = new Location(world, hit.getX(), hit.getY(), hit.getZ());

            return target;
        } else {
            // jeśli nie trafiono, ustaw meteoru punkt nad pierwszym blokiem w zasięgu
            Location endPoint = eye.add(dir.multiply(maxDistance));
            int blockX = endPoint.getBlockX();
            int blockZ = endPoint.getBlockZ();
            int highestY = world.getHighestBlockYAt(blockX, blockZ);
            // meteoru startuje nad powierzchnią (blok + 1)
            target = new Location(world, blockX + 0.5, highestY + 1, blockZ + 0.5);

            return target;
        }
    }

}
