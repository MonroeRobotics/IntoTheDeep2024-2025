package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="clawTest", group = "Main")
public class clawTest extends OpMode {

    Servo claw;
    Gamepad currentGamepad;
    Gamepad previousGamepad;
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        claw = hardwareMap.get(Servo.class, "driveAngle");
        //driveMotor = hardwareMap.get(DcMotor.class, "driveMotor");

        currentGamepad = gamepad1;
        previousGamepad = gamepad1;
        claw.setPosition(.65);
    }

    @Override
    public void loop() {

        if (currentGamepad.left_bumper){
            claw.setPosition(.7);
        }
        if (currentGamepad.right_bumper){
            claw.setPosition(.4);
        }

        //telemetry.addData("Motor Power", driveMotor.getPower());
        telemetry.addData("Status", "Running");
        telemetry.addData("clawAngle", claw.getPosition());
        telemetry.update();
    }
}