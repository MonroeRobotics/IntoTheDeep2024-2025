package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "CR Servo Rotate", group = "Main")
public class intakeTest extends OpMode {

    Gamepad gamepad;
    CRServo crServo;
    DcMotor motor;
    Servo clawServo;
    double servoIntake = -1.0;
    double servoOutake = 1.0;
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        crServo = hardwareMap.get(CRServo.class, "crServo");
        motor = hardwareMap.get(DcMotorEx.class, "motor");
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        gamepad = gamepad1;
        crServo.setPower(0.0);
    }

    @Override
    public void loop() {
        if (gamepad.right_bumper) {
            motor.setPower(.8);
            crServo.setPower(servoIntake);
        }
        if (gamepad.left_bumper){
            motor.setPower(-.80);
        }
        if (gamepad.left_trigger > .1){
            motor.setPower(-.80);
            crServo.setPower(servoOutake);
        }
        if (gamepad.a){
            clawServo.setPosition(.4);
        }
        if (gamepad.b){
            clawServo.setPosition(.7);
        }

    }
}
