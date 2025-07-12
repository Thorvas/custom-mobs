package org.example.factory;

import net.kyori.adventure.text.Component;
import org.example.calculator.MetaExecutor;
import org.example.context.SpellContext;
import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.spell.Spell;

public class SpellDescriptionFactory {

    public SpellDescriptionFactory(MetaExecutor metaExecutor) {
        this.metaExecutor = metaExecutor;
    }

    private final MetaExecutor metaExecutor;


    public Component createSpellDescription(Spell spell) {

        SpellContext context = new SpellContext(spell, new SpellTarget(), new SpellCaster(), 0);

        return spell.getTitle()
                .append(spell.getContent())
                .append(metaExecutor.calculateMeta(context));
    }
}
