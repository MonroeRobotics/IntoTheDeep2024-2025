package org.firstinspires.ftc.teamcode.tsa;

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
    double clawCloseAngle = 90; //placeholder
    double clawOpenAngle = 90; //placeholder
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
                clawAngle = clawCloseAngle;
                break;
            case openClaw:
                clawAngle = clawOpenAngle;
                break;
        }
        clawServo.setPosition(clawAngle);


    }
}

