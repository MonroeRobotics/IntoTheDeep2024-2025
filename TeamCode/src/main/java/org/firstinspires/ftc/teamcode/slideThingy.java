package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "slideThingy", group = "main")
public class slideThingy extends OpMode {
    DcMotor leftSlide;
    DcMotor extraLeftSlide;
    DcMotor extraRightSlide;
    DcMotor rightSlide;
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry());

        leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
        extraLeftSlide = hardwareMap.get(DcMotorEx.class, "extraLeftSlide");
        extraRightSlide = hardwareMap.get(DcMotorEx.class, "extraRightSlide");
        rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extraLeftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extraRightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        extraLeftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        extraRightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void loop() {
        telemetry.addData("leftSlide", leftSlide.getCurrentPosition());
        telemetry.addData("extraLeftSlide", extraLeftSlide.getCurrentPosition());
        telemetry.addData("extraRightSlide", extraRightSlide.getCurrentPosition());
        telemetry.addData("rightSlide", rightSlide.getCurrentPosition());
        telemetry.update();
    }
}
