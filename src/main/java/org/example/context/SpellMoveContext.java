package org.example.context;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class SpellMoveContext {

    private Vector delta;
    private Location at;
    private Location next;
    private Location target;
    private Vector direction;
    private double speed;

    public Location getTarget() {
        return target;
    }

    public void setTarget(Location target) {
        this.target = target;
    }

    public Location getNext() {
        return next;
    }

    public void setNext(Location next) {
        this.next = next;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Vector getDelta() {
        return delta;
    }

    public void setDelta(Vector delta) {
        this.delta = delta;
    }

    public Location getAt() {
        return at;
    }

    public void setAt(Location at) {
        this.at = at;
    }

    public Location getNextLocation() {
        return next;
    }

    public void setNextLocation(Location next) {
        this.next = next;
    }
}
