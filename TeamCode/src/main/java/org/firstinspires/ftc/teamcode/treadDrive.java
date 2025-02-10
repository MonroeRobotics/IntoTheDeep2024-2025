package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.vision.cameraThing;

@TeleOp(name = "treadDrive", group = "Main")
public class treadDrive extends OpMode {
    double leftDrivePower;
    double rightDrivePower;
    double drivePower = 0.8;
    Gamepad currentGamepad1;
    Gamepad previousGamepad1;
    DcMotorEx leftDriveMotor;
    DcMotorEx rightDriveMotor;
    DcMotorEx armMotor;
    Servo servo;
    int armTarget;
    cameraThing camera;

    boolean timerStarted;
    double timer;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();
        leftDriveMotor = hardwareMap.get(DcMotorEx.class, "leftDriveMotor");
        rightDriveMotor = hardwareMap.get(DcMotorEx.class, "rightDriveMotor");
        servo = hardwareMap.get(Servo.class, "servo");

        armMotor = hardwareMap.get(DcMotorEx.class, "armMotor");
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(1.0);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDriveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDriveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightDriveMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        camera = new cameraThing(hardwareMap);
        camera.initCam();
    }

    @Override
    public void loop() {
        if (currentGamepad1.left_stick_y >= .1 || currentGamepad1.left_stick_y <= .1 || currentGamepad1.left_stick_x >= .1 || currentGamepad1.left_stick_x <= .1 || currentGamepad1.right_stick_y >= .1 || currentGamepad1.right_stick_y <= .1 || currentGamepad1.right_stick_x >= .1 || currentGamepad1.right_stick_x <= .1) {
            leftDrivePower = -currentGamepad1.left_stick_y + currentGamepad1.right_stick_x;
            rightDrivePower = -currentGamepad1.left_stick_y + (-currentGamepad1.right_stick_x);
        }
        leftDriveMotor.setPower(leftDrivePower);
        rightDriveMotor.setPower(rightDrivePower);

        if (currentGamepad1.right_trigger >= .1 && !timerStarted){
            armTarget += 10;
            timerStarted = true;
            timer = 50 + System.currentTimeMillis();
        }
        if (currentGamepad1.left_trigger >= .1 && !timerStarted){
            armTarget -= 10;
            timerStarted = true;
            timer = 50 + System.currentTimeMillis();
        }
        if (timer <= System.currentTimeMillis() && timerStarted){
            timerStarted = false;
        }
        armMotor.setTargetPosition(armTarget);

        if (currentGamepad1.a){
            servo.setPosition(0.0);
        }
        if (currentGamepad1.b){
            servo.setPosition(1.0);
        }

        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);

        //telemetry.addData("xpower", currentGamepad1.left_stick_x);
        //telemetry.addData("ypower", currentGamepad1.left_stick_y);
        telemetry.update();
    }
}
