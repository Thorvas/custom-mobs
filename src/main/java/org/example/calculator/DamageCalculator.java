package org.example.calculator;

public class DamageCalculator {

    public double calculateScaledDamage(int level, double maxHP, double k) {
        return maxHP * level / (level + k);
    }
}
