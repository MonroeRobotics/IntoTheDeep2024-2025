package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
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

    int autoCycleCount = 0;

    MecanumDrive drive;
    ArmController armController;

    //region trajectory declerations
    Trajectory n0;
    //endregion

    Vector2d startingVector = new Vector2d(0,0);
    Pose2d startingPos = new Pose2d(startingVector, Math.toRadians(0));

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, startingPos);
        armController = new ArmController(hardwareMap);
    }
}
