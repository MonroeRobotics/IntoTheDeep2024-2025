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
    boolean leftRunning;
    boolean rightRunning;

    @Override
    public void init() {
        extraLeftSlide = hardwareMap.get(DcMotorEx.class, "extraLeftSlide");
        extraRightSlide = hardwareMap.get(DcMotorEx.class, "extraRightSlide");

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        currentgamepad = new Gamepad();
        previousGamepad = new Gamepad();
        leftRunning = false;
        rightRunning = false;
    }

    @Override
    public void loop() {
        if (currentgamepad.dpad_up && !previousGamepad.dpad_up && !leftRunning){
            extraLeftSlide.setPower(.5);
            leftRunning = true;
        }
        if (currentgamepad.dpad_up && !previousGamepad.dpad_up && leftRunning){
            extraLeftSlide.setPower(0);
            leftRunning = false;
        }

        if (currentgamepad.y && !previousGamepad.y && !rightRunning){
            extraRightSlide.setPower(.5);
            rightRunning = true;
        }
        if (currentgamepad.y && !previousGamepad.y && rightRunning){
            extraRightSlide.setPower(0);
            rightRunning = false;
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

        telemetry.addData("Status: ", leftRunning);
        telemetry.addData("Motor power", extraLeftSlide.getPower());
        telemetry.addData("left motor direction", extraLeftSlide.getDirection());
        telemetry.addData("right motor direction", extraRightSlide.getDirection());
        telemetry.update();

        previousGamepad.copy(currentgamepad);
        currentgamepad.copy(gamepad2);
    }
}
