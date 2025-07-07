package org.example.entity;

public enum NegativeEntityStatus implements EntityStatus {
    STUNNED("STUNNED"),
    SILENCED("SILENCED");

    private final String name;

    NegativeEntityStatus(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
