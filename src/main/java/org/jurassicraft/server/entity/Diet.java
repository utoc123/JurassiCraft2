package org.jurassicraft.server.entity;

public enum Diet {
    HERBIVORE(false, true, false, false),
    CARNIVORE(true, false, false, false),
    OMNIVORE(true, true, true, false),
    PISCIVORE(false, false, true, false),
    PISCIVORE_CARNIVORE(true, false, true, false),
    INSECTIVORE(false, false, false, true),
    INSECTIVORE_CARNIVORE(true, false, false, true);

    private boolean isCarnivorous;
    private boolean isHerbivorous;
    private boolean isPiscivorous;
    private boolean isInsectivorous;

    Diet(boolean isCarnivorous, boolean isHerbivorous, boolean isPiscivorous, boolean isInsectivorous) {
        this.isCarnivorous = isCarnivorous;
        this.isHerbivorous = isHerbivorous;
        this.isPiscivorous = isPiscivorous;
        this.isInsectivorous = isInsectivorous;
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
}
