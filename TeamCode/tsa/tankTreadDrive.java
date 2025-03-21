package org.firstinspires.ftc.teamcode.tsa;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp

public class tankTreadDrive extends OpMode {
    double leftDrivePower;
    double rightDrivePower;
    double drivePower = 0.8;
    Gamepad gamepad;
    DcMotorEx leftDriveMotor;
    DcMotorEx rightDriveMotor;

    @Override
    public void init() {
        gamepad = new Gamepad();
        leftDriveMotor = hardwareMap.get(DcMotorEx.class, "leftDriveMotor");
        rightDriveMotor = hardwareMap.get(DcMotorEx.class, "rightDriveMotor");

        leftDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDriveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftDriveMotor.setPower(drivePower);
        rightDriveMotor.setPower(drivePower);
    }

    @Override
    public void loop() {
        leftDrivePower = gamepad.left_stick_y + gamepad.left_stick_x;
        rightDrivePower = gamepad.left_stick_y + (-1 * gamepad.left_stick_x);
        leftDriveMotor.setPower(leftDrivePower);
        rightDriveMotor.setPower(rightDrivePower);
    }
}
