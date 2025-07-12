package org.example.entity.status;

public enum PositiveEntityStatus implements EntityStatus {
    EMPOWERED("EMPOWERED");

    private final String name;

    // Konstruktor enuma
    PositiveEntityStatus(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}

