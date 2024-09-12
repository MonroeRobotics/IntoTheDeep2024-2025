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

import org.firstinspires.ftc.teamcode.driveClasses.MecanumDrive;

@TeleOp(name="TractionControl", group="main")
public class TractionControl extends OpMode{




    Gamepad currentGamepad1 = new Gamepad();
    Gamepad previousGamepad1 = new Gamepad();
    DcMotor fLeft;
    DcMotor fRight;
    DcMotor bLeft;
    DcMotor bRight;
    MecanumDrive drive;
    Vector2d position = new Vector2d(0,0);
    Pose2d pose = new Pose2d(position, 90);

    double drivePower =.8;
    double xPower;
    double yPower;
    double headingPower;
    org.firstinspires.ftc.teamcode.vision.cameraThing cameraThing;
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        drive = new MecanumDrive(hardwareMap,pose);

        currentGamepad1 = gamepad1;
        previousGamepad1 = gamepad1;

        /*fLeft = hardwareMap.get(DcMotorEx.class, "leftFront");
        fRight = hardwareMap.get(DcMotorEx.class, "rightFront");
        bLeft = hardwareMap.get(DcMotorEx.class, "leftBack");
        bRight = hardwareMap.get(DcMotorEx.class, "leftRight");*/

        //Copied from Ugly drive, still need to fix this
        /*cameraThing = new cameraThing(hardwareMap);
        cameraThing.initCam();*/
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
            yPower = drivePower;
            headingPower = 0;
        } else if (currentGamepad1.dpad_right) {
            xPower = 0;
            yPower = -drivePower;
            headingPower = 0;
        }

        if (currentGamepad1.right_bumper){
            drivePower = 1;
        } else if(currentGamepad1.left_bumper){
            drivePower = .4;
        } else{
            drivePower = .8;
        }

        Vector2d gamepadInput = new Vector2d(xPower, yPower);
        PoseVelocity2d poseVelocity2d = new PoseVelocity2d(gamepadInput, headingPower);

        drive.setDrivePowers(poseVelocity2d);
        telemetry.addData("xPower", xPower);
        telemetry.addData("yPower", yPower);
        telemetry.addData("headingPower", headingPower);
        telemetry.addData("leftStick x", currentGamepad1.left_stick_x);
        telemetry.addData("lefStick y", currentGamepad1.left_stick_y);
        telemetry.addData("rightStick x", currentGamepad1.right_stick_x);
    }
}