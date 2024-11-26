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
    double anotherTimer;
    boolean anotherTimerStarted = false;
    boolean startTimerStarted = false;
    boolean lowerArmTimerStarted = false;
    boolean intakeTimerStarted;
    boolean bucketTransitionTimerStarted;
    double bucketTransitionTimer;
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
    Vector2d blueBasket = new Vector2d(54,60); //225

    Vector2d blueNeutralSample1 = new Vector2d(49,40); //35, 33, -90
    double bNS1AH = -90;
    Vector2d blueNeutralSample2Approach = new Vector2d(57,54); //-90
    double bNS2AH = -90;
    Vector2d blueNeutralSample2 = new Vector2d(56,40); //-90
    Vector2d blueNeutralSample3Approach = new Vector2d(62,26); //0
    double bNS3AH = 0;
    Vector2d blueNeutralSample3 = new Vector2d(62,26); //0

    Vector2d wall = new Vector2d(-56, 56);

    int autoCycleCount = 0;

    MecanumDrive drive;
    ArmController armController;
    AutoConfiguration autoConfiguration;

    //region trajectory declerations
    TrajectoryActionBuilder toSubmersible;
    TrajectoryActionBuilder toNeutral1;
    TrajectoryActionBuilder toNeutral2;
    TrajectoryActionBuilder toNeutral3;
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
                    if (waitTimer <= System.currentTimeMillis()){
                        armController.currentArmState = ArmController.ArmState.TALL_BUCKET_READY;
                        queuedState = autoState.BUCKET;
                    }
                    /*else if (waitTimer <= System.currentTimeMillis() && !autoConfiguration.isBucketOnly()){
                        armController.currentArmState = ArmController.ArmState.HIGH_SPECIMEN_PLACE;
                        queuedState = autoState.SUBMERSIBLE;
                    }*/
                    else {
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
                    Pose2d neutralStart = drive.pose;
                    if (cycleNumber == 1){
                        toNeutral1 = drive.actionBuilder(neutralStart)
                                .strafeToLinearHeading(new Vector2d(49, 48), Math.toRadians(-100))
                                .strafeToLinearHeading(blueNeutralSample1, Math.toRadians(-100));
                    }
                    else if (cycleNumber == 2) {
                        toNeutral2 = drive.actionBuilder(neutralStart)
                                .strafeToLinearHeading(blueNeutralSample2Approach, Math.toRadians(-100))
                                .strafeToLinearHeading(blueNeutralSample2, Math.toRadians(-100));
                    }
                    else if (cycleNumber == 3){
                        toNeutral3 = drive.actionBuilder(neutralStart)
                                .strafeToLinearHeading(new Vector2d( 59, 54), Math.toRadians(-90))
                                .strafeToLinearHeading(new Vector2d( 63, 40), Math.toRadians(-90));
                    }
                    if (!intakeTimerStarted){
                        waitTimer = 2000 + System.currentTimeMillis();
                        intakeTimerStarted = true;
                        anotherTimerStarted = false;
                        armController.currentArmState = ArmController.ArmState.EXTEND;
                        armController.checkIntakeAngle();
                        armController.checkIntakeServoPower();
                        armController.updateArmState();
                        armController.updateArmABS();
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
                        else {
                            queuedState = autoState.PARK;
                        }
                    }

                    Pose2d reverseStart = drive.pose;
                    /*TrajectoryActionBuilder reverse = drive.actionBuilder(reverseStart) //bug at cycle 3 run to last sample
                            .strafeToLinearHeading(new Vector2d( 48,48), Math.toRadians(-90));
                    Action reverseAction = reverse.build();*/

                    if (waitTimer <= System.currentTimeMillis()) {
                        armController.currentArmState = ArmController.ArmState.RETRACT;
                        armController.updateArmState();
                        armController.updateArmABS();
                        if(!anotherTimerStarted) {
                            anotherTimer = 500 + System.currentTimeMillis();
                            anotherTimerStarted = true;
                            //Actions.runBlocking(new SequentialAction(reverseAction));
                        }
                        if (anotherTimer <= System.currentTimeMillis()){
                            armController.currentArmState = ArmController.ArmState.CLOSE_CLAW;
                            armController.updateArmState();
                            armController.updateArmABS();

                            if(!bucketTransitionTimerStarted){
                                bucketTransitionTimer = 750 + System.currentTimeMillis();
                                bucketTransitionTimerStarted = true;
                            }
                            if(bucketTransitionTimer <= System.currentTimeMillis()){
                                queuedState = autoState.BUCKET;
                            }
                        }
                    }

                    break;
                case BUCKET:
                    armController.currentArmState = ArmController.ArmState.TALL_BUCKET_READY;
                    armController.updateArmState();
                    armController.updateArmABS();
                    Pose2d bucketStart;
                    if (cycleNumber == 0) {
                        bucketStart = startingDrivePose;
                        TrajectoryActionBuilder toBucketStart = drive.actionBuilder(bucketStart)
                                .strafeToLinearHeading(startingDrivePoseLeftAway, Math.toRadians(-90))
                                .strafeToLinearHeading(blueBasket, Math.toRadians(225));
                        Action toBucketStartAction = toBucketStart.build();
                        Actions.runBlocking(new SequentialAction( toBucketStartAction));
                    }
                    else if (cycleNumber == 1){
                        bucketStart = drive.pose;
                        TrajectoryActionBuilder toBucket = drive.actionBuilder(bucketStart)
                                .strafeToLinearHeading(new Vector2d(56, 48), Math.toRadians(225))
                                .strafeToLinearHeading(new Vector2d(58, 56), Math.toRadians(225));
                        Action toBucketAction = toBucket.build();
                        Actions.runBlocking(new SequentialAction(toBucketAction));
                    }
                    else if (cycleNumber == 2){
                        bucketStart = drive.pose;
                        TrajectoryActionBuilder toBucket = drive.actionBuilder(bucketStart)
                                .strafeToLinearHeading(new Vector2d(61, 50), Math.toRadians(225+30))
                                .strafeToLinearHeading(new Vector2d(61, 56), Math.toRadians(225+30));
                        Action toBucketAction = toBucket.build();
                        Actions.runBlocking(new SequentialAction(toBucketAction));
                    }
                    else if (cycleNumber == 3){
                        bucketStart = drive.pose;
                        TrajectoryActionBuilder toBucket = drive.actionBuilder(bucketStart)
                                .strafeToLinearHeading(new Vector2d(56, 48), Math.toRadians(225+20))
                                .strafeToLinearHeading(new Vector2d(60, 54), Math.toRadians(225+20));
                        Action toBucketAction = toBucket.build();
                        Actions.runBlocking(new SequentialAction(toBucketAction));
                    }
                    cycleNumber += 1;
                    if (armController.getSlideHeight() >= 1830 && armController.getSlideHeight() <= 1840) {
                        lowerArmTimerStarted = false;
                        intakeTimerStarted = false;
                        anotherTimerStarted = false;
                        queuedState = autoState.DROP;
                    }
                    bucketTransitionTimerStarted = false;
                    break;
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
                        else if (cycleNumber <= 4){
                            intakeTimerStarted = false;
                            anotherTimerStarted = false;
                            queuedState = autoState.TO_NEUTRAL;
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
                    armController.currentArmState = ArmController.ArmState.RETRACT;
                    armController.updateArmState();
                    armController.updateArmABS();
                    break;
                case STOP:
                    break;
            }
            telemetry.addData("System Time", System.currentTimeMillis());
            telemetry.addData("Wait Timer", waitTimer);
            telemetry.addData("another timer", anotherTimerStarted);
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
