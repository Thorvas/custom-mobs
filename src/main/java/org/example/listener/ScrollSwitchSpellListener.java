package org.example.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.example.spell.Spell;
import org.example.spellbook.SpellManager;
import org.example.spellbook.Spellbook;

public class ScrollSwitchSpellListener implements Listener {

    private final SpellManager spellManager;

    public ScrollSwitchSpellListener(SpellManager spellManager) {
        this.spellManager = spellManager;
    }

    @EventHandler
    public void onScrollSpell(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        // Tylko jeśli gracz kuca:
        if (!player.isSneaking()) return;

        // Sprawdź, czy trzyma książkę w off-hand
        ItemStack off = player.getInventory().getItemInOffHand();
        if (off.getType() != Material.BOOK ||
                off.getItemMeta() == null ||
                !off.getItemMeta().hasItemName() ||
                !off.getItemMeta().itemName().equals(Component.text("Ksiega zaklec"))) return;

        e.setCancelled(true);

        // Pobierz jego spellbook (już załadowany w SpellManager)
        Spellbook book = spellManager.loadSpellbookFor(player);

        // Oblicz kierunek scrolla:
        int prev = e.getPreviousSlot();
        int next = e.getNewSlot();
        int size = book.getKnownSpells().size();
        if (size == 0) return;

        int idx = book.getSpellIndex();
        // forward scroll (next slot)
        if ((next > prev) || (prev == 8 && next == 0)) {
            idx = (idx + 1) % size;
        } else {
            // backward scroll (prev slot or wrap)
            idx = (idx - 1 + size) % size;
        }
        book.setSpellIndex(idx);

        // opcjonalnie: natychmiast zapisz do PDC
        spellManager.saveSpellbookFor(player);

        // Wyświetl ActionBar z nazwą
        Spell chosen = book.getKnownSpells().get(idx);

        player.playSound(
                player.getLocation(),
                Sound.UI_BUTTON_CLICK,
                SoundCategory.PLAYERS,
                1.0f,
                1.0f
        );
        player.sendActionBar(Component.text("Wybrano: " + chosen.getName(), NamedTextColor.GOLD));
    }
}
