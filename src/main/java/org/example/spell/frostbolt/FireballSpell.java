package org.example.spell.frostbolt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.example.context.DamageType;
import org.example.spell.AreaSpell;
import org.example.spell.InterruptibleSpell;
import org.example.spell.UpgradeableSpell;

public class FireballSpell implements AreaSpell, InterruptibleSpell, UpgradeableSpell {

    // Parametry lotu fireballa
    double speed = 5.0;    // bloki na tick
    double maxRange = 30.0;   // maksymalna odległość
    double explosionRadius = 5.0;
    float explosionPower = 1.0f;
    double baseDamage = 4.0;

    private int experience = 5;

    @Override
    public void cast(Player player) {

    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(double explosionRadius) {
        this.explosionRadius = explosionRadius;
    }

    public float getExplosionPower() {
        return explosionPower;
    }

    public void setExplosionPower(float explosionPower) {
        this.explosionPower = explosionPower;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(double baseDamage) {
        this.baseDamage = baseDamage;
    }

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
