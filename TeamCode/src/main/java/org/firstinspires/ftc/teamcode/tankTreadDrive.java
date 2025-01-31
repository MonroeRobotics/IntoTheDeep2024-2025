package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "treadDrive", group = "Main")
public class tankTreadDrive extends OpMode {
    double leftDrivePower;
    double rightDrivePower;
    double drivePower = 0.8;
    Gamepad currentGamepad1;
    DcMotorEx leftDriveMotor;
    DcMotorEx rightDriveMotor;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        currentGamepad1 = new Gamepad();
        leftDriveMotor = hardwareMap.get(DcMotorEx.class, "leftDriveMotor");
        rightDriveMotor = hardwareMap.get(DcMotorEx.class, "rightDriveMotor");

        leftDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDriveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDriveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightDriveMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        if (currentGamepad1.left_stick_y >= .1 || currentGamepad1.left_stick_y <= .1 || currentGamepad1.left_stick_x >= .1 || currentGamepad1.left_stick_x <= .1 || currentGamepad1.right_stick_y >= .1 || currentGamepad1.right_stick_y <= .1 || currentGamepad1.right_stick_x >= .1 || currentGamepad1.right_stick_x <= .1) {
            leftDrivePower = -currentGamepad1.left_stick_y + currentGamepad1.right_stick_x;
            rightDrivePower = -currentGamepad1.left_stick_y + (-currentGamepad1.right_stick_x);
        }
        leftDriveMotor.setPower(leftDrivePower);
        rightDriveMotor.setPower(rightDrivePower);

        currentGamepad1.copy(gamepad1);

        telemetry.addData("xpower", currentGamepad1.left_stick_x);
        telemetry.addData("ypower", currentGamepad1.left_stick_y);
        telemetry.update();
    }
}
