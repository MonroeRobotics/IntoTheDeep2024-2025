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
    double clawTimer;
    boolean clawTimerStarted = false;
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
    Vector2d blueBasket = new Vector2d(61,55); //225

    Vector2d blueNeutralSample1 = new Vector2d(51,40); //35, 33, -90
    double bNS1AH = -90;
    Vector2d blueNeutralSample2Approach = new Vector2d(57,54); //-90
    double bNS2AH = -90;
    Vector2d blueNeutralSample2 = new Vector2d(62,40); //-90
    Vector2d blueNeutralSample3Approach = new Vector2d(62,26); //0
    double bNS3AH = 0;
    Vector2d blueNeutralSample3 = new Vector2d(62,26); //0

    Vector2d wall = new Vector2d(-56, 56);

    int autoCycleCount = 0;

    PinpointDrive drive;
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
            drive = new PinpointDrive(hardwareMap,initialPosition);
            /*
            Pose2d poseEstimate = drive.pose;
            TrajectoryActionBuilder blueNeutral1 = drive.actionBuilder(poseEstimate)
                    .splineTo(blueSubmersible, Math.toRadians(90));*/
        }

        while (opModeIsActive()){
            switch (queuedState){
                case START:
                    //Delay for closing claw and raising slides
                    if (!startTimerStarted){
                        waitTimer = System.currentTimeMillis() + 500;
                        startTimerStarted = true;
                    }
                    armController.currentArmState = ArmController.ArmState.CLOSE_CLAW;
                    if (waitTimer <= System.currentTimeMillis()){
                        armController.currentArmState = ArmController.ArmState.TALL_BUCKET_READY;
                        queuedState = autoState.BUCKET;
                    }
                    //maintains loop, unnecessary
                    else {
                        queuedState = autoState.START;
                    }
                    break;
                case SUBMERSIBLE:
                    //Likely will be unreliable, don't count on this for points
                    Pose2d submersibleStart = drive.pose;
                    TrajectoryActionBuilder subIntake = drive.actionBuilder(submersibleStart)
                            .strafeToLinearHeading(blueSubmersible, Math.toRadians(90));
                    Action subIntakeAction = subIntake.build();
                    Actions.runBlocking(new SequentialAction(subIntakeAction));
                    queuedState = autoState.BUCKET;
                    break;
                case TO_NEUTRAL:
                    Pose2d neutralStart = drive.pose;

                    //build trajectory for correct sample based off cycle number
                    if (cycleNumber == 1){
                        toNeutral1 = drive.actionBuilder(neutralStart)
                                .strafeToLinearHeading(new Vector2d(51, 48), Math.toRadians(-100))
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

                    //Extend intake and move to sample position
                    if (!intakeTimerStarted){
                        waitTimer = 2000 + System.currentTimeMillis();
                        intakeTimerStarted = true;
                        armController.currentArmState = ArmController.ArmState.EXTEND;
                        armController.checkIntakeAngle();
                        armController.checkIntakeServoPower();
                        armController.updateArmState();
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

                    //Timed sequence for retracting, closing claw, and raising bucket
                    if (waitTimer <= System.currentTimeMillis()) {
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
                                queuedState = autoState.BUCKET;
                            }
                        }
                    }
                    break;
                case BUCKET:
                    armController.currentArmState = ArmController.ArmState.TALL_BUCKET_READY;
                    armController.updateArmState();
                    Pose2d bucketStart;

                    //Trajectories based off cycle number, only reason for extras after 1 is because poor tuning
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
                                .strafeToLinearHeading(new Vector2d(62, 56), Math.toRadians(225));
                        Action toBucketAction = toBucket.build();
                        Actions.runBlocking(new SequentialAction(toBucketAction));
                    }
                    else if (cycleNumber == 2){
                        bucketStart = drive.pose;
                        TrajectoryActionBuilder toBucket = drive.actionBuilder(bucketStart)
                                .strafeToLinearHeading(new Vector2d(61, 48), Math.toRadians(225-10))
                                .strafeToLinearHeading(new Vector2d(61, 56), Math.toRadians(225-10));
                        Action toBucketAction = toBucket.build();
                        Actions.runBlocking(new SequentialAction(toBucketAction));
                    }
                    else if (cycleNumber == 3){
                        bucketStart = drive.pose;
                        TrajectoryActionBuilder toBucket = drive.actionBuilder(bucketStart)
                                .strafeToLinearHeading(new Vector2d(56, 48), Math.toRadians(225-10))
                                .strafeToLinearHeading(new Vector2d(62, 56), Math.toRadians(225-10));
                        Action toBucketAction = toBucket.build();
                        Actions.runBlocking(new SequentialAction(toBucketAction));
                    }
                    cycleNumber += 1; //increase cycle number every time something is dropped, should be moved to drop section potentially

                    //drops sample when slide height reaches the correct position
                    if (armController.getSlideHeight() >= 1830 && armController.getSlideHeight() <= 1840) {
                        lowerArmTimerStarted = false;
                        queuedState = autoState.DROP;
                    }

                    //resets timers for TO_NEUTRAL, should be moved to drop
                    intakeTimerStarted = false;
                    clawTimerStarted = false;
                    bucketTransitionTimerStarted = false;
                    break;
                case DROP:
                    armController.currentArmState = ArmController.ArmState.OPEN_CLAW;

                    //Timer for lowering arm and switching to the next state
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
                            queuedState = autoState.TO_NEUTRAL;
                            intakeTimerStarted = false;
                        }
                        else{
                            //queuedState = autoState.SUBMERSIBLE; will likely be unreliable
                        }
                    }
                    else{
                        //Loops this case, unnecessary if I change cases once something is true
                        queuedState = autoState.DROP;
                    }
                    break;
                case PARK:
                    //Still need park position but this just prepares bot for teleop with the slides lowered
                    armController.currentArmState = ArmController.ArmState.RETRACT;
                    armController.updateArmState();
                    break;
                case STOP:
                    //Nothing for here yet, might just remove it
                    break;
            }
            telemetry.addData("System Time", System.currentTimeMillis());
            telemetry.addData("Wait Timer", waitTimer);
            telemetry.addData("Queued State", queuedState);
            telemetry.addData("Cycle Number", cycleNumber);
            telemetry.addData("Target Pose", neutralTarget);
            telemetry.addData("Current Pose", drive.pose);
            telemetry.update();

            armController.updateArmState();
        }
    }
}
