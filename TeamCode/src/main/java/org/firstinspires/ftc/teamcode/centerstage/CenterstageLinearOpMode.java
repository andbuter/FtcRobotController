package org.firstinspires.ftc.teamcode.centerstage;

import org.firstinspires.ftc.teamcode.LinearOpMode9808;
import org.firstinspires.ftc.teamcode.centerstage.alliance.AllianceBase;

public abstract class CenterstageLinearOpMode extends LinearOpMode9808  {
    protected double getBackdropDeg() { return ((AllianceBase) getColorBase()).getBackdropDeg();}
}
