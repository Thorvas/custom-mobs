package org.example.registry;

import org.example.type.SpellType;

import java.util.Map;

public class SpellRegistry {

    private final Map<SpellType, SpellDefinition> definitions;

    public SpellRegistry(Map<SpellType, SpellDefinition> definitions) {
        this.definitions = definitions;
    }

    public SpellDefinition getSpellDefinition(SpellType spellType) {
        return definitions.get(spellType);
    }

}
