// src/main/java/org/example/MeteorPlugin.java
package org.example;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends JavaPlugin implements Listener {

    private ProtocolManager protocolManager;

    // parametry meteoru
    private static final double START_HEIGHT = 50.0;      // ile bloków nad miejscem kliknięcia wyjdzie meteor
    private static final double FALL_SPEED = 2.5;         // bloki na tick
    private static final double EXPLOSION_RADIUS = 5.0;   // zasięg obrażeń meteoru
    private static final float EXPLOSION_POWER = 0f;      // siła wybuchu (0 = brak zniszczeń)
    private static final int GROUND_FLAME_COUNT = 20;     // ilość płomieni pozostawionych po wybuchu

    private final Random random = new Random();

    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBookRightClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return;
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() != Material.BOOK) return;

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
        launchMeteor(world, target, Particle.FLAME, Particle.SMOKE);
    }

    /**
     * Uruchamia efekt meteoru w danym miejscu.
     * @param world Świat, w którym ma spaść meteor
     * @param target Lokalizacja celu meteoru
     * @param trailParticle1 Pierwszy typ cząsteczki śladu meteoru
     * @param trailParticle2 Drugi typ cząsteczki śladu meteoru
     */
    private void launchMeteor(World world, Location target, Particle trailParticle1, Particle trailParticle2) {
        new BukkitRunnable() {
            double y = target.getY() + START_HEIGHT;

            @Override
            public void run() {
                if (y <= target.getY() + 1) {
                    explodeMeteor(world, target);
                    cancel();
                    return;
                }
                spawnTrailParticles(world, target, y, trailParticle1, trailParticle2);
                y -= FALL_SPEED;
            }
        }.runTaskTimer(this, 0L, 1L);
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

    private void explodeMeteor(World world, Location target) {
        // wybuch bez zniszczeń
        world.createExplosion(target, EXPLOSION_POWER, false, false);
        // obrażenia w promieniu
        for (Entity e : world.getNearbyEntities(target,
                EXPLOSION_RADIUS, EXPLOSION_RADIUS, EXPLOSION_RADIUS)) {
            if (e instanceof org.bukkit.entity.LivingEntity) {
                ((org.bukkit.entity.LivingEntity) e).damage(10.0);
            }
        }
        world.spawnParticle(Particle.EXPLOSION, target, 1);
    }

    private void leaveGroundFlames(World world, Location target) {
        for (int i = 0; i < GROUND_FLAME_COUNT; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double distance = random.nextDouble() * EXPLOSION_RADIUS;
            double offsetX = Math.cos(angle) * distance;
            double offsetZ = Math.sin(angle) * distance;
            int blockX = target.getBlockX() + (int) Math.round(offsetX);
            int blockZ = target.getBlockZ() + (int) Math.round(offsetZ);
            int highestY = world.getHighestBlockYAt(blockX, blockZ);
            Location flameLoc = new Location(world, blockX, highestY + 1, blockZ);
            flameLoc.getBlock().setType(Material.FIRE);
        }
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Sprawdź czy gracz trzyma książkę
        if (item.getType() != Material.BOOK &&
                item.getType() != Material.WRITTEN_BOOK &&
                item.getType() != Material.WRITABLE_BOOK) return;

        // Anuluj domyślne działanie (np. otwieranie książki)
        event.setCancelled(true);

        // Otwórz księgę zaklęć
        openSpellbook(player);
    }

    /**
     * Otwiera księgę zaklęć dla gracza używając ProtocolLib
     */
    private void openSpellbook(Player player) {
        try {
            // Stwórz książkę z zawartością
            ItemStack book = createSpellbook();

            // Wyślij pakiet otwarcia książki
            PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.OPEN_BOOK);
            packet.getEnumModifier(EnumWrappers.Hand.class, 0).write(0, EnumWrappers.Hand.MAIN_HAND);

            // Tymczasowo zamień przedmiot w ręce gracza
            ItemStack originalItem = player.getInventory().getItemInMainHand();
            player.getInventory().setItemInMainHand(book);

            // Wyślij pakiet
            protocolManager.sendServerPacket(player, packet);

            // Przywróć oryginalny przedmiot po krótkiej chwili
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.getInventory().setItemInMainHand(originalItem);
                }
            }.runTaskLater(this, 2L);

        } catch (Exception e) {
            getLogger().severe("Błąd podczas otwierania księgi zaklęć: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tworzy księgę z zawartością zaklęć
     */
    private ItemStack createSpellbook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setTitle("§6§lKsięga Zaklęć");
        meta.setAuthor("§5Mag Arcanum");

        List<String> pages = new ArrayList<>();

        // Strona 1 - Tytuł
        pages.add("§6§l===================\n" +
                "§6§lKSIĘGA ZAKLĘĆ\n" +
                "§6§l===================\n\n" +
                "§7Starożytne zaklęcia\n" +
                "§7i tajemne moce\n" +
                "§7czekają na odkrycie.\n\n" +
                "§8Kliknij prawym przyciskiem\n" +
                "§8z książką w ręce, aby\n" +
                "§8rzucić zaklęcie.\n\n" +
                "§4§lOSTRZEŻENIE:\n" +
                "§cMoc może być niebezpieczna!");

        // Strona 2 - Meteor (aktywna strona)
        pages.add("§c§l⚡ METEOR ⚡\n" +
                "§c§l==============\n\n" +
                "§7Przywołuje płonący\n" +
                "§7meteor z niebios,\n" +
                "§7który spada na\n" +
                "§7wskazane miejsce.\n\n" +
                "§e§lSkładniki:\n" +
                "§7- Energia magiczna\n" +
                "§7- Koncentracja\n" +
                "§7- Książka w ręce\n\n" +
                "§4§lObrażenia: §c10 HP\n" +
                "§4§lZasięg: §c5 bloków");

        // Strona 3 - Leczenie
        pages.add("§a§l✚ UZDROWIENIE ✚\n" +
                "§a§l================\n\n" +
                "§7Przywraca siły życiowe\n" +
                "§7i regeneruje zdrowie\n" +
                "§7rzucającego zaklęcie.\n\n" +
                "§e§lSkładniki:\n" +
                "§7- Energia życiowa\n" +
                "§7- Czysty umysł\n" +
                "§7- Dobre intencje\n\n" +
                "§a§lLeczenie: §25 HP\n" +
                "§a§lEfekt: §2Regeneracja");

        // Strona 4 - Teleportacja
        pages.add("§d§l⟐ TELEPORTACJA ⟐\n" +
                "§d§l=================\n\n" +
                "§7Przenosi rzucającego\n" +
                "§7w wybrane miejsce\n" +
                "§7w mgnieniu oka.\n\n" +
                "§e§lSkładniki:\n" +
                "§7- Energia przestrzenna\n" +
                "§7- Precyzja\n" +
                "§7- Odwaga\n\n" +
                "§d§lZasięg: §520 bloków\n" +
                "§d§lEfekt: §5Cząsteczki");

        // Strona 5 - Mur Ognia
        pages.add("§6§l🔥 MUR OGNIA 🔥\n" +
                "§6§l===============\n\n" +
                "§7Tworzy barierę z płomieni\n" +
                "§7która chroni przed\n" +
                "§7wrogami i zadaje\n" +
                "§7obrażenia napastnikom.\n\n" +
                "§e§lSkładniki:\n" +
                "§7- Energia ognia\n" +
                "§7- Siła woli\n" +
                "§7- Kontrola\n\n" +
                "§6§lCzas: §e30 sekund\n" +
                "§6§lWysokość: §e3 bloki");

        meta.setPages(pages);
        book.setItemMeta(meta);

        return book;
    }
}
