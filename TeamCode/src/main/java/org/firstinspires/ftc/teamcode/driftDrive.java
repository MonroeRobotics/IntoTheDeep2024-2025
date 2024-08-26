package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp
public class driftDrive extends OpMode {
    Gamepad gamepad = new Gamepad();
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    MecanumDrive drive;

    double xPower;
    double yPower;
    double heading;

    Vector2d position = new Vector2d(0, 0);
    Pose2d on = new Pose2d(position, 90);

    double maxSpeed = .6;
    double headingPower = .6;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        drive = new MecanumDrive(hardwareMap, on);
        gamepad = gamepad1;

    }

    @Override
    public void loop() {
        xPower = -gamepad.left_stick_y * maxSpeed;
        yPower = -gamepad.left_stick_x * maxSpeed;
        heading = -gamepad.right_stick_x * headingPower;

        Vector2d input = new Vector2d(xPower, yPower);
        PoseVelocity2d velocity = new PoseVelocity2d(input, heading);

        drive.setDrivePowers(velocity);

        maxSpeed = .6;

        headingPower = .6;

        if (gamepad.left_bumper) {
            maxSpeed = .3;
            headingPower = .3;
        }

        if (gamepad.right_bumper) {
            maxSpeed = .8;
            headingPower = .3;
        }

        /*if (gamepad.x) {
            maxSpeed = 0;
        }*/

        if (gamepad.right_trigger >= .1) {
            maxSpeed = 0.1;
        }

        if (gamepad.left_trigger >= .1) {
            headingPower = 1.0;
        }

        telemetry.addData("xPower", xPower);
        telemetry.addData("yPower", yPower);
        telemetry.addData("Turning Speed", headingPower);
        telemetry.addData("leftTrigger", gamepad.left_trigger);
        telemetry.update();

    }
}