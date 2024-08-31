package org.firstinspires.ftc.teamcode.centerstage.alliance;

import org.firstinspires.ftc.teamcode.AllianceCore;

public abstract class AllianceBase extends AllianceCore {
    public AllianceBase(Alliance alliance) {
        super(alliance);
    }

    abstract public double getBackdropDeg();
}
