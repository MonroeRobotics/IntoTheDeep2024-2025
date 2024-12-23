package org.firstinspires.ftc.teamcode.archive.oldDrives;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.driveClasses.MecanumDrive;

@TeleOp
public class beutifulDrive extends OpMode {

    Gamepad ps5 = new Gamepad();
    MecanumDrive driveMecanum;
    Vector2d position = new Vector2d(100,100);
    Pose2d pose = new Pose2d(position, 90);
    double driveMax = .8;
    double xSpeed;
    double ySpeed;
    double headingSpeed;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        driveMecanum = new MecanumDrive(hardwareMap, pose);
        ps5 = gamepad1;
    }

    @Override
    public void loop() {
        xSpeed = -ps5.left_stick_y;
        ySpeed = -ps5.left_stick_x;
        headingSpeed = -ps5.right_stick_x;
        xSpeed *= driveMax;
        ySpeed *= driveMax;
        headingSpeed *= driveMax;
        Vector2d ps5Input = new Vector2d(xSpeed, ySpeed);
        PoseVelocity2d ps5Velocity = new PoseVelocity2d(ps5Input, headingSpeed);
        driveMecanum.setDrivePowers(ps5Velocity);

    }
}
