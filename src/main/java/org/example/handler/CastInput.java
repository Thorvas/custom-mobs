package org.example.handler;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class CastInput {

    private Location from;
    private Location target;
    private Vector direction;

    public CastInput(Location from, Vector direction, Location target) {
        this.from = from;
        this.direction = direction;
        this.target = target;
    }

    public Location getTarget() {
        return target;
    }

    public void setTarget(Location target) {
        this.target = target;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }
}
