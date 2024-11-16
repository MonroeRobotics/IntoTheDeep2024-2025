package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.driveClasses.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.ArmController;
import org.firstinspires.ftc.teamcode.util.AutoConfiguration;

@Autonomous(name = "Blue Auto", group = "Main")
@Config
public class blueAuto extends LinearOpMode {

    //region Dashboard Variables

    //region Auto Timer variables
    public  static double SPECIMEN_PLACEMENT_TIME;
    public static double BUCKET_DROP_TIME;
    public static double INTAKE_TIME;
    public static double HUMAN_PLAYER_WAIT_TIME;
    public static double ERROR_THRESHOLD = 10;
    double waitTimer;

    //endregion

    Pose2d startingDrivePose;
    Pose2d startingDrivePoseLeft = new Pose2d(16.58, 62.45, Math.toRadians(-90));
    Pose2d startingDrivePoseRight = new Pose2d(-16.58,62.45, Math.toRadians(-90));
    Vector2d blueSubmersible = new Vector2d(0,35);
    Vector2d blueBasket = new Vector2d(53,53); //220

    Vector2d blueNeutralSample1 = new Vector2d(48,33); //-90
    Vector2d blueNeutralSample2Approach = new Vector2d(58,39); //-90
    Vector2d blueNeutralSample2 = new Vector2d(58,33); //-90
    Vector2d blueNeutralSample3Approach = new Vector2d(57,26); //0
    Vector2d blueNeutralSample3 = new Vector2d(62,26); //0

    int autoCycleCount = 0;

    MecanumDrive drive;
    ArmController armController;
    AutoConfiguration autoConfiguration;

    //region trajectory declerations
    TrajectoryActionBuilder toSubmersible;
    TrajectoryActionBuilder toNeutralBlue1;
    TrajectoryActionBuilder toNeutralBlue2;
    TrajectoryActionBuilder toNeutralBlue3;
    //endregion


    enum autoState {
        START,
        SUBMERSIBLE,
        PLACE,
        NEUTRAL1,
        NEUTRAL2,
        NEUTRAL3,
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
        drive = new MecanumDrive(hardwareMap, startingDrivePose);

        armController = new ArmController(hardwareMap);
        armController.initArm();

        Pose2d initialPosition = new Pose2d(35,58, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap,initialPosition);

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
                    queuedState = autoState.PLACE;
                    break;
                case SUBMERSIBLE:
                    TrajectoryActionBuilder specimenPlace = drive.actionBuilder(startingDrivePose)
                            .strafeToLinearHeading(blueSubmersible, Math.toRadians(90));
                    specimenPlace.build();
                    //queuedState = autoState.NEUTRAL1;
                    break;
                case NEUTRAL1:
                    Pose2d locationEstimation0 = drive.pose;
                    TrajectoryActionBuilder toNeutral1 = drive.actionBuilder(locationEstimation0)
                            .strafeToLinearHeading(new Vector2d(0, 26), Math.toRadians(90))
                            .splineTo(blueNeutralSample1, Math.toRadians(-90));
                    toNeutral1.build();
                    armController.currentArmState = ArmController.ArmState.POINT_BLANK_INTAKE;
                    queuedState = autoState.BUCKET;
                    break;
                case BUCKET:
                    armController.currentArmState = ArmController.ArmState.TALL_BUCKET_READY;
                    Pose2d locationEstimation1 = drive.pose;
                    TrajectoryActionBuilder toBucket = drive.actionBuilder(locationEstimation1)
                            .strafeToLinearHeading(blueBasket, Math.toRadians(225));
                    toBucket.build();
                    queuedState = autoState.NEUTRAL2;
                    break;
                case NEUTRAL2:
                    break;
            }
            telemetry.update();
            drive.updatePoseEstimate();
            armController.updateArmState();
            armController.updateArmABS();
            armController.checkIntakeAngle();
            armController.checkIntakeServoPower();
        }
    }
}
