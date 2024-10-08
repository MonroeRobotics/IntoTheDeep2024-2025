package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="drive", group="main")
public class drive extends OpMode{




    Gamepad currentGamepad1;
    Gamepad previousGamepad1;

    Gamepad currentGamepad2;
    Gamepad previousGamepad2;

    CRServo intake0;
    CRServo intake1;
    Servo intakeExtension;
    Servo armAngleL;
    Servo armAngleR;
    Servo claw;
    DcMotor leftSlide;
    DcMotor rightSlide;


    //MecanumDrive drive;
    Vector2d position = new Vector2d(0,0);
    Pose2d pose = new Pose2d(position, 90);

    double drivePower =.8;
    double xPower;
    double yPower;
    double headingPower;

    double intakeExtensionTarget;
    int leftSlideTarget;
    int rightSlideTarget;

    double highBucketHeight;
    //cameraThing cameraThing;
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        //drive = new MecanumDrive(hardwareMap,pose);

        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();
        currentGamepad2 = new Gamepad();
        previousGamepad2 = new Gamepad();

        currentGamepad1 = gamepad1;
        previousGamepad1 = gamepad1;
        currentGamepad2 = gamepad2;
        previousGamepad2 = gamepad2;

        //cameraThing = new cameraThing(hardwareMap);
        //cameraThing.initCam();
        intakeExtension = hardwareMap.get(Servo.class, "intakeExtension");
        intake0 = hardwareMap.get(CRServo.class, "intake0");
        intake1 = hardwareMap.get(CRServo.class, "intake1");

        leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");

        armAngleL = hardwareMap.get(Servo.class, "armAngleL");
        armAngleR = hardwareMap.get(Servo.class, "armAngleR");
        claw = hardwareMap.get(Servo.class, "claw");

        leftSlide.setDirection(DcMotorSimple.Direction.REVERSE);

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlide.setPower(drivePower);
        rightSlide.setPower(drivePower);

        intakeExtension.setPosition(.0);
        leftSlideTarget = leftSlide.getCurrentPosition();
        rightSlideTarget = rightSlide.getCurrentPosition();
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


        intakeExtensionTarget = intakeExtension.getPosition();
        if (currentGamepad2.a && !previousGamepad2.a){
            intakeExtensionTarget +=.05;
            intakeExtension.setPosition(intakeExtensionTarget);
        }
        if (currentGamepad2.b && !previousGamepad2.b){
            intakeExtensionTarget -=.05;
            intakeExtension.setPosition(intakeExtensionTarget);
        }

        if (currentGamepad2.dpad_up){
            leftSlideTarget += 10;
            rightSlideTarget +=10;
        }
        if (currentGamepad2.dpad_down){
            leftSlideTarget -=10;
            rightSlideTarget -=10;
        }

        Vector2d gamepadInput = new Vector2d(xPower, yPower);
        PoseVelocity2d poseVelocity2d = new PoseVelocity2d(gamepadInput, headingPower);

        leftSlide.setTargetPosition(leftSlideTarget);
        rightSlide.setTargetPosition(rightSlideTarget);
        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //drive.setDrivePowers(poseVelocity2d);
        /*telemetry.addData("xPower", xPower);
        telemetry.addData("yPower", yPower);
        telemetry.addData("headingPower", headingPower);
        telemetry.addData("leftStick x", currentGamepad1.left_stick_x);
        telemetry.addData("lefStick y", currentGamepad1.left_stick_y);
        telemetry.addData("rightStick x", currentGamepad1.right_stick_x);*/
        telemetry.addData("intake servo pos", intakeExtension.getPosition());
        telemetry.addData("leftSlide Target", leftSlide.getTargetPosition());
        telemetry.addData("leftSlide", leftSlide.getCurrentPosition());
        telemetry.addData("rightSLide Target", rightSlide.getTargetPosition());
        telemetry.addData("rightSlide", rightSlide.getCurrentPosition());
        }
    }