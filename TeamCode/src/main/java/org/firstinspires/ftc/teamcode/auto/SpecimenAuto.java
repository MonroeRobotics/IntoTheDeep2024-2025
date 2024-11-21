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
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.driveClasses.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.ArmController;
import org.firstinspires.ftc.teamcode.util.AutoConfiguration;
import org.opencv.core.Mat;

@Config
@Autonomous
public class SpecimenAuto extends LinearOpMode {

    Pose2d startingDrivePose;
    Pose2d startingDrivePoseLeft = new Pose2d(16.58, 62.45, Math.toRadians(-90));
    Vector2d startingDrivePoseLeftAway = new Vector2d(16.58, 52.45);// -90
    Pose2d startingDrivePoseRight = new Pose2d(-16.58,62.45, Math.toRadians(-90));

    Vector2d blueSubmersible = new Vector2d(0,35); //90
    Vector2d wallApproach = new Vector2d(-48,50);
    Vector2d wallGrab = new Vector2d(-48, 57); //-90

    MecanumDrive drive;
    ArmController armController;
    AutoConfiguration autoConfiguration;

    enum autoState {
        START,
        SUBMERSIBLE,
        PLACE,
        TO_WALL,
        GRAB,
        PARK,
        STOP
    }
    autoState queuedState = autoState.START;

    Gamepad currentGamepad1;
    Gamepad previousGamepad1;

    //region messy variable stuff
    double waitTimer;
    int cycleCount = 0;

    boolean startTimerStarted;
    boolean placeTimerStarted;
    boolean grabTimerStarted;
    //endregion

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
            drive = new MecanumDrive(hardwareMap,startingDrivePose);
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
                        queuedState = autoState.SUBMERSIBLE;
                    }
                    else {
                        armController.currentArmState = ArmController.ArmState.CLOSE_CLAW;
                        queuedState = autoState.START;
                    }
                    break;
                case SUBMERSIBLE:
                    Pose2d submersibleStart = drive.pose;
                    TrajectoryActionBuilder toSubmersible = drive.actionBuilder(submersibleStart)
                            .strafeToLinearHeading(blueSubmersible, Math.toRadians(90));
                    Action toSubmersibleAction = toSubmersible.build();
                    Actions.runBlocking(new SequentialAction(toSubmersibleAction));
                    placeTimerStarted = false;
                    queuedState = autoState.PLACE;
                    break;
                case PLACE:
                    armController.currentArmState = ArmController.ArmState.SPECIMEN_PLACE_SEQUENCE;
                    armController.updateArmState();
                    armController.updateArmABS();

                    if(placeTimerStarted = false) {
                        waitTimer = 500 + System.currentTimeMillis();
                        placeTimerStarted = true;
                    }

                    if ((armController.getSlideHeight() >= 360) && (armController.getSlideHeight() <= 370) && waitTimer <= System.currentTimeMillis()){
                        armController.currentArmState = ArmController.ArmState.OPEN_CLAW;
                        armController.updateArmState();
                        armController.updateArmABS();
                        cycleCount +=1;
                        queuedState = autoState.TO_WALL;
                    }
                    else {
                        queuedState = autoState.PLACE;
                    }
                    break;
                case TO_WALL:
                    if (cycleCount < 3) {
                        armController.currentArmState = ArmController.ArmState.SPECIMEN_PICK_UP;
                        Pose2d toWallStart = drive.pose;
                        TrajectoryActionBuilder toWall = drive.actionBuilder(toWallStart)
                                .strafeToLinearHeading(wallApproach, Math.toRadians(-90))
                                .strafeToLinearHeading(wallGrab, Math.toRadians(-90));
                        Action toWallAction = toWall.build();
                        Actions.runBlocking(new SequentialAction(toWallAction));
                        grabTimerStarted = false;
                        queuedState = autoState.GRAB;
                    }
                    else{
                        queuedState = autoState.PARK;
                    }
                    break;
                case GRAB:
                    //if distance sensor <=.5, grab
                    armController.updateArmState();
                    armController.updateArmABS();

                    if(grabTimerStarted = false){
                        waitTimer = 500 + System.currentTimeMillis();
                    }

                    if (waitTimer <= System.currentTimeMillis()){
                        armController.currentArmState = ArmController.ArmState.CLOSE_CLAW;
                        queuedState = autoState.SUBMERSIBLE;
                    }
                    else{
                        queuedState = autoState.GRAB;
                    }
                    break;
                case PARK:

                    break;
                case STOP:

                    break;
            }

            armController.updateArmState();
            armController.updateArmABS();

            telemetry.addData("System Time", System.currentTimeMillis());
            telemetry.addData("Wait Timer", waitTimer);
            telemetry.addData("Queued State", queuedState);
            telemetry.addData("Current Pose", drive.pose);
            telemetry.update();

        }
    }
}
