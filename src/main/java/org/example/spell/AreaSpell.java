package org.example.spell;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface AreaSpell extends Spell {

    void cast(Player player);
}
