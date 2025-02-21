package org.firstinspires.ftc.teamcode.tsa;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.vision.cameraThing;

@TeleOp

public class TSAdrive extends OpMode {
    TSAarmController armController;
    double leftDrivePower;
    double rightDrivePower;
    double drivePower = 0.8;
    Gamepad gamepad;
    Gamepad previousGamepad;
    DcMotorEx leftDriveMotor;
    DcMotorEx rightDriveMotor;

    cameraThing camera;

    @Override
    public void init() {
        gamepad = new Gamepad();
        previousGamepad = new Gamepad();

        leftDriveMotor = hardwareMap.get(DcMotorEx.class, "leftDriveMotor");
        rightDriveMotor = hardwareMap.get(DcMotorEx.class, "rightDriveMotor");

        leftDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDriveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armController = new TSAarmController(hardwareMap);
        armController.initArm();

        /*leftDriveMotor.setPower(drivePower);
        rightDriveMotor.setPower(drivePower);*/

        camera = new cameraThing(hardwareMap);
        camera.initCam();
    }

    @Override
    public void loop() {
        leftDrivePower = gamepad.left_stick_y + gamepad.left_stick_x;
        rightDrivePower = gamepad.left_stick_y + (-1 * gamepad.left_stick_x);
        leftDriveMotor.setPower(leftDrivePower);
        rightDriveMotor.setPower(rightDrivePower);
        if (gamepad.x || !previousGamepad.x){
            armController.currentArmstate = TSAarmController.ArmState.closeClaw;
        }
        if (gamepad.a || !previousGamepad.a){
            armController.currentArmstate = TSAarmController.ArmState.openClaw;
        }
        armController.updateArmState();
    }
}

