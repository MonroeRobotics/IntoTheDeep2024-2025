package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.driveClasses.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.ArmController;

@Autonomous(name = "Red Auto", group = "Main")
@Config
public class redAuto extends LinearOpMode {

    //region Dashboard Variables

    //region Auto Timer variables
    public  static double SPECIMEN_PLACEMENT_TIME;
    public static double BUCKET_DROP_TIME;
    public static double INTAKE_TIME;
    public static double HUMAN_PLAYER_WAIT_TIME;
    public static double ERROR_THRESHOLD = 10;
    double waitTimer;

    //endregion

    Pose2d blueSubmersible = new Pose2d(0,35, Math.toRadians(90));
    Pose2d blueBasket = new Pose2d(53,53,Math.toRadians(220));

    Pose2d blueNeutralSample1 = new Pose2d(48,33,Math.toRadians(-90));
    Pose2d blueNeutralSample2Approach = new Pose2d(58,39,Math.toRadians(-90));
    Pose2d blueNeutralSample2 = new Pose2d(58,33,Math.toRadians(-90));
    Pose2d blueNeutralSample3Approach = new Pose2d(57,26,Math.toRadians(0));
    Pose2d blueNeutralSample3 = new Pose2d(62,26,Math.toRadians(0));

    int autoCycleCount = 0;

    MecanumDrive drive;
    ArmController armController;

    //region trajectory declerations
    TrajectoryActionBuilder toSubmersible;
    TrajectoryActionBuilder toNeutralBlue1;
    TrajectoryActionBuilder toNeutralBlue2;
    TrajectoryActionBuilder toNeutralBlue3;
    //endregion

    Vector2d startingVector = new Vector2d(0,0);
    Pose2d startingPos = new Pose2d(startingVector, Math.toRadians(0));

    public class armControllerStuff{
        public Action specimenPlace(){
            armController.currentArmState = ArmController.ArmState.SPECIMEN_PLACE_SEQUENCE;
            return null;
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, startingPos);
        armController = new ArmController(hardwareMap);

        Pose2d initialPosition = new Pose2d(35,58, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap,initialPosition);

        armController = new ArmController(hardwareMap);

        toSubmersible = drive.actionBuilder(initialPosition)
                .splineToLinearHeading(blueSubmersible, Math.toRadians(90))
                ;
    }
}
