package org.firstinspires.ftc.teamcode.TSA;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class TSAarmController {
    HardwareMap hardwareMap;
    public enum ArmState{
        closeClaw,
        openClaw,
        clawUp,
        clawDown
    }
    Servo clawServo;
    Servo clawTilt;
    double clawAngle;
    public static double CLAW_SERVO_FORWARD = .5; //claw closed angle
    public static double CLAW_SERVO_BACKWARD = .5; //claw open angle
    double TiltAngle;
    public static double TILT_UP_ANGLE = 0.5;
    public static double TILT_DOWN_ANGLE = 0.5;
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

