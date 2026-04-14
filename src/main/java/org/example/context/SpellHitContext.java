package org.example.context;

import org.bukkit.Location;

public class SpellHitContext {

    private Location hitLocation;

    public void setHitLocation(Location hitLocation) {
        this.hitLocation = hitLocation;
    }

    public Location getHitLocation() {
        return hitLocation;
    }
}
