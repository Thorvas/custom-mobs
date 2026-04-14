package org.example.registry;

public class MeteorProjectileSpellDefinition extends ProjectileSpellDefinition {

    public double getMeteorStartHeight() {
        // ile bloków nad miejscem kliknięcia wyjdzie meteor
        double startHeight = 50.0;
        return startHeight;
    }

    public double getMeteorFallSpeed() {
        // bloki na tick
        double speed = 2.5;
        return speed;
    }

    public double getMeteorExplosionRadius() {
        // zasięg obrażeń meteoru
        double explosionRadius = 5.0;
        return explosionRadius;
    }

    public float getMeteorExplosionPower() {
        // siła wybuchu (0 = brak zniszczeń)
        float explosionPower = 0f;
        return explosionPower;
    }

    public double getMeteorBaseDamage() {
        // obrażenia meteoru
        double meteorBaseDamage = 10.0;
        return meteorBaseDamage;
    }

    public int getMeteorGroundFlameCount() {
        // ilość płomieni pozostawionych po wybuchu
        int groundFlameCount = 20;
        return groundFlameCount;
    }

    public double getMeteorMaxDistance() {
        double maxRange = 20.0;
        return maxRange;
    }

    @Override
    public double getProjectileMaxRange() {
        // Ensure the projectile simulation doesn't end before the meteor reaches the ground
        double vertical = getMeteorStartHeight();
        double horizontal = getMeteorMaxDistance();
        // hypotenuse plus small buffer
        return Math.max(super.getProjectileMaxRange(), Math.hypot(vertical, horizontal) + 5.0);
    }

    @Override
    public double getProjectileBaseSpeed() {
        return 3.0;
    }
}
