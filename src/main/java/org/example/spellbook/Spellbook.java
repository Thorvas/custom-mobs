package org.example.spellbook;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.context.PreCastSpellContext;
import org.example.entity.SpellCaster;
import org.example.handler.PipelineExecutor;
import org.example.spell.MeteorSpell;

import java.util.Collections;

public class Spellbook {

    private final PipelineExecutor pipelineExecutor;
    private JavaPlugin plugin;

    public Spellbook(PipelineExecutor pipelineExecutor, JavaPlugin plugin) {
        this.plugin = plugin;
        this.pipelineExecutor = pipelineExecutor;
    }

    public void castSpell(LivingEntity caster) {

        SpellCaster spellCaster = new SpellCaster();
        spellCaster.setNegativeStatuses(Collections.emptyList());
        spellCaster.setPositiveStatuses(Collections.emptyList());
        spellCaster.setCaster(caster);

        PreCastSpellContext preCastSpellContext = new PreCastSpellContext(
                spellCaster
        );

        if (!this.pipelineExecutor.handlePreCast(preCastSpellContext)) {
            return;
        }

        MeteorSpell meteorSpell = new MeteorSpell(this.plugin, this.pipelineExecutor);
        meteorSpell.cast((Player) caster);
    }
}
