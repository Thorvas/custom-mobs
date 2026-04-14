package org.example.registry;

public class ProjectileSpellDefinition implements SpellDefinition {

    public double getProjectileBaseSpeed() {
        double speed = 5.0;
        return speed;
    }

    public double getProjectileBaseKnockback() {
        return 5.0;
    }

    public double getProjectileMaxRange() {
        double maxRange = 30.0;
        return maxRange;
    }

    public double getProjectileExplosionRadius() {
        double explosionRadius = 5.0;
        return explosionRadius;
    }

    public float getProjectileExplosionPower() {
        float explosionPower = 1.0f;
        return explosionPower;
    }

    public double getProjectileBaseDamage() {
        double baseDamage = 4.0;
        return baseDamage;
    }
}
