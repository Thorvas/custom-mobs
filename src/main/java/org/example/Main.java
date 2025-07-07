// src/main/java/org/example/MeteorPlugin.java
package org.example;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.handler.ApplyDamageHandlerCasting;
import org.example.handler.ApplyKnockbackHandlerCasting;
import org.example.handler.CheckStunnedStatusHandler;
import org.example.handler.PipelineExecutor;
import org.example.spellbook.Spellbook;

import java.util.List;
import java.util.Random;

public class Main extends JavaPlugin implements Listener {

    private final Random random = new Random();
    private Spellbook spellbook;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        this.spellbook = new Spellbook(new PipelineExecutor(
                List.of(new CheckStunnedStatusHandler()),
                List.of(new ApplyDamageHandlerCasting(),
                        new ApplyKnockbackHandlerCasting())
        ), this);
    }

    @EventHandler
    public void onBookRightClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return;
        Player player = event.getPlayer();

        if (!player.isSneaking()) return;

        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (offHand == null || offHand.getType() != Material.BOOK) return;

        spellbook.castSpell(player);
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

            player.openBook(book);

        } catch (Exception e) {
            getLogger().severe("Błąd podczas otwierania księgi zaklęć: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tworzy księgę z zawartością zaklęć
     */
    private ItemStack createSpellbook() {
        BookMeta meta = (BookMeta) new ItemStack(Material.WRITTEN_BOOK).getItemMeta();
        meta.title(Component.text("Księga Zaklęć", NamedTextColor.GOLD, TextDecoration.BOLD));
        meta.author(Component.text("Mag Arcanum", NamedTextColor.LIGHT_PURPLE));

        Component meteorPage = Component.text("================\n", NamedTextColor.DARK_RED, TextDecoration.BOLD)
                .append(Component.text("⚡ METEOR ⚡\n", NamedTextColor.DARK_RED, TextDecoration.BOLD))
                .append(Component.text("================\n\n", NamedTextColor.DARK_GREEN))
                .append(Component.text("Na wskazany obszar zostaje \n", NamedTextColor.DARK_GRAY))
                .append(Component.text("przywołany płonący meteoryt.\n\n", NamedTextColor.DARK_GRAY))
                .append(Component.text("Obrażenia: ", NamedTextColor.DARK_RED)).append(Component.text("10 HP.\n\n", NamedTextColor.RED))
                .append(Component.text("» Ustaw jako czar domyślny", NamedTextColor.GOLD)
                        .clickEvent(ClickEvent.runCommand("/setdefault meteor"))
                        .hoverEvent(HoverEvent.showText(Component.text("Kliknij, aby ustawić domyślny czar", NamedTextColor.YELLOW)))
                );

        Component healPage = Component.text("================\n", NamedTextColor.DARK_RED, TextDecoration.BOLD)
                .append(Component.text("⚡ PRZEMIANA: LIS ⚡\n", NamedTextColor.DARK_RED, TextDecoration.BOLD))
                .append(Component.text("================\n\n", NamedTextColor.DARK_GREEN))
                .append(Component.text("Przemienia rzucającego \n", NamedTextColor.DARK_GRAY))
                .append(Component.text("w lisa.\n\n", NamedTextColor.DARK_GRAY))
                .append(Component.text("» Ustaw jako czar domyślny", NamedTextColor.GOLD)
                        .clickEvent(ClickEvent.runCommand("/setdefault meteor"))
                        .hoverEvent(HoverEvent.showText(Component.text("Kliknij, aby ustawić domyślny czar", NamedTextColor.YELLOW)))
                );

        meta.addPages(meteorPage, healPage /* kolejne */);
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        book.setItemMeta(meta);
        return book;
    }

}
