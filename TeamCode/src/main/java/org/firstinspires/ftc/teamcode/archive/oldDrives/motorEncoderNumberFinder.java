package org.firstinspires.ftc.teamcode.archive.oldDrives;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

public class motorEncoderNumberFinder extends OpMode {
    double encoderValue0;
    double encoderValue1;
    DcMotor motor0;
    DcMotor motor1;
    Gamepad gamepad;
    double drivePower = .2;
    @Override
    public void init() {
        gamepad = gamepad1;
        motor0 = hardwareMap.get(DcMotorEx.class, "motor0");
        motor1 = hardwareMap.get(DcMotorEx.class, "motor1");
        motor1.setDirection(DcMotorSimple.Direction.REVERSE); //change if needed
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {
        if (gamepad.left_trigger >.1 && gamepad.right_trigger >.1){
            motor0.setTargetPosition(0);
            motor1.setTargetPosition(0);
        }
        if (gamepad.dpad_up){
            motor0.setPower(drivePower);
            motor1.setPower(drivePower);
        }
        if (gamepad.dpad_down){
            motor0.setPower(-drivePower);
            motor1.setPower(-drivePower);
        }

        encoderValue0 = motor0.getCurrentPosition();
        encoderValue1 = motor1.getCurrentPosition();
        telemetry.addData("motor0 pos", encoderValue0);
        telemetry.addData("motor1 pos", encoderValue1);
    }
}
