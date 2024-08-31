package org.firstinspires.ftc.teamcode.centerstage.auto.position;

import org.firstinspires.ftc.teamcode.centerstage.auto.Auto;

public abstract class PositionBase {
    public abstract boolean allowParkOnly();
    public abstract int getPixelDropHeight();
    public abstract Auto.Selection getDefaultPark();
}
