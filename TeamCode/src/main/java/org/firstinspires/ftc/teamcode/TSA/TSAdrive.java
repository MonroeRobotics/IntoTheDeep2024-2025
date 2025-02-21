package org.firstinspires.ftc.teamcode.TSA;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.vision.cameraThing;
import org.firstinspires.ftc.teamcode.TSA.TSAarmController;

@TeleOp
public class TSAdrive extends OpMode {
    TSAarmController armController;
    double leftDrivePower;
    double rightDrivePower;
    double drivePower = 0.8;
    Gamepad currentGamepad1;
    Gamepad previousGamepad1;
    DcMotorEx leftDriveMotor;
    DcMotorEx rightDriveMotor;

    cameraThing camera;

    @Override
    public void init() {
        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();

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
        if (currentGamepad1.left_stick_y >= .1 || currentGamepad1.left_stick_y <= .1 || currentGamepad1.left_stick_x >= .1 || currentGamepad1.left_stick_x <= .1 || currentGamepad1.right_stick_y >= .1 || currentGamepad1.right_stick_y <= .1 || currentGamepad1.right_stick_x >= .1 || currentGamepad1.right_stick_x <= .1) {
            leftDrivePower = -currentGamepad1.left_stick_y + currentGamepad1.right_stick_x;
            rightDrivePower = -currentGamepad1.left_stick_y + (-currentGamepad1.right_stick_x);
        }
        leftDriveMotor.setPower(leftDrivePower);
        rightDriveMotor.setPower(rightDrivePower);

        if (currentGamepad1.x || !previousGamepad1.x){
            armController.currentArmstate = TSAarmController.ArmState.closeClaw;
        }
        if (currentGamepad1.a || !previousGamepad1.a){
            armController.currentArmstate = TSAarmController.ArmState.openClaw;
        }

        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);

        armController.updateArmState();
        telemetry.addData("armState", armController.currentArmstate);
    }
}

