package org.firstinspires.ftc.teamcode.auto.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.auto.position.PositionBase;
import org.firstinspires.ftc.teamcode.auto.position.WingBase;

@Autonomous(name = "Blue Wing (new)", group = "Autonomous")
public class Wing extends BlueAuto {
    private final WingBase positionBase = new WingBase();
    protected PositionBase getPositionBase() { return positionBase; }

    protected void plow( String propLocation ) {
        switch (propLocation) {
            case "Left":
                driveBase.plowFromBlueRightStartToLeftSpike();
                break;
            case "Right":
                driveBase.plowFromBlueRightStartToRightSpike();
                break;
            case "Center":
                driveBase.plowFromBlueRightStartToCenterSpike();
                break;
        }
    }

    @Override
    protected void driveToBackdrop( String propLocation, boolean parkOnly ) {
        double backwards = -1 * getBackdropDeg();
        double backdropDriveDistance = 0;

        driveBase.tilt.setPosition(driveBase.tiltToCarry);
        driveBase.arm.setTargetPosition(driveBase.armLowered);
        while(driveBase.arm.isBusy());

        switch (propLocation) {
            case "Left":
                backdropDriveDistance = -68;
                break;
            case "Right":
                backdropDriveDistance = -68;
                break;
            case "Center":
                backdropDriveDistance = -83;
                driveBase.gyroTurn(.6,backwards);
                driveBase.tankDrive(.3,10); // To get behind the purple pixel
                break;
        }

        driveBase.gyroTurn(.6,backwards);
        driveBase.DriveSidewaysCorrected(.5,-1*(midParkDistFromWall-driveBase.rightDistanceToWall()),backwards);
        driveBase.gyroTurn(.6,backwards);
        driveBase.tankDrive(.6,backdropDriveDistance);// drive under the stage door, into the middle

        // Move from the current location 54in from the wall, to in front of the target tag
        if (!parkOnly) {
            driveBase.gyroTurn(.6, getBackdropDeg());
            driveBase.DriveSidewaysCorrected(.5, -1*(midParkDistFromWall-getCurrentBackdropTagLocation()), backwards);
            driveBase.gyroTurn(.6, getBackdropDeg());
        }
    }
}
