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

@TeleOp(name="drive", group="main")
public class drive extends OpMode{

    Gamepad currentGamepad1 = new Gamepad();
    Gamepad previousGamepad1 = new Gamepad();
    DcMotor fLeft;
    DcMotor fRight;
    DcMotor bLeft;
    DcMotor bRight;
    MecanumDrive drive;
    Vector2d position = new Vector2d(0,0);
    Pose2d pose = new Pose2d(position, 90);
    Vector2d gamepadInput = new Vector2d(currentGamepad1.left_stick_x, currentGamepad1.left_stick_y);
    PoseVelocity2d poseVelocity2d = new PoseVelocity2d(gamepadInput, currentGamepad1.right_stick_x);

    double drivePower =.8;
    double xPower;
    double yPower;
    double headingPower;
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        drive = new MecanumDrive(hardwareMap,pose);

        currentGamepad1 = gamepad1;
        previousGamepad1 = gamepad1;
    }

    @Override
    public void loop() {
        xPower = -currentGamepad1.left_stick_y;
        yPower = -currentGamepad1.left_stick_x;
        headingPower = -currentGamepad1.right_stick_x;

        xPower *= drivePower;
        yPower *= drivePower;
        headingPower *= drivePower;


        if (currentGamepad1.dpad_up) {
            xPower = drivePower;
            yPower = 0;
            headingPower = 0;
        } else if (currentGamepad1.dpad_down) {
            xPower = -drivePower;
            yPower = 0;
            headingPower = 0;
        } else if (currentGamepad1.dpad_left) {
            xPower = 0;
            yPower = -drivePower;
            headingPower = 0;
        } else if (currentGamepad1.dpad_right) {
            xPower = 0;
            yPower = drivePower;
            headingPower = 0;
        }

        drive.setDrivePowers(poseVelocity2d);
        telemetry.addData("xPower", xPower);
        telemetry.addData("yPower", yPower);
        telemetry.addData("headingPower", headingPower);
        }
    }