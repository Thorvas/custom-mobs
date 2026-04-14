package org.example.spell.meteor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.example.context.DamageType;
import org.example.spell.AreaSpell;
import org.example.spell.InterruptibleSpell;
import org.example.spell.UpgradeableSpell;
import org.example.type.SpellType;

public class MeteorSpell implements AreaSpell, InterruptibleSpell, UpgradeableSpell {

    private int experience = 7;
    private final String id = "METEOR";

    public String getId() {
        return id;
    }

    public String getName() {
        return "Meteoryt";
    }

    @Override
    public Long getCooldown() {
        return 10_000L;
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.METEOR;
    }

    @Override
    public DamageType getType() {
        return DamageType.FIRE;
    }

    @Override
    public Component getTitle() {
        return Component.text("================\n", NamedTextColor.DARK_RED, TextDecoration.BOLD)
                .append(Component.text("⚡ Meteoryt ⚡\n", NamedTextColor.DARK_RED, TextDecoration.BOLD))
                .append(Component.text("================\n", NamedTextColor.DARK_RED, TextDecoration.BOLD));
    }

    @Override
    public Component getContent() {
        return Component.text("Na wskazany obszar zostaje \n", NamedTextColor.DARK_GRAY)
                .append(Component.text("przywołany płonący meteoryt.\n", NamedTextColor.DARK_GRAY));
    }

    @Override
    public int getExperience() {
        return this.experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void cast(Player player) {
    }
}
