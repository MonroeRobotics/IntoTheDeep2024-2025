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

import org.firstinspires.ftc.teamcode.driveClasses.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.ArmController;
import org.firstinspires.ftc.teamcode.util.AutoConfiguration;

@Autonomous(name = "Bucket Auto", group = "Main")
@Config
public class BucketAuto extends LinearOpMode {

    //region Dashboard Variables

    //region Auto Timer variables
    public  static double SPECIMEN_PLACEMENT_TIME;
    public static double BUCKET_DROP_TIME;
    public static double INTAKE_TIME;
    public static double HUMAN_PLAYER_WAIT_TIME;
    public static double ERROR_THRESHOLD = 10;

    public static double TO_BUCKET_TIME = 10000; //ms
    public static double TO_NEUTRAL_TIME = 6000; //ms
    public  static  double TO_SUBMERSIBLE_TIME;
    double waitTimer;
    boolean startTimerStarted = false;
    boolean lowerArmTimerStarted = false;
    boolean intakeTimerStarted;
    int cycleNumber;

    //endregion

    Pose2d startingDrivePose;
    Pose2d startingDrivePoseLeft = new Pose2d(16.58, 62.45, Math.toRadians(-90));
    Vector2d startingDrivePoseLeftAway = new Vector2d(16.58, 52.45);// -90
    Pose2d startingDrivePoseRight = new Pose2d(-16.58,62.45, Math.toRadians(-90));
    Vector2d neutralTarget;
    Vector2d approachTarget;
    double headingTarget;

    Vector2d blueSubmersible = new Vector2d(0,35);
    Vector2d blueBasket = new Vector2d(56,56); //220

    Vector2d blueNeutralSample1 = new Vector2d(48,43); //35, 33, -90
    double bNS1AH = -90;
    Vector2d blueNeutralSample2Approach = new Vector2d(45,39); //-90
    double bNS2AH = -90;
    Vector2d blueNeutralSample2 = new Vector2d(45,33); //-90
    Vector2d blueNeutralSample3Approach = new Vector2d(57,26); //0
    double bNS3AH = 0;
    Vector2d blueNeutralSample3 = new Vector2d(62,26); //0

    Vector2d wall = new Vector2d(-56, 56);

    int autoCycleCount = 0;

    MecanumDrive drive;
    ArmController armController;
    AutoConfiguration autoConfiguration;

    //region trajectory declerations
    TrajectoryActionBuilder toSubmersible;
    TrajectoryActionBuilder toNeutral;
    //endregion


    enum autoState {
        START,
        SUBMERSIBLE,
        //PLACE,
        //TO_WALL,
        TO_NEUTRAL,
        BUCKET,
        DROP,
        PARK,
        STOP
    }
    autoState queuedState = autoState.START;

    Gamepad currentGamepad1;
    Gamepad previousGamepad1;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        armController = new ArmController(hardwareMap);
        armController.initArm(true);


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
            Pose2d initialPosition = startingDrivePose;
            drive = new MecanumDrive(hardwareMap,initialPosition);
            /*
            Pose2d poseEstimate = drive.pose;
            TrajectoryActionBuilder blueNeutral1 = drive.actionBuilder(poseEstimate)
                    .splineTo(blueSubmersible, Math.toRadians(90));*/
        }

        while (opModeIsActive()){
            switch (queuedState){
                case START:
                    //add anything that affects which path you'll take
                    //Like scoring neutral samples or shoving specimens
                    /*
                    if (AutoConfiguration.bucketOnly){
                        queuedState = autoState.BUCKET;
                    }
                    else {
                        queuedState = autoState.SUBMERSIBLE;
                    }
                    */
                    if (!startTimerStarted){
                        waitTimer = System.currentTimeMillis() + 500;
                        startTimerStarted = true;
                    }
                    armController.currentArmState = ArmController.ArmState.CLOSE_CLAW;
                    if (waitTimer <= System.currentTimeMillis() && autoConfiguration.isBucketOnly()){
                        armController.currentArmState = ArmController.ArmState.TALL_BUCKET_READY;
                        queuedState = autoState.BUCKET;
                    }
                    /*else if (waitTimer <= System.currentTimeMillis() && !autoConfiguration.isBucketOnly()){
                        armController.currentArmState = ArmController.ArmState.HIGH_SPECIMEN_PLACE;
                        queuedState = autoState.SUBMERSIBLE;
                    }*/
                    else {
                        armController.currentArmState = ArmController.ArmState.CLOSE_CLAW;
                        queuedState = autoState.START;
                    }
                    break;
                case SUBMERSIBLE:
                    Pose2d submersibleStart = drive.pose;
                    TrajectoryActionBuilder subIntake = drive.actionBuilder(submersibleStart)
                            .strafeToLinearHeading(blueSubmersible, Math.toRadians(90));
                    Action subIntakeAction = subIntake.build();
                    Actions.runBlocking(new SequentialAction(subIntakeAction));
                    queuedState = autoState.BUCKET;
                    break;
                /*case PLACE:
                    armController.currentArmState = ArmController.ArmState.SPECIMEN_PLACE_SEQUENCE;
                    waitTimer = 500 + System.currentTimeMillis();
                    if ((armController.getSlideHeight() >= 360) && (armController.getSlideHeight() <= 370) && waitTimer <= System.currentTimeMillis()){
                        armController.currentArmState = ArmController.ArmState.OPEN_CLAW;
                        //queuedState = autoState.TO_NEUTRAL;
                    }
                    break;
                case TO_WALL:
                    armController.currentArmState = ArmController.ArmState.SPECIMEN_PICK_UP;
                    Pose2d toWallStart = drive.pose;
                    TrajectoryActionBuilder toWall = drive.actionBuilder(toWallStart)
                            .strafeToLinearHeading(wall, Math.toRadians(-90));
                    break;*/
                case TO_NEUTRAL:
                    armController.checkIntakeAngle();
                    armController.checkIntakeServoPower();
                    armController.updateArmState();
                    armController.updateArmABS();
                    Pose2d neutralStart = drive.pose;

                    if (!intakeTimerStarted){
                        waitTimer = 5000 + System.currentTimeMillis();
                        intakeTimerStarted = true;
                        armController.currentArmState = ArmController.ArmState.EXTEND;
                    }
                    if (cycleNumber == 1) {
                        neutralTarget = blueNeutralSample1;
                        headingTarget = bNS1AH;
                    }
                    /*else if (cycleNumber == 2){
                        neutralTarget = blueNeutralSample2;
                        approachTarget = blueNeutralSample2Approach;
                        headingTarget = bNS2AH;
                    }
                    else if (cycleNumber == 3){
                        neutralTarget = blueNeutralSample3;
                        approachTarget = blueNeutralSample3Approach;
                        headingTarget = bNS3AH;
                    }*/
                    //if (cycleNumber == 1){
                        toNeutral = drive.actionBuilder(neutralStart)
                                .strafeToLinearHeading(blueNeutralSample1, Math.toRadians(-90));
                    //}
                    /*else if (cycleNumber >1) {
                        toNeutral = drive.actionBuilder(neutralStart)
                                .strafeToLinearHeading(approachTarget, Math.toRadians(headingTarget))
                                .strafeToLinearHeading(neutralTarget, Math.toRadians(90));
                    }*/
                    Action toNeutralAction = toNeutral.build();
                    Actions.runBlocking(new SequentialAction(toNeutralAction));

                    if (waitTimer <= System.currentTimeMillis()) {
                        armController.currentArmState = ArmController.ArmState.RETRACT;
                        //queuedState = autoState.BUCKET;
                    }
                    break;
                case BUCKET:
                    if(System.currentTimeMillis() > waitTimer) {
                        armController.currentArmState = ArmController.ArmState.TALL_BUCKET_READY;
                        Pose2d bucketStart;
                        if (cycleNumber == 0) {
                            bucketStart = startingDrivePose;
                            TrajectoryActionBuilder toBucketStart = drive.actionBuilder(bucketStart)
                                    .strafeToLinearHeading(startingDrivePoseLeftAway, Math.toRadians(-90))
                                    .strafeToLinearHeading(blueBasket, Math.toRadians(225));
                            Action toBucketStartAction = toBucketStart.build();
                            Actions.runBlocking(new SequentialAction( toBucketStartAction));
                        } else {
                            bucketStart = drive.pose;
                            TrajectoryActionBuilder toBucket = drive.actionBuilder(bucketStart)
                                    .strafeToLinearHeading(blueBasket, Math.toRadians(225));
                            Action toBucketAction = toBucket.build();
                            Actions.runBlocking(new SequentialAction(toBucketAction));
                        }
                        cycleNumber += 1;
                        if (armController.getSlideHeight() >= 1830 && armController.getSlideHeight() <= 1840) {
                            queuedState = autoState.DROP;
                        }
                        break;
                    }
                case DROP:
                    armController.currentArmState = ArmController.ArmState.OPEN_CLAW;
                    if (!lowerArmTimerStarted){
                        waitTimer = System.currentTimeMillis() + 250;
                        lowerArmTimerStarted = true;
                    }
                    if (waitTimer <= System.currentTimeMillis()){
                        armController.currentArmState = ArmController.ArmState.RETRACT;
                        if (cycleNumber == AutoConfiguration.maxCycleCount){
                            queuedState = autoState.PARK;
                        }
                        else if (cycleNumber <= 2){
                            queuedState = autoState.TO_NEUTRAL;
                            intakeTimerStarted = false;
                        }
                        else{
                            //queuedState = autoState.SUBMERSIBLE;
                        }
                    }
                    else{
                        queuedState = autoState.DROP;
                    }
                    break;
                case PARK:
                    break;
                case STOP:
                    break;
            }
            telemetry.addData("System Time", System.currentTimeMillis());
            telemetry.addData("Wait Timer", waitTimer);
            telemetry.addData("Queued State", queuedState);
            telemetry.addData("Cycle Number", cycleNumber);
            telemetry.addData("Target Pose", neutralTarget);
            telemetry.addData("Current Pose", drive.pose);
            //drive.updatePoseEstimate();
            armController.updateArmState();
            armController.updateArmABS();

            telemetry.update();
        }
    }
}
