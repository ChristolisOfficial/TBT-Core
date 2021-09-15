package com.christolis.tbtcore.game;

import org.bukkit.Location;

public class CuboidArea {

    private Location a;
    private Location b;

    public CuboidArea(Location a, Location b) {
        this.a = a;
        this.b = b;
    }

    public Location getA() {
        return a;
    }

    public Location getB() {
        return b;
    }

    // TODO Move AABB method here.
}
