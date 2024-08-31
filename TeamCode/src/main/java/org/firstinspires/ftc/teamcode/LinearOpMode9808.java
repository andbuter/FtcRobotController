package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class LinearOpMode9808 extends LinearOpMode {
    abstract protected void pre_init_9808();
    abstract protected void init_9808();
    abstract protected void run_9808();
    abstract protected AllianceCore getColorBase();
    // 9808 HW interface layer
    protected DraculaBase driveBase = new DraculaBase();
    protected String getColorString() {return getColorBase().getColorString();}
    protected RevBlinkinLedDriver.BlinkinPattern getStaticColor() {return getColorBase().getStaticColor();}
    protected RevBlinkinLedDriver.BlinkinPattern getHeartbeatColor() {return getColorBase().getHeartbeatColor();}

    public void runOpMode() {
        pre_init_9808();
        while (opModeInInit()) {
            init_9808();
        }
        run_9808();
    }

    protected void setStaticLED() {
        driveBase.setLED(getStaticColor());
    }

    protected void setLEDHeartbeat() {
        driveBase.setLED(getHeartbeatColor());
    }
}
