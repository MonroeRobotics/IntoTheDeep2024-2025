package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class AutoConfiguration {

    public static boolean hasInitAuto = false;

    Telemetry telemetry;

    public enum ParkSide{
        CLOSE,
        FAR
    }
    public enum AllianceColor{
        RED,
        BLUE
    }

    public enum AdjVariables{
        START_POSITION,
        DELAY,
        SPECIMEN_ONLY,
        SUBMERSIBLE_INTAKE,
        CYCLE_COUNT,
        PARK_SIDE,
        ALLIANCE_YELLOW
    }

    public enum StartPosition{
        LEFT,
        RIGHT
    }
    public static boolean submersibleIntake = false;
    public static boolean specimenOnly = false;

    public static boolean allianceYellow = false;

    public static int delay = 0;
    public static int cycleCount = 0;

    String indicatorMarker = "֎";

    public static ParkSide parkSide = ParkSide.FAR;
    public static StartPosition startPosition = StartPosition.LEFT;
    public static AllianceColor allianceColor;

    AdjVariables[] adjVariables = AdjVariables.values();

    AdjVariables currentVariable = adjVariables[0];

    int currVIndex = 0;


    public AutoConfiguration(Telemetry telemetry, AllianceColor allianceColor){
        hasInitAuto = true;
        this.telemetry = telemetry;
        this.allianceColor = allianceColor;
    }
    public AutoConfiguration(Telemetry telemetry, AllianceColor allianceColor, ParkSide parkSide, StartPosition startPosition, int delay, boolean submersibleIntake, boolean specimenOnly){
        hasInitAuto = true;
        this.telemetry = telemetry;
        this.allianceColor = allianceColor;
        this.parkSide = parkSide;
        this.startPosition = startPosition;
        this.delay = delay;
        this.submersibleIntake = submersibleIntake;
        this.specimenOnly = specimenOnly;
    }

    public boolean isSubmersibleIntake() {
        return submersibleIntake;
    }

    public int getCycleCount() {
        return cycleCount;
    }

    public boolean isSpecimenOnly(){
        return specimenOnly;
    }

    public int getDelay() {
        return delay;
    }

    public ParkSide getParkSide() {
        return parkSide;
    }

    public StartPosition getStartPosition(){
        return startPosition;
    }

    public boolean getAllianceYellow(){return allianceYellow;}
    public void processInput(Gamepad currentGamepad, Gamepad previousGamepad){
        if(currentGamepad.dpad_down && !previousGamepad.dpad_down && currVIndex < adjVariables.length - 1){
            currVIndex += 1;
            currentVariable = adjVariables[currVIndex];
        }
        else if(currentGamepad.dpad_up && !previousGamepad.dpad_up && currVIndex > 0){
            currVIndex -= 1;
            currentVariable = adjVariables[currVIndex];
        }
        else if(currentGamepad.dpad_right && !previousGamepad.dpad_right) {
            switch (currentVariable) {
                case START_POSITION:
                    if(startPosition == StartPosition.LEFT){
                        startPosition = StartPosition.RIGHT;
                    }
                    else{
                        startPosition = StartPosition.LEFT;
                    }
                    break;
                case DELAY:
                    if(delay < 30){
                        delay ++;
                    }
                    break;
                case SPECIMEN_ONLY:
                    specimenOnly = !specimenOnly;
                    break;
                case SUBMERSIBLE_INTAKE:
                    submersibleIntake = !submersibleIntake;
                    break;
                case CYCLE_COUNT:
                    if(cycleCount < 5){
                        cycleCount ++;
                    }
                    break;
                case PARK_SIDE:
                    if(parkSide == ParkSide.FAR){
                        parkSide = ParkSide.CLOSE;
                    }
                    else{
                        parkSide = ParkSide.FAR;
                    }
                    break;
                case ALLIANCE_YELLOW:
                    allianceYellow = !allianceYellow;
                    break;
            }
        }
        else if(currentGamepad.dpad_left && !previousGamepad.dpad_left) {
            switch (currentVariable) {
                case START_POSITION:
                    if(startPosition == StartPosition.LEFT){
                        startPosition = StartPosition.RIGHT;
                    }
                    else{
                        startPosition = StartPosition.LEFT;
                    }
                    break;
                case DELAY:
                    if(delay > 0){
                        delay --;
                    }
                    break;
                case SPECIMEN_ONLY:
                    specimenOnly = !specimenOnly;
                    break;
                case SUBMERSIBLE_INTAKE:
                    submersibleIntake = !submersibleIntake;
                    break;
                case CYCLE_COUNT:
                    if(cycleCount > 0) {
                        cycleCount --;
                    }
                case PARK_SIDE:
                    if(parkSide == ParkSide.FAR){
                        parkSide = ParkSide.CLOSE;
                    }
                    else{
                        parkSide = ParkSide.FAR;
                    }
                    break;
                case ALLIANCE_YELLOW:
                    allianceYellow = !allianceYellow;
                    break;
            }
        }

        telemetry.addData("Current Color", allianceColor);
        telemetry.addData(((currentVariable == AdjVariables.START_POSITION ? indicatorMarker : "") + "Start Position"), startPosition);
        telemetry.addData(((currentVariable == AdjVariables.DELAY ? indicatorMarker : "") + "Delay"), delay);
        telemetry.addData(((currentVariable == AdjVariables.SPECIMEN_ONLY ? indicatorMarker : "") + "Purple Pixel Only"), specimenOnly);
        telemetry.addData(((currentVariable == AdjVariables.SUBMERSIBLE_INTAKE ? indicatorMarker : "") + "White Pixel"), submersibleIntake);
        telemetry.addData(((currentVariable == AdjVariables.CYCLE_COUNT ? indicatorMarker : "") + "Max Cycle Count"), cycleCount);
        telemetry.addData(((currentVariable == AdjVariables.PARK_SIDE ? indicatorMarker : "") + "Park Side"), parkSide);
        telemetry.addData(((currentVariable == AdjVariables.ALLIANCE_YELLOW ? indicatorMarker : "") + "ALLIANCE_YELLOW"), allianceYellow);
        telemetry.update();
    }
}