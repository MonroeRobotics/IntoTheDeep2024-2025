package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


@Config
public class ArmController {
    HardwareMap hardwareMap;

    //region Arm Variables



    int SLIDE_HEIGHT = 1; //Live Updating Slide height
    int SLIDE_STAGE = 0; //Used for incremental Slide Height
    public static double SLIDE_POWER = 0.8; //Max Linear Slide Power
    public static double SLIDE_MAX_VELO = 2000; //Max Linear Slide Velocity


    public enum ArmState { //Creates States that arm could be in for logic use
        INTAKE,
        OUTTAKE_READY,
        OUTTAKE_ACTIVE
    }

    ArmState currentArmState = ArmState.INTAKE; //Creates a variables to store current Arm State

    double ARM_POSITION = 0; //Live Updating Arm Servo Position (0 is intake position)
    public static double ARM_SERVO_FORWARD = 0;//Stores Value of Arm intake Position
    public static double ARM_SERVO_BACKWARD = 1;//Stores Value of Arm outtake Position

    double ARM_SERVO_POSITION = .7; //Live Updating Arm Position (.7 is open)
    public static double CLAW_SERVO_FORWARD = .1; //Stores Value of Claw intake Position
    public static double CLAW_SERVO_TRANSITION = 0.6; //Stores value of Claw Outtake position
    public static double CLAW_SERVO_BACKWARD = 0.7; //Stores value of Claw Outtake position
    public static int SLIDE_HEIGHT_SERVO_TRANSITION = 100;

    double outtakeTimer = 0; //Timer to control outtake
    public static double OUTTAKE_TIME = 150; //How Long Outtake runs for (ms)
    //endregion

    //region Arm Objects
    Servo extendoL;
    Servo extendoR;

    CRServo intakeL;
    CRServo intakeR;

    Servo intakeAngleL;
    Servo intakeAngleR;

    Servo armAngleL;
    Servo armAngleR;

    Servo claw;
    Servo clawAngle;

    DcMotorEx rightSlide;
    DcMotorEx leftSlide;
    //endregion

    public ArmController (HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    public void initArm(){
        //region Arm Init
        //region Arm Hardware Map

        extendoL = hardwareMap.get(Servo.class, "extendoL");
        //extendoR = hardwareMap.get(Servo.class, "extendoR");
        intakeL = hardwareMap.get(CRServo.class, "intakeL");
        intakeR = hardwareMap.get(CRServo.class, "intakeR");
        intakeAngleL = hardwareMap.get(Servo.class, "intakeAngleL");
        intakeAngleR = hardwareMap.get(Servo.class, "intakeAngleR");

        leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");

        //endregion

        //region Arm Lift Motor Settings
        if(!AutoConfiguration.hasInitAuto) {
            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightSlide.setDirection(DcMotorSimple.Direction.REVERSE);

        leftSlide.setTargetPosition(SLIDE_HEIGHT);
        rightSlide.setTargetPosition(SLIDE_HEIGHT);

        leftSlide.setPower(SLIDE_POWER);
        rightSlide.setPower(SLIDE_POWER);

        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftSlide.setVelocity(SLIDE_MAX_VELO);
        rightSlide.setVelocity(SLIDE_MAX_VELO);
        //endregion

        //region Servos
        intakeR.setDirection(CRServo.Direction.REVERSE);
        intakeAngleR.setDirection(Servo.Direction.REVERSE);
        armAngleR.setDirection(Servo.Direction.REVERSE);
        /*armServoLeft.setPosition(ARM_POSITION);
        armServoRight.setPosition(ARM_POSITION);
        clawServo.setPosition(CLAW_SERVO_POSITION);*/
        //endregion

        //endregion
    }
    public void switchArmState(){
        switch (currentArmState) {
            case INTAKE:
                currentArmState = ArmState.OUTTAKE_READY;
                updateArmState();
                break;
            case OUTTAKE_READY:
                currentArmState = ArmState.INTAKE;
                updateArmState();
                break;
        }
    }

    public void setOuttakePower(double power){
        //intakeServo.setPower(power);
    }


    public void updateArmState(){
        switch (currentArmState){
            case INTAKE:
                ARM_SERVO_POSITION = CLAW_SERVO_FORWARD;
                ARM_POSITION = ARM_SERVO_FORWARD;
                //intakeServo.setPower(0);
                SLIDE_HEIGHT = 20;
                break;
            case OUTTAKE_READY:
                ARM_SERVO_POSITION = CLAW_SERVO_BACKWARD;
                ARM_POSITION = ARM_SERVO_BACKWARD;
               // intakeServo.setPower(0);
                if (SLIDE_STAGE == 0) {
                    SLIDE_HEIGHT = 400;
                }
                else{
                    SLIDE_HEIGHT = 400 + (SLIDE_STAGE * 150);
                }
                break;
        }
    }

    public void changeStage(int change){
        if (change > 0 && SLIDE_STAGE < 9) {
            SLIDE_STAGE += change;
        } else if (change < 0 && SLIDE_STAGE > 0) {
            SLIDE_STAGE += change;
        }
        updateArmState();
    }

    public void setStage(int stage){
        if(stage >= 0 && stage <= 9) SLIDE_STAGE = stage;
        updateArmState();
    }

    public int getSlideHeight(){
        return SLIDE_HEIGHT;
    }


    public void setSlideHeight(int slideHeight){
        SLIDE_HEIGHT = slideHeight;
    }

    public ArmState getCurrentArmState(){
        return currentArmState;
    }


    public void setArmPos(double armPos){
        ARM_POSITION = armPos;
    }

    public void setClawPos(double clawPos){
        ARM_SERVO_POSITION = clawPos;

    }

    public void startOuttake(){
        if (outtakeTimer <= System.currentTimeMillis()) {
            outtakeTimer = System.currentTimeMillis() + OUTTAKE_TIME;
            //intakeServo.setPower(1);
        }
        else if (outtakeTimer >= System.currentTimeMillis() && outtakeTimer <= (System.currentTimeMillis() + (OUTTAKE_TIME * 2))){
            outtakeTimer += OUTTAKE_TIME;
            //intakeServo.setPower(1);
        }
    }

    public void checkOuttakeTimer(){
        if(outtakeTimer <= System.currentTimeMillis() && currentArmState == ArmState.OUTTAKE_READY){
            //intakeServo.setPower(0);
        }
    }


    public void updateArm(){
        //Sets Slides Arm and claw to respective positions as determined by the previous logic
        leftSlide.setTargetPosition(SLIDE_HEIGHT);
        rightSlide.setTargetPosition(SLIDE_HEIGHT);

        //armServoLeft.setPosition(ARM_POSITION);
        //armServoRight.setPosition(1 - ARM_POSITION);

        if (currentArmState == ArmState.INTAKE && leftSlide.getCurrentPosition() <= SLIDE_HEIGHT_SERVO_TRANSITION){
            //clawServo.setPosition(CLAW_SERVO_POSITION);
        }
        else if(currentArmState == ArmState.INTAKE) {
            //clawServo.setPosition(CLAW_SERVO_TRANSITION);
        }
        else if(currentArmState != ArmState.INTAKE) {
            //clawServo.setPosition(CLAW_SERVO_POSITION);
        }
        checkOuttakeTimer();
    }

    public void updateArmABS(){
        //Sets Slides Arm and claw to respective positions as determined by the previous logic
        leftSlide.setTargetPosition(SLIDE_HEIGHT);
        rightSlide.setTargetPosition(SLIDE_HEIGHT);

        //armServoLeft.setPosition(ARM_POSITION);
        //armServoRight.setPosition(1 - ARM_POSITION);

        //clawServo.setPosition(CLAW_SERVO_POSITION);
    }

    public void resetSlideZero(){

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setSlideHeight(0);
    }
}