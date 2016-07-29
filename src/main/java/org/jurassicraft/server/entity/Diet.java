package org.jurassicraft.server.entity;

public enum Diet {
    HERBIVORE(false, true, false, false, 0x007F0E),
    CARNIVORE(true, false, false, false, 0xB70000),
    OMNIVORE(true, true, true, false, 0xB77F0E),
    PISCIVORE(false, false, true, false, 0x437EA8),
    PISCIVORE_CARNIVORE(true, false, true, false, 0x805474),
    INSECTIVORE(false, false, false, true, 0);

    private boolean isCarnivorous;
    private boolean isHerbivorous;
    private boolean isPiscivorous;
    private boolean isInsectivorous;

    private int color;

    Diet(boolean isCarnivorous, boolean isHerbivorous, boolean isPiscivorous, boolean isInsectivorous, int color) {
        this.isCarnivorous = isCarnivorous;
        this.isHerbivorous = isHerbivorous;
        this.isPiscivorous = isPiscivorous;
        this.isInsectivorous = isInsectivorous;
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

    public boolean isInsectivorous() {
        return this.isInsectivorous;
    }

    public int getColor() {
        return this.color;
    }
}
