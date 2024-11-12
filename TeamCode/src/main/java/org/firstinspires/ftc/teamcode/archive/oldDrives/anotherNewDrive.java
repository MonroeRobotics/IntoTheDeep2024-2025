package org.firstinspires.ftc.teamcode.archive.oldDrives;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.driveClasses.MecanumDrive;

public class anotherNewDrive extends OpMode {

Gamepad ps = gamepad1;
MecanumDrive mecanumDrive;

Vector2d position = new Vector2d(100, 100);
Pose2d pose = new Pose2d(position, 90);

double maxSpeed = 0.8;
double xSpeed;
double ySpeed;
double headingSpeed;


    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        mecanumDrive = new MecanumDrive(hardwareMap, pose);
        ps = gamepad1;
    }

    @Override
    public void loop() {

        xSpeed = ps.left_stick_x;
        ySpeed = ps.left_stick_y;
        xSpeed *= maxSpeed;
        ySpeed *= maxSpeed;
        headingSpeed *= maxSpeed;
        Vector2d psInput = new Vector2d(xSpeed, ySpeed);
        PoseVelocity2d psVelocity = new PoseVelocity2d(psInput, headingSpeed);
        mecanumDrive.setDrivePowers(psVelocity);

    }
}
