package org.jurassicraft.server.entity;

public final class LegSolverQuadruped extends LegSolver {
    public final Leg backLeft, backRight, frontLeft, frontRight;

    public LegSolverQuadruped(float forward, float side) {
        this(0, forward, side);
    }

    public LegSolverQuadruped(float forwardCenter, float forward, float side) {
        super(
            new Leg(forwardCenter - forward, side),
            new Leg(forwardCenter - forward, -side),
            new Leg(forwardCenter + forward, side),
            new Leg(forwardCenter + forward, -side)
        );
        this.backLeft = legs[0];
        this.backRight = legs[1];
        this.frontLeft = legs[2];
        this.frontRight = legs[3];
    }
}
