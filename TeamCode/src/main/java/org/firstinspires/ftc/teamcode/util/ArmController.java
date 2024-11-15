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



    public static int SLIDE_HEIGHT = 5; //Live Updating Slide height
    int SLIDE_STAGE = 0; //Used for incremental Slide Height
    public static double SLIDE_POWER = 0.8; //Max Linear Slide Power
    public static double SLIDE_MAX_VELO = 2000; //Max Linear Slide Velocity


    public enum ArmState { //Creates States that arm could be in for logic use
        EXTEND,
        RETRACT,
        CLOSE_CLAW,
        SPECIMEN_PICK_UP,
        LOW_SPECIMEN_PLACE,
        HIGH_SPECIMEN_PLACE,
        SHORT_BUCKET_READY,
        TALL_BUCKET_READY,
        OPEN_CLAW, //open claw
        SPECIMEN_PLACE_SEQUENCE,
        POINT_BLANK_INTAKE
    }

    public ArmState currentArmState = ArmState.EXTEND; //Creates a variables to store current Arm State

    double ARM_ANGLE_POSITION = 0.12; //Live Updating Arm Angle Position (0 is intake position) should normally be .11
    public static double ARM_ANGLE_INTAKE = 0.12;//Stores Value of Arm intake Position should normally be .11
    public static double ARM_ANGLE_SPECIMEN_PICK_UP = .67; //get value, likely opposite of normal outtake
    public static double ARM_ANGLE_SPECIMEN_DROP = .41;//Stores value of arm outtake position for specimen
    public static double ARM_ANGLE_BUCKET_OUTTAKE = .56;//Stores Value of Arm outtake Position

    double CLAW_POSITION = .5; //Live Updating Arm Position (.5 is open)
    public static double CLAW_CLOSED = .3; //Stores Value of Claw closed Position
    //public static double CLAW_SERVO_TRANSITION = 0.6; //Stores value of Claw Outtake position
    public static double CLAW_OPEN = 0.5; //Stores value of Claw open position

    public static double CLAW_ANGLE_POSITION = .2; //stores value of claw angle
    public static double CLAW_ANGLE_INTAKE = .2; //stores value of claw angle for intake
    public static double CLAW_ANGLE_SPECIMEN_PICK_UP = .55; //
    public static double CLAW_ANGLE_OUTTAKE = .55; //stores value of the claw angle when dropping stuff
    public static double CLAW_ANGLE_SPECIMEN_OUTTAKE = 1;//stuff

    public static double INTAKE_SERVO_POWER = 0.0; //Stores value of intake servos
    public static double INTAKE_SERVO_POWER_OFF = 0.0; //stores value of intake cr servos not spinning
    public static double INTAKE_SERVO_INTAKE = -1; //stores value of intake CR servos intaking
    public static double INTAKE_SERVO_EDJECT = 1; //stores value of intake cr servos edjecting something

    public static double INTAKE_ANGLE = .20; //stores value of intake angle
    public static double INTAKE_ANGLE_INTAKE = .43; //stores value of intakeAngle intake position
    public static double INTAKE_ANGLE_RETRACT = .20; //stores value of intakeAngle when retracted

    public static double EXTENDO_ANGLE = .95; //stores value fo current extendo
    public static double EXTENDO_EXTEND = .75; //stores value of extendo extending
    public static double EXTENDO_RETRACT = .95; //stores value of extendo retracting

    public static int SLIDE_HEIGHT_LOWERED = 5;
    public static int SLIDE_HEIGHT_SERVO_TRANSITION = 100;
    public static int SLIDE_HEIGHT_SPECIMEN_PICK_UP = 5; //get value
    public static int SLIDE_HEIGHT_LOW_SPECIMEN_PLACE; //get value
    public static int SLIDE_HEIGHT_HIGH_SPECIMEN_PLACE = 825; //get value
    public static int SLIDE_HEIGHT_LOW_BUCKET_DROP; //get value
    public static int SLIDE_HEIGHT_HIGH_BUCKET_DROP = 1835;
    public static int SLIDE_HEIGHT_HIGH_SPECIMEN_DROP = 385;
    public static int SLIDE_HEIGHT_LOW_SPECIMEN_DROP; //get value, Low specimen place -100

    double edjectTimer = 0; //Timer to control outtake
    public static double EDJECT_TIME = 1500; //How Long edject runs for (ms)

    double intakeTimer = 0; //timer to control intake drop delay
    public static double INTAKE_TIMER = 500;//how long intake waits to drop (ms)
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

    boolean edject;

    public ArmController (HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    public void initArm(){
        //region Arm Init
        //region Arm Hardware Map

        extendoL = hardwareMap.get(Servo.class, "extendoL");
        extendoR = hardwareMap.get(Servo.class, "extendoR");

        intakeL = hardwareMap.get(CRServo.class, "intakeL");
        intakeR = hardwareMap.get(CRServo.class, "intakeR");

        intakeAngleL = hardwareMap.get(Servo.class, "intakeAngleL");
        intakeAngleR = hardwareMap.get(Servo.class, "intakeAngleR");

        armAngleL = hardwareMap.get(Servo.class, "armAngleL");
        armAngleR = hardwareMap.get(Servo.class, "armAngleR");

        claw = hardwareMap.get(Servo.class, "claw");
        clawAngle = hardwareMap.get(Servo.class, "clawAngle");

        leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");

        //endregion

        //region Arm Lift Motor Settings
        if(!AutoConfiguration.hasInitAuto) {
            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //rightSlide.setDirection(DcMotorSimple.Direction.REVERSE);

        SLIDE_HEIGHT = SLIDE_HEIGHT_LOWERED;

        leftSlide.setTargetPosition(SLIDE_HEIGHT);
        rightSlide.setTargetPosition(SLIDE_HEIGHT);

        leftSlide.setPower(SLIDE_POWER);
        rightSlide.setPower(SLIDE_POWER);

        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftSlide.setVelocity(SLIDE_MAX_VELO);
        rightSlide.setVelocity(SLIDE_MAX_VELO);

        leftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        rightSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        //endregion

        //region Servos
        intakeR.setDirection(CRServo.Direction.REVERSE);
        intakeAngleR.setDirection(Servo.Direction.REVERSE);
        extendoR.setDirection(Servo.Direction.REVERSE);
        armAngleR.setDirection(Servo.Direction.REVERSE);

        intakeAngleL.setPosition(INTAKE_ANGLE);
        intakeAngleR.setPosition(INTAKE_ANGLE);

        extendoL.setPosition(EXTENDO_ANGLE);
        extendoR.setPosition(EXTENDO_ANGLE);

        armAngleL.setPosition(ARM_ANGLE_POSITION);
        armAngleR.setPosition(ARM_ANGLE_POSITION);

        clawAngle.setPosition(CLAW_ANGLE_POSITION);

        claw.setPosition(CLAW_POSITION);
        //endregion

        currentArmState = ArmState.RETRACT;
        //endregion
    }
    public void switchArmState(){ //Switches between methods in sequential order
        switch (currentArmState) {
            case EXTEND:
                currentArmState = ArmState.RETRACT;
                updateArmState();
                break;
            case RETRACT:
                currentArmState = ArmState.CLOSE_CLAW;
                updateArmState();
                break;
            case CLOSE_CLAW:
                    currentArmState = ArmState.TALL_BUCKET_READY;
                    updateArmState();
                    break;
            case TALL_BUCKET_READY:
                currentArmState = ArmState.OPEN_CLAW;
                updateArmState();
                break;
            case OPEN_CLAW:
                currentArmState = ArmState.EXTEND;
                updateArmState();
                break;
        }
    }


    public void updateArmState(){
        switch (currentArmState){
            case EXTEND:
                CLAW_POSITION = CLAW_OPEN;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_INTAKE;
                ARM_ANGLE_POSITION = ARM_ANGLE_INTAKE;
                EXTENDO_ANGLE = EXTENDO_EXTEND;
                //INTAKE_ANGLE = INTAKE_ANGLE_INTAKE; Intake angle is adjusted with a seperate class so we can get a delay
                //INTAKE_SERVO_POWER = INTAKE_SERVO_INTAKE; same reason as intake angle
                SLIDE_HEIGHT = SLIDE_HEIGHT_LOWERED;
                break;
            case RETRACT:
                EXTENDO_ANGLE = EXTENDO_RETRACT;
                INTAKE_ANGLE = INTAKE_ANGLE_RETRACT;
                ARM_ANGLE_POSITION = ARM_ANGLE_INTAKE;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_INTAKE;
                break;
            case CLOSE_CLAW:
                CLAW_POSITION = CLAW_CLOSED;
                INTAKE_SERVO_POWER = INTAKE_SERVO_POWER_OFF;
                break;
            case SPECIMEN_PICK_UP:
                SLIDE_HEIGHT = SLIDE_HEIGHT_SPECIMEN_PICK_UP;
                ARM_ANGLE_POSITION = ARM_ANGLE_SPECIMEN_PICK_UP;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_SPECIMEN_PICK_UP;
                break;
            case LOW_SPECIMEN_PLACE:
                SLIDE_HEIGHT = SLIDE_HEIGHT_LOW_SPECIMEN_PLACE;
                ARM_ANGLE_POSITION = ARM_ANGLE_SPECIMEN_DROP;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_SPECIMEN_OUTTAKE;
                break;
            case HIGH_SPECIMEN_PLACE:
                SLIDE_HEIGHT = SLIDE_HEIGHT_HIGH_SPECIMEN_PLACE;
                ARM_ANGLE_POSITION = ARM_ANGLE_SPECIMEN_DROP;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_SPECIMEN_OUTTAKE;
                break;
            case SHORT_BUCKET_READY:
                SLIDE_HEIGHT = SLIDE_HEIGHT_LOW_BUCKET_DROP;
                ARM_ANGLE_POSITION = ARM_ANGLE_BUCKET_OUTTAKE;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_OUTTAKE;
                break;
            case TALL_BUCKET_READY:
                SLIDE_HEIGHT = SLIDE_HEIGHT_HIGH_BUCKET_DROP;
                ARM_ANGLE_POSITION = ARM_ANGLE_BUCKET_OUTTAKE;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_OUTTAKE;
               // intakeServo.setPower(0);
                break;
            case OPEN_CLAW:
                CLAW_POSITION = CLAW_OPEN;
                break;
            case SPECIMEN_PLACE_SEQUENCE:
                SLIDE_HEIGHT = SLIDE_HEIGHT_HIGH_SPECIMEN_DROP; //implement high and low difference
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
    public double getIntakeAngle(){return INTAKE_ANGLE;}


    public void setSlideHeight(int slideHeight){
        SLIDE_HEIGHT = slideHeight;
    }

    public ArmState getCurrentArmState(){
        return currentArmState;
    }


    public void setArmPos(double armPos){
        ARM_ANGLE_POSITION = armPos;
    }

    public void setClawPos(double clawPos){
        CLAW_POSITION = clawPos;

    }

    public void startEdject(){
        edjectTimer = System.currentTimeMillis() + EDJECT_TIME;
        INTAKE_SERVO_POWER = INTAKE_SERVO_EDJECT;
        edject = true;
    }

    public void checkIntakeServoPower(){
        if (edjectTimer <= System.currentTimeMillis() && edject) {
            INTAKE_SERVO_POWER = INTAKE_SERVO_POWER_OFF;
            edject = false;
        }
        else if (currentArmState == ArmState.EXTEND && !edject){
            INTAKE_SERVO_POWER = INTAKE_SERVO_INTAKE;
        }
        else if (currentArmState == ArmState.RETRACT && !edject){
            INTAKE_SERVO_POWER = INTAKE_SERVO_POWER_OFF;
        }
    }

    public void startIntake(){
        intakeTimer = System.currentTimeMillis() + INTAKE_TIMER;
    }
    public void checkIntakeAngle(){
        if (currentArmState == ArmState.EXTEND && intakeTimer <= System.currentTimeMillis()){
            INTAKE_ANGLE = INTAKE_ANGLE_INTAKE;
        }
    }


    public void updateArm(){
        //Sets Slides Arm and claw to respective positions as determined by the previous logic
        leftSlide.setTargetPosition(SLIDE_HEIGHT);
        rightSlide.setTargetPosition(SLIDE_HEIGHT);

        //armServoLeft.setPosition(ARM_POSITION);
        //armServoRight.setPosition(1 - ARM_POSITION);

        if (currentArmState == ArmState.EXTEND && leftSlide.getCurrentPosition() <= SLIDE_HEIGHT_SERVO_TRANSITION){
            //clawServo.setPosition(CLAW_SERVO_POSITION);
        }
        else if(currentArmState == ArmState.EXTEND) {
            //clawServo.setPosition(CLAW_SERVO_TRANSITION);
        }
        else if(currentArmState != ArmState.EXTEND) {
            //clawServo.setPosition(CLAW_SERVO_POSITION);
        }
        checkIntakeServoPower();
    }

    public void updateArmABS(){
        //Sets Slides Arm and claw to respective positions as determined by the previous logic
        leftSlide.setTargetPosition(SLIDE_HEIGHT);
        rightSlide.setTargetPosition(SLIDE_HEIGHT);

        intakeL.setPower(INTAKE_SERVO_POWER);
        intakeR.setPower(INTAKE_SERVO_POWER);

        intakeAngleL.setPosition(INTAKE_ANGLE);
        intakeAngleR.setPosition(INTAKE_ANGLE);

        extendoL.setPosition(EXTENDO_ANGLE);
        extendoR.setPosition(EXTENDO_ANGLE);

        armAngleL.setPosition(ARM_ANGLE_POSITION);
        armAngleR.setPosition(ARM_ANGLE_POSITION);

        clawAngle.setPosition(CLAW_ANGLE_POSITION);
        claw.setPosition(CLAW_POSITION);
    }

    public void resetSlideZero(){

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setSlideHeight(0);
    }
}