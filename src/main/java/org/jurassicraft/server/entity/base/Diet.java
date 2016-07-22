package org.jurassicraft.server.entity.base;

public enum Diet {
    HERBIVORE(false, true, false, 0x007F0E), CARNIVORE(true, false, true, 0xB70000), OMNIVORE(true, true, true, 0xB77F0E), PISCIVORE(false, false, true, 0x437EA8);

    private boolean isCarnivorous;
    private boolean isHerbivorous;
    private boolean isPiscivorous;

    private int color;

    Diet(boolean isCarnivorous, boolean isHerbivorous, boolean isPiscivorous, int color) {
        this.isCarnivorous = isCarnivorous;
        this.isHerbivorous = isHerbivorous;
        this.isPiscivorous = isPiscivorous;
        this.color = color;
    }

    public boolean isCarnivorous() {
        return this.isCarnivorous;
    }

    public boolean isHerbivorous() {
        return this.isHerbivorous;
    }

    public boolean isPiscivorous() {
        return this.isPiscivorous;
    }

    public int getColor() {
        return this.color;
    }
}
