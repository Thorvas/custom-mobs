package org.example.spellbook;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.context.PreCastSpellContext;
import org.example.context.SpellContext;
import org.example.entity.SpellCaster;
import org.example.handler.PipelineExecutor;
import org.example.handler.SpellCastExecutor;
import org.example.spell.Spell;
import org.example.spell.fireball.FireballSpell;
import org.example.spell.meteor.MeteorSpell;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class SpellManager {

    private final PipelineExecutor pipelineExecutor;
    private final SpellCastExecutor spellCastExecutor;
    private Map<UUID, Spellbook> spellbooks = new HashMap<>();
    private JavaPlugin plugin;

    public SpellManager(PipelineExecutor pipelineExecutor, SpellCastExecutor spellCastExecutor, JavaPlugin plugin) {
        this.plugin = plugin;
        this.pipelineExecutor = pipelineExecutor;
        this.spellCastExecutor = spellCastExecutor;
    }

    private static final Map<String, Supplier<Spell>> allSpells = Map.of(
            "METEOR", MeteorSpell::new,
            "FIREBALL", FireballSpell::new
    );

    public static Spell getById(String id) {
        Supplier<Spell> supplier = allSpells.get(id);
        if (supplier == null) {
            throw new IllegalArgumentException("Spell with id " + id + " not found");
        }
        return supplier.get();
    }

    public Spellbook loadSpellbookFor(LivingEntity caster) {
        UUID id = caster.getUniqueId();
        Spellbook book = spellbooks.computeIfAbsent(id,
                __ -> new Spellbook(plugin)
        );
        // jeżeli to Player, ładuj z PDC
        if (caster instanceof Player) {
            book.loadFromPDC((Player) caster);
        }
        return book;
    }

    /**
     * Zapisuje dane Spellbook w PDC (tylko gdy Player).
     */
    public void saveSpellbookFor(LivingEntity caster) {
        if (!(caster instanceof Player)) return;
        UUID id = caster.getUniqueId();
        Spellbook book = spellbooks.get(id);
        if (book != null) {
            book.saveToPDC((Player) caster);
        }
    }

    public void resetSpellbookFor(LivingEntity caster) {
        UUID id = caster.getUniqueId();
        // 1) usuń stan w pamięci
        spellbooks.remove(id);

        // 2) usuń zapis w PDC (jeśli to gracz)
        if (caster instanceof Player p) {
            PersistentDataContainer c = p.getPersistentDataContainer();
            NamespacedKey spellsKey = new NamespacedKey(plugin, "known_spells");
            NamespacedKey selectKey = new NamespacedKey(plugin, "selected_spell");
            c.remove(spellsKey);
            c.remove(selectKey);
        }
    }


    /**
     * Ładuje wszystkie online PlayerSpellbooki do mapy.
     */
    public void loadAllOnline() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            loadSpellbookFor(p);
        }
    }

    /**
     * Zapisuje wszystkie spellbooki online.
     */
    public void saveAllOnline() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            saveSpellbookFor(p);
        }
    }

    public void castSpell(LivingEntity caster) {
        Spell selectedSpell = loadSpellbookFor(caster).getSelectedSpell();
        castSpell(caster, selectedSpell);
    }

    public void castSpell(LivingEntity caster, Spell spell) {

        SpellCaster spellCaster = new SpellCaster();
        spellCaster.setNegativeStatuses(Collections.emptyList());
        spellCaster.setPositiveStatuses(Collections.emptyList());
        spellCaster.setCaster(caster);

        Spell spellToCast = spell;
        if (spellToCast == null) {
            return;
        }

        PreCastSpellContext preCastSpellContext = new PreCastSpellContext(
                spellCaster, spellToCast
        );

        SpellContext spellContext = new SpellContext();
        spellContext.setSpell(spellToCast);
        spellContext.setCaster(spellCaster);

        if (!this.pipelineExecutor.handlePreCast(preCastSpellContext)) {
            return;
        }

        spellCastExecutor.handleSpellCast(spellContext);

    }
}
