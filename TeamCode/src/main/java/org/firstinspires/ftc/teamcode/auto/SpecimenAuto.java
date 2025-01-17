package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.firstinspires.ftc.teamcode.util.ArmController;
import org.firstinspires.ftc.teamcode.util.AutoConfiguration;

@Config
@Autonomous
public class SpecimenAuto extends LinearOpMode {

    Pose2d startingDrivePose;
    Pose2d startingDrivePoseLeft = new Pose2d(16.58, 62.45, Math.toRadians(-90));
    Vector2d startingDrivePoseLeftAway = new Vector2d(16.58, 52.45);// -90
    Pose2d startingDrivePoseRight = new Pose2d(-16.58,63.45, Math.toRadians(-90));

    Vector2d blueSubmersible = new Vector2d(0,(24+9.5)); //90
    Vector2d wallApproach = new Vector2d(-48,50);
    Vector2d wallGrab = new Vector2d(-48, 57); //-90

    PinpointDrive drive;
    ArmController armController;
    AutoConfiguration autoConfiguration;
    TrajectoryActionBuilder toNeutral1;
    TrajectoryActionBuilder toNeutral2;
    TrajectoryActionBuilder toNeutral3;

    boolean intakeTimerStarted;
    boolean bucketTransitionTimerStarted;
    double bucketTransitionTimer;
    double clawTimer;
    boolean clawTimerStarted = false;

    double extendoTimer;
    boolean extended = false;

    enum autoState {
        START,
        SUBMERSIBLE,
        PLACE,
        TO_WALL,
        TO_NEUTRAL,
        WALL_DEPOSIT,
        GRAB,
        PARK,
        STOP
    }
    autoState queuedState = autoState.START;

    Gamepad currentGamepad1;
    Gamepad previousGamepad1;

    //region messy variable stuff
    double waitTimer;
    int cycleNumber = 0;
    int maxCycleCount;

    boolean startTimerStarted;
    boolean placeTimerStarted;
    boolean grabTimerStarted;
    boolean transitionTimerStarted;
    boolean anotherTimerStarted;
    double anotherTimer;
    //endregion

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        armController = new ArmController(hardwareMap);
        armController.initArm(false);
        armController.currentArmState = ArmController.ArmState.CLOSE_CLAW;


        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();
        currentGamepad1.copy(gamepad1);
        previousGamepad1.copy(currentGamepad1);

        autoConfiguration = new AutoConfiguration(telemetry, AutoConfiguration.AllianceColor.BLUE);
        autoConfiguration.processInput(currentGamepad1, previousGamepad1);

        while (opModeInInit()){
            autoConfiguration.processInput(currentGamepad1,previousGamepad1);

            previousGamepad1.copy(currentGamepad1);
            currentGamepad1.copy(gamepad1);

            if(autoConfiguration.getStartPosition() == AutoConfiguration.StartPosition.LEFT){
                startingDrivePose = startingDrivePoseLeft;
            }
            else{
                startingDrivePose = startingDrivePoseRight;
            }
            drive = new PinpointDrive(hardwareMap,startingDrivePoseRight);
            maxCycleCount = autoConfiguration.getCycleCount();
            maxCycleCount = 2;
        }

        while (opModeIsActive()){
            switch (queuedState){
                case START:
                    if (!startTimerStarted){
                        waitTimer = System.currentTimeMillis() + 500;
                        startTimerStarted = true;
                    }
                    armController.currentArmState = ArmController.ArmState.CLOSE_CLAW;
                    if (waitTimer <= System.currentTimeMillis() && autoConfiguration.isBucketOnly()){
                        armController.currentArmState = ArmController.ArmState.HIGH_SPECIMEN_PLACE;
                        armController.setIntakePos(.3);
                        queuedState = autoState.SUBMERSIBLE;
                    }
                    else {
                        queuedState = autoState.START;
                    }
                    break;
                case SUBMERSIBLE:
                    armController.currentArmState = ArmController.ArmState.HIGH_SPECIMEN_PLACE;
                    armController.updateArmState();
                    Pose2d submersibleStart = drive.pose;
                    TrajectoryActionBuilder toSubmersible = drive.actionBuilder(submersibleStart)
                            .strafeToLinearHeading(new Vector2d(12, 50), Math.toRadians(-90))
                            .strafeToLinearHeading(new Vector2d(12, (24+8.5)), Math.toRadians(-90));
                    Action toSubmersibleAction = toSubmersible.build();
                    Actions.runBlocking(new SequentialAction(toSubmersibleAction));
                    //drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0,0), 0));
                    placeTimerStarted = false;
                    queuedState = autoState.PLACE;
                    break;
                case PLACE:
                    armController.currentArmState = ArmController.ArmState.SPECIMEN_PLACE_SEQUENCE;
                    armController.updateArmState();

                    if(!placeTimerStarted) {
                        waitTimer = 750 + System.currentTimeMillis();
                        placeTimerStarted = true;
                    }

                    if ((armController.getSlideHeight() <= 710) && waitTimer <= System.currentTimeMillis()){
                        armController.currentArmState = ArmController.ArmState.OPEN_CLAW;
                        armController.updateArmState();
                        cycleNumber +=1;
                        transitionTimerStarted = false;
                        queuedState = autoState.TO_WALL;
                    }
                    else {
                        queuedState = autoState.PLACE;
                    }
                    break;
                case TO_WALL:
                    if (cycleNumber < maxCycleCount) {
                        Pose2d toWallStart = drive.pose;
                        TrajectoryActionBuilder toWall = drive.actionBuilder(toWallStart)
                                .strafeToLinearHeading(wallApproach, Math.toRadians(-90));
                        Action toWallAction = toWall.build();
                        Actions.runBlocking(new SequentialAction(toWallAction));
                        armController.currentArmState = ArmController.ArmState.SPECIMEN_PICK_UP;
                        armController.updateArmState();
                        grabTimerStarted = false;
                        queuedState = autoState.GRAB;
                    }
                    else if (cycleNumber < 5){
                        queuedState = autoState.TO_NEUTRAL;
                    }
                    else{
                        queuedState = autoState.PARK;
                    }
                    break;
                case TO_NEUTRAL:
                    Pose2d neutralStart = drive.pose;
                    //Extend intake and move to sample position
                    if (!intakeTimerStarted){
                        waitTimer = 2000 + System.currentTimeMillis();
                        extendoTimer = 500 + System.currentTimeMillis();
                        intakeTimerStarted = true;

                        //build trajectory for correct sample based off cycle number
                        if (cycleNumber == 1){
                            toNeutral1 = drive.actionBuilder(neutralStart)
                                    .strafeToLinearHeading(new Vector2d(-38, 58), Math.toRadians(-90))
                                    .strafeToLinearHeading(new Vector2d(-38,40), Math.toRadians(-90));
                        }
                        else if (cycleNumber == 2) {
                            toNeutral2 = drive.actionBuilder(neutralStart)
                                    .strafeToLinearHeading(new Vector2d(-54, 58), Math.toRadians(-90))
                                    .strafeToLinearHeading(new Vector2d(-54, 40), Math.toRadians(-90));
                        }
                        else if (cycleNumber == 3){
                            toNeutral3 = drive.actionBuilder(neutralStart)
                                    .strafeToLinearHeading(new Vector2d( -50, 58), Math.toRadians(-70))
                                    .strafeToLinearHeading(new Vector2d( -50, 40), Math.toRadians(-70));
                        }

                        if(cycleNumber == 1){
                            Action toNeutral1Action = toNeutral1.build();
                            Actions.runBlocking(new SequentialAction(toNeutral1Action));
                        }
                        else if (cycleNumber == 2){
                            Action toNeutral2Action = toNeutral2.build();
                            Actions.runBlocking(new SequentialAction(toNeutral2Action));
                        }
                        else if (cycleNumber == 3){
                            Action toNeutral3Action = toNeutral3.build();
                            Actions.runBlocking(new SequentialAction(toNeutral3Action));
                        }
                        /*else{go to submersible}*/
                    }

                    if (extendoTimer <= System.currentTimeMillis() && !extended){
                        armController.currentArmState = ArmController.ArmState.EXTEND;
                        armController.lowerIntake = true;
                        armController.checkIntakeAngle();
                        armController.checkIntakeServoPower();
                        armController.updateArmState();
                        extended = true;
                    }

                    //Timed sequence for retracting, closing claw, and raising bucket
                    if (waitTimer <= System.currentTimeMillis() /*|| armController.currentArmState == ArmController.ArmState.RETRACT*/) {
                        armController.currentArmState = ArmController.ArmState.RETRACT;
                        armController.updateArmState();
                        if(!clawTimerStarted) {
                            clawTimer = 500 + System.currentTimeMillis();
                            clawTimerStarted = true;
                            //Actions.runBlocking(new SequentialAction(reverseAction));
                        }
                        if (clawTimer <= System.currentTimeMillis()){
                            armController.currentArmState = ArmController.ArmState.CLOSE_CLAW;
                            armController.updateArmState();

                            if(!bucketTransitionTimerStarted){
                                bucketTransitionTimer = 750 + System.currentTimeMillis();
                                bucketTransitionTimerStarted = true;
                            }
                            if(bucketTransitionTimer <= System.currentTimeMillis()){
                                queuedState = autoState.WALL_DEPOSIT;
                            }
                        }
                    }
                    //else armController.updateIntake(); queuedState = autoState.TO_NEUTRAL;
                    break;
                case WALL_DEPOSIT:
                    Pose2d toWallStart = drive.pose;
                    TrajectoryActionBuilder toWall = drive.actionBuilder(toWallStart)
                            .strafeToLinearHeading(wallApproach, Math.toRadians(-90));
                    Action toWallAction = toWall.build();
                    Actions.runBlocking(new SequentialAction(toWallAction));
                    armController.currentArmState = ArmController.ArmState.SPECIMEN_PICK_UP;
                    armController.updateArmState();
                    armController.currentArmState = ArmController.ArmState.OPEN_CLAW;
                    armController.updateArmState();
                    cycleNumber += 1;
                    queuedState = autoState.TO_NEUTRAL;
                    break;
                case GRAB:
                    //if distance sensor <=.5, grab
                    armController.updateArmState();
                    Pose2d grabStart = drive.pose;
                    if(!grabTimerStarted){
                        TrajectoryActionBuilder grabWall = drive.actionBuilder(grabStart)
                                .strafeToLinearHeading(wallGrab, Math.toRadians(-90));
                        Action grabWallAction = grabWall.build();
                        Actions.runBlocking(new SequentialAction(grabWallAction));
                        waitTimer = 50 + System.currentTimeMillis();
                        grabTimerStarted = true;
                        anotherTimerStarted = false;
                    }
                    if(!anotherTimerStarted){
                        anotherTimer = 150 + System.currentTimeMillis();
                        anotherTimerStarted = true;
                    }
                    //drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0,0), 0));

                    if (waitTimer <= System.currentTimeMillis()){
                        armController.currentArmState = ArmController.ArmState.CLOSE_CLAW;
                    }
                    else{
                        queuedState = autoState.GRAB;
                    }

                    if (anotherTimer <= System.currentTimeMillis()){
                        queuedState = autoState.SUBMERSIBLE;
                    }
                    break;
                case PARK:

                    break;
                case STOP:

                    break;
            }

            armController.updateArmState();

            telemetry.addData("System Time", System.currentTimeMillis());
            telemetry.addData("Wait Timer", waitTimer);
            telemetry.addData("Queued State", queuedState);
            telemetry.addData("Current Pose", drive.pose);
            telemetry.update();

        }
    }
}
