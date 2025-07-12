package org.example.context;

import org.example.entity.SpellCaster;
import org.example.spell.Spell;

public class PreCastSpellContext {

    private final SpellCaster caster;
    private final Spell spell;

    public PreCastSpellContext(SpellCaster caster, Spell spell) {
        this.caster = caster;
        this.spell = spell;
    }

    public SpellCaster getCaster() {
        return caster;
    }

    public Spell getSpell() {
        return spell;
    }

}
