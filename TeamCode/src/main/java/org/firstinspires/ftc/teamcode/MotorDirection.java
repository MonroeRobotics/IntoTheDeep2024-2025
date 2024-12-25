package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
@TeleOp(name="MotorDirection",group="main")
public class MotorDirection extends OpMode {

    Gamepad currentgamepad;
    Gamepad previousGamepad;

    DcMotorEx extraLeftSlide;
    DcMotorEx extraRightSlide;

    boolean leftReversed;
    boolean rightReversed;
    boolean running;

    @Override
    public void init() {
        extraLeftSlide = hardwareMap.get(DcMotorEx.class, "extraLeftSlide");
        extraRightSlide = hardwareMap.get(DcMotorEx.class, "extraRightSlide");

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        currentgamepad = new Gamepad();
        previousGamepad = new Gamepad();
        running = false;
    }

    @Override
    public void loop() {
        if (currentgamepad.y && !previousGamepad.y && !running){
            extraLeftSlide.setPower(.5);
            extraRightSlide.setPower(.5);
            running = true;
        }
        if (currentgamepad.y && !previousGamepad.y && running){
            extraLeftSlide.setPower(0);
            extraRightSlide.setPower(0);
            running = false;
        }

        if (currentgamepad.a && !previousGamepad.a && !rightReversed){
            extraRightSlide.setDirection(DcMotorSimple.Direction.REVERSE);
            rightReversed = true;
        }
        else if (currentgamepad.a && !previousGamepad.a && rightReversed){
            extraRightSlide.setDirection(DcMotorSimple.Direction.FORWARD);
            rightReversed = false;
        }

        if (currentgamepad.dpad_down && previousGamepad.dpad_down && !leftReversed){
            extraLeftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
            leftReversed = true;
        }
        else if (currentgamepad.dpad_down && previousGamepad.dpad_down && leftReversed){
            extraLeftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
            leftReversed = false;
        }

        telemetry.addData("Status: ", running);
        telemetry.addData("Motor power", extraLeftSlide.getPower());
        telemetry.addData("left motor direction", extraLeftSlide.getDirection());
        telemetry.addData("right motor direction", extraRightSlide.getDirection());

        previousGamepad.copy(currentgamepad);
        currentgamepad.copy(gamepad2);
    }
}
