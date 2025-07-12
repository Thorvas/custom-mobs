package org.example.context;

import org.example.entity.SpellCaster;
import org.example.spell.Spell;

import java.util.HashMap;
import java.util.Map;

public class VisualSpellContext {

    private Map<VisualSpellContextAttributes, Object> metadata = new HashMap<>();

    public <T> void addAttribute(VisualSpellContextAttributes key, T value) {
        metadata.put(key, value);
    }

    public <T> T getAttr(VisualSpellContextAttributes key) {
        return metadata.get(key) != null ? (T) metadata.get(key) : null;
    }


    public Spell getSpell() {
        return spell;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public SpellCaster getCaster() {
        return caster;
    }

    public void setCaster(SpellCaster caster) {
        this.caster = caster;
    }

    private Spell spell;
    private SpellCaster caster;
}
