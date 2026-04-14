// src/main/java/org/example/MeteorPlugin.java
package org.example;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.calculator.IMetaCalculator;
import org.example.calculator.MetaContextResolver;
import org.example.calculator.MetaExecutor;
import org.example.calculator.fireball.*;
import org.example.calculator.meteor.MeteorCalculateManager;
import org.example.calculator.meteor.MeteorDamageCalculator;
import org.example.calculator.meteor.MeteorKnockbackCalculator;
import org.example.calculator.meteor.MeteorMetaCalculator;
import org.example.factory.SpellDescriptionFactory;
import org.example.handler.*;
import org.example.handler.fireball.FireballApplyDamageHandlerCasting;
import org.example.handler.fireball.FireballApplyKnockbackHandlerCasting;
import org.example.handler.fireball.FireballVfxHitHandler;
import org.example.handler.fireball.FireballVfxMoveHandler;
import org.example.handler.meteor.MeteorApplyDamageHandlerCasting;
import org.example.handler.meteor.MeteorApplyKnockbackHandlerCasting;
import org.example.handler.meteor.MeteorVfxHitHandler;
import org.example.handler.meteor.MeteorVfxMoveHandler;
import org.example.listener.SpellHitListener;
import org.example.listener.SpellMoveListener;
import org.example.listener.SpellSequenceListener;
import org.example.registry.MeteorProjectileSpellDefinition;
import org.example.registry.ProjectileSpellDefinition;
import org.example.registry.SpellRegistry;
import org.example.spell.Spell;
import org.example.spellbook.CooldownManager;
import org.example.spellbook.SpellManager;
import org.example.spellbook.Spellbook;
import org.example.type.SpellType;

import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin implements Listener {

    private SpellManager spellManager;
    private SpellDescriptionFactory spellDescriptionFactory;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        MetaContextResolver metaContextResolver = new MetaContextResolver();
        FireballDamageCalculator fireballDamageCalculator = new FireballDamageCalculator();
        FireballKnockbackCalculator fireballKnockbackCalculator = new FireballKnockbackCalculator();
        FireballRangeCalculator fireballRangeCalculator = new FireballRangeCalculator();
        FireballCalculateManager fireballCalculateManager = new FireballCalculateManager(
                fireballDamageCalculator, fireballKnockbackCalculator, fireballRangeCalculator
        );

        MeteorDamageCalculator meteorDamageCalculator = new MeteorDamageCalculator();
        MeteorKnockbackCalculator meteorKnockbackCalculator = new MeteorKnockbackCalculator();
        MeteorCalculateManager meteorCalculateManager = new MeteorCalculateManager(
                meteorDamageCalculator, meteorKnockbackCalculator
        );

        List<IMetaCalculator> metaCalculators = List.of(
                new FireballMetaCalculator(fireballCalculateManager),
                new MeteorMetaCalculator(meteorCalculateManager)
        );
        MetaExecutor metaExecutor = new MetaExecutor(metaCalculators);

        ProjectileSpellDefinition fireballSpellDefinition = new ProjectileSpellDefinition();
        MeteorProjectileSpellDefinition meteorProjectileSpellDefinition = new MeteorProjectileSpellDefinition();

        SpellRegistry spellRegistry = new SpellRegistry(
                Map.of(
                        SpellType.FIREBALL, fireballSpellDefinition,
                        SpellType.METEOR, meteorProjectileSpellDefinition
                )
        );

        MeteorVfxHitHandler meteorVfxHitHandler = new MeteorVfxHitHandler(spellRegistry, this);
        MeteorVfxMoveHandler meteorVfxMoveHandler = new MeteorVfxMoveHandler();

        FireballVfxHitHandler fireballVfxHitHandler = new FireballVfxHitHandler(this, spellRegistry);
        FireballVfxMoveHandler fireballVfxMoveHandler = new FireballVfxMoveHandler();

        // 1) Inicjalizujesz SpellManager
        this.spellManager = new SpellManager(
                new PipelineExecutor(
                        List.of(new CheckCooldownStatusHandler(new CooldownManager()),
                                new CheckStunnedStatusHandler()),
                        List.of(new MeteorApplyDamageHandlerCasting(meteorCalculateManager),
                                new MeteorApplyKnockbackHandlerCasting(),
                                new FireballApplyKnockbackHandlerCasting(),
                                new FireballApplyDamageHandlerCasting(fireballCalculateManager))
                ),
                new SpellCastExecutor(this, spellRegistry), this);

        this.spellDescriptionFactory = new SpellDescriptionFactory(metaExecutor, metaContextResolver, spellManager);

        // 2) Ładujesz wszystkich online graczy
        spellManager.loadAllOnline();
        getServer().getPluginManager().registerEvents(
                new SpellSequenceListener(spellManager),
                this
        );

        getServer().getPluginManager().registerEvents(
                new SpellHitListener(meteorVfxHitHandler, fireballVfxHitHandler),
                this
        );

        getServer().getPluginManager().registerEvents(
                new SpellMoveListener(fireballVfxMoveHandler, meteorVfxMoveHandler),
                this
        );

        getCommand("resetspells").setExecutor((sender, cmd, label, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Tylko gracze mogą resetować czary.");
                return true;
            }
            Player p = (Player) sender;
            spellManager.resetSpellbookFor(p);
            p.sendMessage("Twoje zaklęcia zostały zresetowane.");
            return true;
        });
    }

    @Override
    public void onDisable() {
        // zapisujesz stan przed wyłączeniem
        spellManager.saveAllOnline();
    }

    // --- listenerzy do join/quit ---

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        spellManager.loadSpellbookFor(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        spellManager.saveSpellbookFor(e.getPlayer());
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Sprawdź czy gracz trzyma książkę
        if (item.getType() != Material.BOOK ||
                item.getItemMeta() == null ||
                !item.getItemMeta().hasItemName() ||
                !item.getItemMeta().itemName().equals(Component.text("Ksiega zaklec"))) return;

        // Anuluj domyślne działanie (np. otwieranie książki)
        event.setCancelled(true);

        // Otwórz księgę zaklęć
        openSpellbook(player, spellDescriptionFactory);
    }

    private void openSpellbook(Player player, SpellDescriptionFactory spellDescriptionFactory) {
        // pobierz Spellbook gracza
        Spellbook book = spellManager.loadSpellbookFor(player);

        // teraz budujesz ItemStack z zawartością według book.getKnownSpells()
        BookMeta meta = (BookMeta) new ItemStack(Material.WRITTEN_BOOK).getItemMeta();
        meta.title(Component.text("Księga Zaklęć", NamedTextColor.GOLD));
        meta.author(Component.text("Mag Arcanum", NamedTextColor.LIGHT_PURPLE));

        // dla każdego znanego spella tworzysz stronę
        for (Spell s : book.getKnownSpells()) {
            Component page = spellDescriptionFactory.createSpellDescription(s, player);
            meta.addPages(page);
        }
        ItemStack display = new ItemStack(Material.WRITTEN_BOOK);
        display.setItemMeta(meta);
        player.openBook(display);
    }
}
