package org.firstinspires.ftc.teamcode.TSA.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class ArmController {
    HardwareMap hardwareMap;
    public enum ArmState{
        closeClaw,
        openClaw,
        clawUp,
        clawDown
    }
    Servo clawServo;
    Servo clawTilt;

    DcMotorEx armMotor;

    double clawAngle = .5;
    public static double clawClosed = .5; //claw closed angle
    public static double clawOpen = .625; //claw open angle

    double TiltAngle;
    public static double TILT_UP_ANGLE = 1;
    public static double TILT_DOWN_ANGLE = 0.6;

    public static double timer;

    public static boolean timerStarted;

    public static int armTarget;

    public static double armSpeedMultiplier = 10;


    public ArmState currentArmstate = ArmState.closeClaw;
    public ArmController(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }
    public void initArm(){
        clawServo = hardwareMap.get(Servo.class, "claw");
        clawTilt = hardwareMap.get(Servo.class, "clawAngle");
        currentArmstate = ArmState.closeClaw;
        armMotor = hardwareMap.get(DcMotorEx.class, "armMotor");
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(1.0);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void updateArmState(){
        switch(currentArmstate){
            case closeClaw:
                clawAngle = clawClosed;
                break;
            case openClaw:
                clawAngle = clawOpen;
                break;
            case clawUp:
                TiltAngle = TILT_UP_ANGLE;
                break;
            case clawDown:
                TiltAngle = TILT_DOWN_ANGLE;
                break;
        }
        clawServo.setPosition(clawAngle);
        clawTilt.setPosition(TiltAngle);
    }
    public void armMotor(double rightTrigger, double leftTrigger){
        if (rightTrigger >= .1 && !timerStarted){
            armTarget += rightTrigger * armSpeedMultiplier;
            timerStarted = true;
            timer = 50 + System.currentTimeMillis();
        }
        if (leftTrigger >= .1 && !timerStarted){
            armTarget -= rightTrigger * armSpeedMultiplier;
            timerStarted = true;
            timer = 50 + System.currentTimeMillis();
        }
        if (timer <= System.currentTimeMillis() && timerStarted) {
            timerStarted = false;
        }
        if(armTarget < 1){armTarget = 10;}
        armMotor.setTargetPosition(armTarget);
    }
}

