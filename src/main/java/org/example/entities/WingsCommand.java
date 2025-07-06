package org.example.entities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.example.Main;

import java.util.HashMap;
import java.util.UUID;

public class WingsCommand implements CommandExecutor {

    private final HashMap<UUID, BukkitRunnable> activeWings = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tylko gracz może użyć tej komendy.");
            return true;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (activeWings.containsKey(uuid)) {
            activeWings.get(uuid).cancel();
            activeWings.remove(uuid);
            player.sendMessage("§eSkrzydła wyłączone.");
            return true;
        }

        BukkitRunnable task = new BukkitRunnable() {
            private double time = 0;

            @Override
            public void run() {
                if (!player.isOnline() || player.isDead()) {
                    this.cancel();
                    activeWings.remove(uuid);
                    return;
                }

                drawWings(player, time);
                time += 0.1; // Animation speed
            }
        };

        task.runTaskTimer(Main.getPlugin(Main.class), 0L, 3L);
        activeWings.put(uuid, task);

        player.sendMessage("§aSkrzydła włączone! Użyj ponownie, by je wyłączyć.");
        return true;
    }

    private void drawWings(Player player, double time) {
        Location center = player.getLocation().clone();
        center.add(0, 1.3, 0); // Slightly lower position

        Vector direction = center.getDirection().normalize();
        Vector right = direction.crossProduct(new Vector(0, 1, 0)).normalize();
        Vector up = new Vector(0, 1, 0);

        // Wing animation - gentle flapping
        double flapOffset = Math.sin(time * 2) * 0.15;

        // Draw both wings
        drawSingleWing(center, player, right, up, direction, true, flapOffset);
        drawSingleWing(center, player, right, up, direction, false, flapOffset);
    }

    private void drawSingleWing(Location center, Player player, Vector right, Vector up, Vector direction,
                                boolean isLeft, double flapOffset) {

        int side = isLeft ? -1 : 1;

        // Wing structure with multiple segments for realistic shape
        double[][] wingShape = {
                // Main wing segments [width, height, depth]
                {0.3, 0.1, -0.1},   // Inner wing
                {0.6, 0.3, -0.2},   // Mid wing
                {0.8, 0.5, -0.3},   // Outer wing
                {1.0, 0.4, -0.4},   // Wing tip
                {1.1, 0.2, -0.5},   // Wing end
        };

        // Draw wing segments
        for (int i = 0; i < wingShape.length; i++) {
            double width = wingShape[i][0];
            double height = wingShape[i][1] + flapOffset;
            double depth = wingShape[i][2];

            // Main wing line
            Vector wingPos = right.clone().multiply(width * side)
                    .add(up.clone().multiply(height))
                    .add(direction.clone().multiply(depth));

            Location wingPoint = center.clone().add(wingPos);

            // Use different particles for variety
            Particle particle = (i % 2 == 0) ? Particle.FIREWORK : Particle.ENCHANTED_HIT;
            player.getWorld().spawnParticle(particle, wingPoint, 1, 0.02, 0.02, 0.02, 0);

            // Add wing membrane effect
            if (i > 0) {
                drawWingMembrane(center, player, wingShape[i - 1], wingShape[i], right, up, direction, side, flapOffset);
            }
        }

        // Add wing joint at the back
        Vector jointPos = right.clone().multiply(0.15 * side)
                .add(up.clone().multiply(0.05))
                .add(direction.clone().multiply(0.1));
        Location jointPoint = center.clone().add(jointPos);
        player.getWorld().spawnParticle(Particle.ENCHANTED_HIT, jointPoint, 1, 0, 0, 0, 0);
    }

    private void drawWingMembrane(Location center, Player player, double[] segment1, double[] segment2,
                                  Vector right, Vector up, Vector direction, int side, double flapOffset) {

        // Draw 2-3 membrane lines between segments
        for (int j = 1; j <= 2; j++) {
            double ratio = j / 3.0;

            double width = lerp(segment1[0], segment2[0], ratio);
            double height = lerp(segment1[1], segment2[1], ratio) + flapOffset;
            double depth = lerp(segment1[2], segment2[2], ratio);

            Vector membranePos = right.clone().multiply(width * side)
                    .add(up.clone().multiply(height))
                    .add(direction.clone().multiply(depth));

            Location membranePoint = center.clone().add(membranePos);

            // Lighter particles for membrane
            player.getWorld().spawnParticle(Particle.CLOUD, membranePoint, 1, 0.01, 0.01, 0.01, 0);
        }
    }

    // Linear interpolation helper
    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }
}