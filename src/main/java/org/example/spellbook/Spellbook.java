package org.example.spellbook;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.example.spell.frostbolt.FireballSpell;
import org.example.spell.meteor.MeteorSpell;
import org.example.spell.Spell;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Spellbook {

    private static final String SPELLS_KEY    = "known_spells";
    private static final String SELECT_KEY    = "selected_spell";

    private final NamespacedKey spellsKey;
    private final NamespacedKey selectKey;
    private final Gson gson = new Gson();

    private List<Spell> knownSpells = new ArrayList<>();
    private int spellIndex = 0;

    public Spellbook(JavaPlugin plugin) {
        this.spellsKey = new NamespacedKey(plugin, SPELLS_KEY);
        this.selectKey = new NamespacedKey(plugin, SELECT_KEY);

        knownSpells.add(new MeteorSpell());
        knownSpells.add(new FireballSpell());
    }

    public int getSpellIndex() {
        return spellIndex;
    }

    public void setSpellIndex(int spellIndex) {
        this.spellIndex = spellIndex;
    }

    public List<Spell> getKnownSpells() {
        return knownSpells;
    }

    public void setKnownSpells(List<Spell> knownSpells) {
        this.knownSpells = knownSpells;
    }

    public Spell getSelectedSpell() {
        if (spellIndex < 0 || spellIndex >= knownSpells.size()) {
            return null;
        }
        return knownSpells.get(spellIndex);
    }

    /** Wczytuje z PDC listę znanych zaklęć i wybrany index */
    public void loadFromPDC(Player p) {

        PersistentDataContainer c = p.getPersistentDataContainer();

        String json = c.get(spellsKey, PersistentDataType.STRING);
        if (json != null) {
            // odczytujemy listę ID i mapujemy na Spell
            List<String> ids = gson.fromJson(
                    json, new TypeToken<List<String>>(){}.getType()
            );
            knownSpells.clear();
            for (String id : ids) {
                Spell s = SpellManager.getById(id);
                if (s != null) knownSpells.add(s);
            }
        }

        Integer idx = c.get(selectKey, PersistentDataType.INTEGER);
        if (idx != null && idx >= 0 && idx < knownSpells.size()) {
            spellIndex = idx;
        } else {
            spellIndex = 0;
        }
    }

    /** Zapisuje do PDC listę ID znanych zaklęć i wybrany index */
    public void saveToPDC(Player p) {
        PersistentDataContainer c = p.getPersistentDataContainer();

        // serializujemy tylko ID
        List<String> ids = new ArrayList<>();
        for (Spell s : knownSpells) ids.add(s.getId());
        c.set(spellsKey, PersistentDataType.STRING, gson.toJson(ids));

        c.set(selectKey, PersistentDataType.INTEGER, spellIndex);
    }
}
