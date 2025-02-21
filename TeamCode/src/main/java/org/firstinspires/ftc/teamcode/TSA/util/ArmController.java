package org.firstinspires.ftc.teamcode.TSA.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class ArmController {
    HardwareMap hardwareMap;
    public enum ArmState{
        closeClaw,
        openClaw
    }
    Servo clawServo;
    double clawAngle;
    public static double CLAW_SERVO_FORWARD = .5; //claw closed angle
    public static double CLAW_SERVO_BACKWARD = .5; //claw open angle
    public ArmState currentArmstate  = ArmState.closeClaw;
    public ArmController(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }
    public void initArm(){
        clawServo = hardwareMap.get(Servo.class, "claw");

    }
    public void updateArmState(){
        switch(currentArmstate){
            case closeClaw:
                clawAngle = CLAW_SERVO_FORWARD;
                break;
            case openClaw:
                clawAngle = CLAW_SERVO_BACKWARD;
                break;
        }
        clawServo.setPosition(clawAngle);


    }
}

