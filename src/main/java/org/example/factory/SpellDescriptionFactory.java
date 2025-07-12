package org.example.factory;

import net.kyori.adventure.text.Component;
import org.example.calculator.MetaContextResolver;
import org.example.calculator.MetaExecutor;
import org.example.context.SpellContext;
import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.spell.Spell;

public class SpellDescriptionFactory {

    public SpellDescriptionFactory(MetaExecutor metaExecutor, MetaContextResolver metaContextResolver) {
        this.metaExecutor = metaExecutor;
        this.metaContextResolver = metaContextResolver;
    }

    private final MetaExecutor metaExecutor;
    private final MetaContextResolver metaContextResolver;


    public Component createSpellDescription(Spell spell) {

        SpellContext context = new SpellContext(spell, new SpellTarget(), new SpellCaster(), 0);

        metaContextResolver.resolveContext(context, spell);

        return spell.getTitle()
                .append(spell.getContent())
                .append(metaExecutor.calculateMeta(context));
    }
}
