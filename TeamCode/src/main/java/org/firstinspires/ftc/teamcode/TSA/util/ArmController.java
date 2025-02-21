package org.firstinspires.ftc.teamcode.TSA.util;

import com.acmerobotics.dashboard.config.Config;
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

    double clawAngle = .5;
    public static double clawClosed = .5; //claw closed angle
    public static double clawOpen = .75; //claw open angle

    double TiltAngle;
    public static double TILT_UP_ANGLE = 0.5;
    public static double TILT_DOWN_ANGLE = 0.5;

    public ArmState currentArmstate = ArmState.closeClaw;
    public ArmController(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }
    public void initArm(){
        clawServo = hardwareMap.get(Servo.class, "claw");
        clawTilt = hardwareMap.get(Servo.class, "clawAngle");
        currentArmstate = ArmState.closeClaw;
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
}

