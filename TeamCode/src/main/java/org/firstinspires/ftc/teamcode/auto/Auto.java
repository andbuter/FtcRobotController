package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.teamcode.HeadingHolder;
import org.firstinspires.ftc.teamcode.LinearOpMode9808;

import org.firstinspires.ftc.teamcode.VisionBase;
import org.firstinspires.ftc.teamcode.auto.position.PositionBase;

abstract public class Auto extends LinearOpMode9808 {
    public enum Selection {
        PARK_WALL,
        PARK_MID,
        PARK_ONLY
    }

    protected enum PropLoc {
        LOC_WALL,
        LOC_CENTER,
        LOC_MID,
        LOC_INVALID
    }

    /**
     * Distance from the wall to each aprilTag on the backdrop
     */
    private final double[] backdropTagLocations = {
            22.0, // LOC_WALL
            28.5, // LOC_CENTER
            35.0, // LOC_MID
            Double.NaN // LOC_INVALID
    };

    protected double midParkDistFromWall = 54.0;

    private VisionBase vision;

    protected Selection modeSelection;

    abstract protected PositionBase getPositionBase();

    abstract protected void plow( String propLocation );
    abstract protected double distanceToCloseWall();
    abstract protected int getAprilTagId( String propLocation );
    abstract protected double allianceCorrectedDistance(double distance );
    abstract protected PropLoc getPropLoc( String propLocation );

    /*        PositionBase Wrappers                     */
    protected int getPixelDropHeight() { return getPositionBase().getPixelDropHeight(); }
    protected Selection getDefaultPark() { return getPositionBase().getDefaultPark(); }
    protected boolean allowParkOnly() { return getPositionBase().allowParkOnly(); }

    protected double getCurrentBackdropTagLocation() {
        return backdropTagLocations[getPropLoc( vision.propLocation ).ordinal()];
    }

    protected void pre_init_9808() {
        vision = new VisionBase();
        driveBase.init(hardwareMap, this);
        vision.initDoubleVision(hardwareMap, this);

        // Initial orientation
        HeadingHolder.setHeading(0.0);
        driveBase.imu.resetYaw();
        driveBase.arm.setPower(.8);

        // Setup arm and positions
        driveBase.grip.setPosition(driveBase.gripOpened);//holding the yellow pixel
        sleep(500);
        driveBase.tilt.setPosition(driveBase.tiltVertical);
        driveBase.arm.setTargetPosition(200);
        driveBase.liftRelease.setPosition(driveBase.liftReleaseClosed);
        driveBase.droneRelease.setPosition(driveBase.droneReleaseClosed);

        setLEDHeartbeat();
    }

    protected void init_9808() {
        vision.locateOurPropStartingRightEdge();

        modeSelection = getDefaultPark();

        // Get controller input for path selection
        getPathSelection();

        switch (vision.propLocation) {
            case "Left":
                driveBase.setSolidRedLED();
                break;
            case "Center":
                driveBase.setSolidGoldLED();
                break;
            default:
                driveBase.setSolidGreenLED();
        }
        telemetry.addLine("ready for START");
        telemetry.update();
    }

    protected void run_9808() {
        boolean parkOnly = modeSelection == Selection.PARK_ONLY;
        if( opModeIsActive() ) {
            plow( vision.propLocation );
            driveToBackdrop( vision.propLocation, parkOnly );
            if( !parkOnly ) {
                findAprilTag();
                dropPixel();
                park();
            }
            wrapUp();
        }
    }

    protected void getPathSelection() {
        if (modeSelection == Selection.PARK_WALL) {
            telemetry.addLine("Full Auto Park Wall");
            telemetry.addLine("Press A to change to Full Auto Park Mid");
            if(allowParkOnly()) {
                telemetry.addLine("Press X to change to PARK ONLY");
            }
        }
        if (modeSelection == Selection.PARK_MID) {
            telemetry.addLine("Full Auto Park Mid");
            telemetry.addLine("Press B to change to Full Auto Park Wall");
            if(allowParkOnly()) {
                telemetry.addLine("Press X to change to PARK ONLY");
            }
        }
        if (modeSelection == Selection.PARK_ONLY) {
            telemetry.addLine("PARK ONLY");
            telemetry.addLine("Press A to change to Full Auto Park Mid");
            telemetry.addLine("Press B to change to Full Auto Park Wall");
        }
        if (gamepad1.a) {
            modeSelection = Selection.PARK_MID;
        } else if (gamepad1.b) {
            modeSelection = Selection.PARK_WALL;
        } else if (gamepad1.x && allowParkOnly()) {
            modeSelection = Selection.PARK_ONLY;
        }
    }

    protected void dropPixel() {
        driveBase.gyroTurn(.5, getBackdropDeg());
        driveBase.tilt.setPosition(driveBase.tiltToRelease);
        driveBase.arm.setTargetPosition(getPixelDropHeight());
        while (driveBase.arm.isBusy());
        driveBase.tankDrive(.3, 3.0);
        driveBase.gyroTurn(.5, getBackdropDeg());
        driveBase.DriveSidewaysCorrected(.3, vision.lateralOffset + driveBase.cameraOffset, getBackdropDeg());
        driveBase.gyroTurn(.6, getBackdropDeg());
        vision.getDistancesToAprilTag(getAprilTagId(vision.propLocation));
        sleep(50);
        driveBase.tankDrive(.3, vision.forwardDistanceToTag - 9.75);
        driveBase.gyroTurn(.6, getBackdropDeg());
        driveBase.grip.setPosition(driveBase.gripClosed);
        sleep(1000);
        driveBase.tankDrive(.3, -5);
    }

    private void findAprilTag() {
        driveBase.runtime.reset();
        vision.targetFound = false;
        while (!vision.targetFound && driveBase.runtime.seconds() < 1.0) {
            vision.getDistancesToAprilTag(getAprilTagId(vision.propLocation));// loop until we find the target
        }
        telemetry.addData("Prop Location: ", vision.propLocation);
        telemetry.addData("time to Detect apriltag: ", driveBase.runtime.seconds());
        telemetry.addData("Lateral Offset to Tag: ", vision.lateralOffset);
        telemetry.addData("forward distance to backdrop: ", vision.forwardDistanceToTag);
        telemetry.update();
    }

    protected void park() {
        double distance = Double.NaN;
        if (modeSelection == Selection.PARK_WALL) {
            distance = 4 - distanceToCloseWall();
        } else if (modeSelection == Selection.PARK_MID) {
            distance = midParkDistFromWall - getCurrentBackdropTagLocation();
        } else if (modeSelection == Selection.PARK_ONLY) {
            // This is handled by driveToBackdrop
        }

        if (distance != Double.NaN) {
            driveBase.DriveSidewaysCorrected(.5, allianceCorrectedDistance(distance), getBackdropDeg());
        }
    }

    protected void wrapUp() {
        driveBase.tilt.setPosition(driveBase.tiltVertical);
        sleep(500);
        setLEDHeartbeat();
        driveBase.gyroTurn(.6, getBackdropDeg());
        driveBase.tankDrive(.3, 10);
        driveBase.arm.setTargetPosition(5);
        driveBase.gyroTurn(.6, getBackdropDeg());
        HeadingHolder.setHeading(driveBase.robotFieldHeading());
        telemetry.addData("Path", "Complete");

        telemetry.update();
        while (opModeIsActive());
    }

    protected void driveToBackdrop( String propLocation, boolean parkOnly ) {
        // Backdrop drive rules
        driveBase.gyroTurn(.6,getBackdropDeg());
        driveBase.tankDrive(.5,15);
        double distance = getCurrentBackdropTagLocation() - distanceToCloseWall();
        driveBase.DriveSidewaysCorrected(.5, allianceCorrectedDistance(distance), getBackdropDeg());
    }
}
