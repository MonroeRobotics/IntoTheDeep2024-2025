package org.firstinspires.ftc.teamcode.TSA;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class TSAarmController {
    HardwareMap hardwareMap;
    public enum ArmState{
        closeClaw,
        openClaw
    }
    Servo clawServo;
    double clawAngle;
    public static double CLAW_SERVO_FORWARD = .1;
    public static double CLAW_SERVO_BACKWARD = 0.7;
    public ArmState currentArmstate  = ArmState.closeClaw;
    public TSAarmController(HardwareMap hardwareMap){
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

