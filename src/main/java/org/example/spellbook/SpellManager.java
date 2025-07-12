package org.example.spellbook;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.calculator.DamageCalculator;
import org.example.context.PreCastSpellContext;
import org.example.context.VisualSpellContext;
import org.example.context.VisualSpellContextAttributes;
import org.example.entity.SpellCaster;
import org.example.handler.PipelineExecutor;
import org.example.handler.VisualPipelineExecutor;
import org.example.spell.frostbolt.FireballSpell;
import org.example.spell.meteor.MeteorSpell;
import org.example.spell.Spell;

import java.util.*;

public class SpellManager {

    private final PipelineExecutor pipelineExecutor;
    private final VisualPipelineExecutor visualPipelineExecutor;
    private Map<UUID, Spellbook> spellbooks = new HashMap<>();
    private JavaPlugin plugin;

    public SpellManager(PipelineExecutor pipelineExecutor, VisualPipelineExecutor visualPipelineExecutor, JavaPlugin plugin) {
        this.plugin = plugin;
        this.pipelineExecutor = pipelineExecutor;
        this.visualPipelineExecutor = visualPipelineExecutor;
    }

    private static final List<Spell> allSpells = List.of(
            new MeteorSpell(),
            new FireballSpell()
    );

    public static Spell getById(String id) {
        return allSpells.stream()
                .filter(spell -> spell.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Spell with id " + id + " not found"));
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

    /** Zapisuje dane Spellbook w PDC (tylko gdy Player). */
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


    /** Ładuje wszystkie online PlayerSpellbooki do mapy. */
    public void loadAllOnline() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            loadSpellbookFor(p);
        }
    }

    /** Zapisuje wszystkie spellbooki online. */
    public void saveAllOnline() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            saveSpellbookFor(p);
        }
    }

    public void castSpell(LivingEntity caster) {

        SpellCaster spellCaster = new SpellCaster();
        spellCaster.setNegativeStatuses(Collections.emptyList());
        spellCaster.setPositiveStatuses(Collections.emptyList());
        spellCaster.setCaster(caster);

        Spell selectedSpell = loadSpellbookFor(caster).getSelectedSpell();

        PreCastSpellContext preCastSpellContext = new PreCastSpellContext(
                spellCaster, selectedSpell
        );

        VisualSpellContext visualSpellContext = new VisualSpellContext();
        visualSpellContext.setSpell(selectedSpell);
        visualSpellContext.setCaster(spellCaster);
        visualSpellContext.addAttribute(VisualSpellContextAttributes.PLUGIN, this.plugin);
        visualSpellContext.addAttribute(VisualSpellContextAttributes.EXECUTOR, this.pipelineExecutor);
        visualSpellContext.addAttribute(VisualSpellContextAttributes.DAMAGE_CALCULATOR, new DamageCalculator());

        if (!this.pipelineExecutor.handlePreCast(preCastSpellContext)) {
            return;
        }

        visualPipelineExecutor.handleSpellRendering(visualSpellContext);

    }
}
