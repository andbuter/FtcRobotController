package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

public class AllianceCore {
    public enum Alliance {
        UNKNOWN,
        BLUE,
        RED
    }

    private final Alliance alliance;

    public AllianceCore( Alliance alliance ) {
        this.alliance = alliance;
    }

    public AllianceCore() {
        this.alliance = Alliance.UNKNOWN;
    }

    public String getColorString() {
        switch( this.alliance ) {
            case BLUE: return "BLUE";
            case RED: return "RED";
        }
        return "UNKNOWN";
    }

    public RevBlinkinLedDriver.BlinkinPattern getHeartbeatColor() {
        switch( this.alliance ) {
            case BLUE: return RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_BLUE;
            case RED: return RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED;
        }
        return RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_WHITE;
    }

    public RevBlinkinLedDriver.BlinkinPattern getStaticColor() {
        switch( this.alliance ) {
            case BLUE: return RevBlinkinLedDriver.BlinkinPattern.BLUE;
            case RED: return RevBlinkinLedDriver.BlinkinPattern.RED;
        }
        return RevBlinkinLedDriver.BlinkinPattern.WHITE;
    }
}
