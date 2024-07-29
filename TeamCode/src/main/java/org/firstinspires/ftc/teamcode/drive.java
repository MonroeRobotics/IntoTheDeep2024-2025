package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="drive", group="main")
public class drive extends OpMode{

    Gamepad currentGamepad1;
    Gamepad previousGamepad1;
    DcMotor fLeft;
    DcMotor fRight;
    DcMotor bLeft;
    DcMotor bRight;

    double drivePower =.8;
    double xPower;
    double yPower;
    double headingPower;
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {
        xPower = -gamepad1.left_stick_y;
        yPower = -gamepad1.left_stick_x;
        headingPower = -gamepad1.right_stick_x;

        double scaledPower = 1 - currentGamepad1.right_trigger;

        if (currentGamepad1.right_trigger >= 0.1 && !(previousGamepad1.right_trigger >= 0.1) && drivePower <= 0.8) {
            drivePower += 0.2;
        } else if (currentGamepad1.left_trigger >= 0.1 && !(previousGamepad1.left_trigger >= 0.1) && drivePower >= 0.8) {
            drivePower -= 0.2;
        }

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
            yPower = -drivePower;
            headingPower = 0;
        } else if (currentGamepad1.dpad_right) {
            xPower = 0;
            yPower = drivePower;
            headingPower = 0;

        }

        if (currentGamepad1.right_bumper) {
            xPower *= scaledPower;
            yPower *= scaledPower;
            headingPower *= scaledPower;
        }

    }
}
