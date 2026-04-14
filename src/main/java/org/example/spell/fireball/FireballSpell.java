package org.example.spell.fireball;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.example.context.DamageType;
import org.example.spell.AreaSpell;
import org.example.spell.InterruptibleSpell;
import org.example.spell.UpgradeableSpell;
import org.example.type.SpellType;

public class FireballSpell implements AreaSpell, InterruptibleSpell, UpgradeableSpell {

    private int experience = 7;

    @Override
    public String getId() {
        return "FIREBALL";
    }

    @Override
    public String getName() {
        return "Kula ognia";
    }

    @Override
    public Long getCooldown() {
        return 3_000L;
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.FIREBALL;
    }

    @Override
    public DamageType getType() {
        return DamageType.FIRE;
    }

    @Override
    public Component getTitle() {
        return Component.text("================\n", NamedTextColor.DARK_RED, TextDecoration.BOLD)
                .append(Component.text("⚡ Kula ognia ⚡\n", NamedTextColor.DARK_RED, TextDecoration.BOLD))
                .append(Component.text("================\n", NamedTextColor.DARK_RED, TextDecoration.BOLD));
    }

    @Override
    public Component getContent() {
        return Component.text("Przeciwnik otrzymuje obrażenia \n", NamedTextColor.DARK_GRAY)
                .append(Component.text("od kuli ognia.\n", NamedTextColor.DARK_GRAY));
    }

    @Override
    public int getExperience() {
        return this.experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
