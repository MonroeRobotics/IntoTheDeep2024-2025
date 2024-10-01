package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "CR Servo Rotate", group = "Main")
public class intakeTest extends OpMode {

    Gamepad gamepad;
    CRServo crServo;
    DcMotor motor;
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        crServo = hardwareMap.get(CRServo.class, "crServo");
        motor = hardwareMap.get(DcMotorEx.class, "motor");
        gamepad = gamepad1;
        crServo.setPower(0.0);
    }

    @Override
    public void loop() {
        if (gamepad.dpad_left){
            crServo.setPower(1.0);
        }
        if (gamepad.dpad_right){
            crServo.setPower(-1.0);
        }
        if (gamepad.left_bumper){
            motor.setPower(-.8);
        }
        if (gamepad.right_bumper){
            motor.setPower(.8);
        }

    }
}
