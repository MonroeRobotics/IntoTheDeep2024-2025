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
    
    //region Slide constraint values
    public static int SLIDE_HEIGHT = 0; //Live Updating Slide height
    int SLIDE_STAGE = 0; //Used for incremental Slide Height
    public static double SLIDE_POWER_ON = 0.8; //Max Linear Slide Power
    public  static double SLIDE_POWER = .8; //adjustable thingy
    public static double SLIDE_POWER_OFF = 0.0; //for saving power when slides are lowered
    public static double SLIDE_MAX_VELO = 2000; //Max Linear Slide Velocity
    //endregion

    public boolean lowerIntake;

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
        ASCENT,
        HANG,
        LOWER
    }

    //region Position value section
    public ArmState currentArmState = ArmState.RETRACT; //Creates a variables to store current Arm State

    //region Arm Angle
    double ARM_ANGLE_POSITION = .39; //Live Updating Arm Angle Position (0 is intake position) should normally be .15
    public static double ARM_ANGLE_INTAKE = .39;//Stores Value of Arm intake Position should normally be .15
    public static double ARM_ANGLE_SPECIMEN_PICK_UP = .89; //get value, likely opposite of normal outtake
    public static double ARM_ANGLE_SPECIMEN_DROP = .38;//Stores value of arm outtake position for specimen
    public static double ARM_ANGLE_BUCKET_OUTTAKE = .79;//Stores Value of Arm outtake Position
    public static double ARM_ANGLE_SPECIMEN_START = .44;
    public static double ARM_ANGLE_ASCENT = .51;
    //endregion

    //region Claw
    double CLAW_POSITION = .5; //Live Updating Arm Position (.5 is open)
    public static double CLAW_CLOSED = .25; //Stores Value of Claw closed Position
    //public static double CLAW_SERVO_TRANSITION = 0.6; //Stores value of Claw Outtake position
    public static double CLAW_OPEN = 0.5; //Stores value of Claw open position
    //endregion

    //region Claw Angle
    public static double CLAW_ANGLE_POSITION = .19; //stores value of claw angle
    public static double CLAW_ANGLE_INTAKE = .19; //stores value of claw angle for intake
    public static double CLAW_ANGLE_SPECIMEN_PICK_UP = .56; //
    public static double CLAW_ANGLE_OUTTAKE = .66; //stores value of the claw angle when dropping stuff
    public static double CLAW_ANGLE_SPECIMEN_OUTTAKE = .58;//stuff
    public static double CLAW_ANGLE_SPECIMEN_START = .25;
    public static double CLAW_ANGLE_ASCENT = .92;
    //endregion

    //region Intake CR Servos
    public static double INTAKE_SERVO_POWER = 0.0; //Stores value of intake servos
    public static double INTAKE_SERVO_POWER_OFF = 0.0; //stores value of intake cr servos not spinning
    public static double INTAKE_SERVO_INTAKE = -1; //stores value of intake CR servos intaking
    public static double INTAKE_SERVO_EJECT = 1; //stores value of intake cr servos ejecting something
    //endregion

    //region Intake Angle
    public static double INTAKE_ANGLE = .19; //stores value of intake angle
    public static double INTAKE_ANGLE_INTAKE = .43; //stores value of intakeAngle intake position
    public static double INTAKE_ANGLE_RETRACT = .19; //stores value of intakeAngle when retracted
    //endregion

    //region Extendo
    public static double EXTENDO_ANGLE = .95; //stores value for current extendo
    public static double EXTENDO_EXTEND = .65; //stores value of extendo extending
    public static double EXTENDO_RETRACT = .95; //stores value of extendo retracting
    //endregion

    //region Slide heights
    public static int SLIDE_HEIGHT_LOWERED = 0;
    public static int SLIDE_HEIGHT_SERVO_TRANSITION = 100;
    public static int SLIDE_HEIGHT_SPECIMEN_PICK_UP = 0; //get value
    public static int SLIDE_HEIGHT_LOW_SPECIMEN_PLACE; //get value
    public static int SLIDE_HEIGHT_HIGH_SPECIMEN_PLACE = 865; //get value
    public static int SLIDE_HEIGHT_LOW_BUCKET_DROP; //get value
    public static int SLIDE_HEIGHT_HIGH_BUCKET_DROP = 1860;
    public static int SLIDE_HEIGHT_HIGH_SPECIMEN_DROP = 1350;
    public static int SLIDE_HEIGHT_LOW_SPECIMEN_DROP; //get value, Low specimen place -100
    public static int SLIDE_HEIGHT_ASCENT = 1800;
    public static int SLIDE_HEIGHT_HANG = 1170;
    //endregion

    //region Timers
    double ejectTimer = 0; //Timer to control outtake
    public static double EJECT_TIME = 750; //How Long eject runs for (ms)

    double intakeTimer = 0; //timer to control intake drop delay
    public static double INTAKE_TIMER = 50;//how long intake waits to drop (ms)
    //endregion
    //endregion

    //region Arm Motors
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

    public DcMotorEx rightSlide;
    public DcMotorEx leftSlide;
    public DcMotorEx extraLeftSlide;
    public DcMotorEx extraRightSlide;
    //endregion

    boolean eject;

    boolean clawTimerRan;
    double clawTimer;

    public ArmController (HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    public void initArm(boolean bucketOnly){
        //region Arm Init
        //region Hardware Mapping

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
        extraLeftSlide = hardwareMap.get(DcMotorEx.class, "extraLeftSlide");
        extraRightSlide = hardwareMap.get(DcMotorEx.class, "extraRightSlide");

        //endregion

        //region slide stuff
        if(!AutoConfiguration.hasInitAuto) {
            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            extraLeftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            extraRightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extraLeftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extraRightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        extraLeftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extraRightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //endregion

        //region Initialization values
        SLIDE_HEIGHT = SLIDE_HEIGHT_LOWERED;
        INTAKE_ANGLE = INTAKE_ANGLE_RETRACT;
        EXTENDO_ANGLE = EXTENDO_RETRACT;
        if (bucketOnly){
            CLAW_ANGLE_POSITION = CLAW_ANGLE_INTAKE;
            ARM_ANGLE_POSITION = ARM_ANGLE_INTAKE;
        }
        else{
            CLAW_ANGLE_POSITION = CLAW_ANGLE_SPECIMEN_START;
            ARM_ANGLE_POSITION = ARM_ANGLE_SPECIMEN_START;
        }
        //endregion

        //region Slide Constraints
        leftSlide.setTargetPosition(SLIDE_HEIGHT);
        rightSlide.setTargetPosition(SLIDE_HEIGHT);
        extraLeftSlide.setTargetPosition(SLIDE_HEIGHT);
        extraRightSlide.setTargetPosition(SLIDE_HEIGHT);

        leftSlide.setPower(SLIDE_POWER);
        rightSlide.setPower(SLIDE_POWER);
        extraLeftSlide.setPower(SLIDE_POWER);
        extraRightSlide.setPower(SLIDE_POWER);

        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extraLeftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extraRightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftSlide.setVelocity(SLIDE_MAX_VELO);
        rightSlide.setVelocity(SLIDE_MAX_VELO);
        extraLeftSlide.setVelocity(SLIDE_MAX_VELO);
        extraRightSlide.setVelocity(SLIDE_MAX_VELO);
        //endregion

        //region Reversing things
        leftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        rightSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        extraLeftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        //extraRightSlide.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeR.setDirection(CRServo.Direction.REVERSE);
        intakeAngleR.setDirection(Servo.Direction.REVERSE);
        extendoR.setDirection(Servo.Direction.REVERSE);
        armAngleR.setDirection(Servo.Direction.REVERSE);
        //endregion

        //region Servo Starting Positions
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


    public void updateArmState(){
        switch (currentArmState){
            case EXTEND:
                //Extends intake and prepares arm for grabbing samples
                CLAW_POSITION = CLAW_OPEN;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_INTAKE;
                ARM_ANGLE_POSITION = ARM_ANGLE_INTAKE;
                EXTENDO_ANGLE = EXTENDO_EXTEND;
                SLIDE_HEIGHT = SLIDE_HEIGHT_LOWERED;
                break;
            case RETRACT:
                //retracts intake and prepares arm for grabbing samples
                EXTENDO_ANGLE = EXTENDO_RETRACT;
                INTAKE_ANGLE = INTAKE_ANGLE_RETRACT;
                lowerIntake = false;
                ARM_ANGLE_POSITION = ARM_ANGLE_INTAKE;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_INTAKE;
                SLIDE_HEIGHT = SLIDE_HEIGHT_LOWERED;
                break;
            case CLOSE_CLAW:
                //Closes claw, crazy
                CLAW_POSITION = CLAW_CLOSED;
                INTAKE_SERVO_POWER = INTAKE_SERVO_POWER_OFF;
                break;
            case SPECIMEN_PICK_UP:
                //aligns arm with wall for picking up specimens
                SLIDE_HEIGHT = SLIDE_HEIGHT_SPECIMEN_PICK_UP;
                ARM_ANGLE_POSITION = ARM_ANGLE_SPECIMEN_PICK_UP;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_SPECIMEN_PICK_UP;
                break;
            case LOW_SPECIMEN_PLACE:
                //Readying for placing specimens on the low bar
                SLIDE_HEIGHT = SLIDE_HEIGHT_LOW_SPECIMEN_PLACE;
                ARM_ANGLE_POSITION = ARM_ANGLE_SPECIMEN_DROP;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_SPECIMEN_OUTTAKE;
                break;
            case HIGH_SPECIMEN_PLACE:
                //Readying for placing specimens on the high bar
                SLIDE_HEIGHT = SLIDE_HEIGHT_HIGH_SPECIMEN_PLACE;
                ARM_ANGLE_POSITION = ARM_ANGLE_SPECIMEN_DROP;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_SPECIMEN_OUTTAKE;
                break;
            case SHORT_BUCKET_READY:
                //readying for placing samples in the low bucket
                SLIDE_HEIGHT = SLIDE_HEIGHT_LOW_BUCKET_DROP;
                ARM_ANGLE_POSITION = ARM_ANGLE_BUCKET_OUTTAKE;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_OUTTAKE;
                break;
            case TALL_BUCKET_READY:
                //readying for placing samples in the high bucket
                SLIDE_HEIGHT = SLIDE_HEIGHT_HIGH_BUCKET_DROP;
                ARM_ANGLE_POSITION = ARM_ANGLE_BUCKET_OUTTAKE;
                CLAW_ANGLE_POSITION = CLAW_ANGLE_OUTTAKE;
               // intakeServo.setPower(0);
                break;
            case OPEN_CLAW:
                //opens claw
                CLAW_POSITION = CLAW_OPEN;
                break;
            case SPECIMEN_PLACE_SEQUENCE:
                //lowers slides to ram specimen onto bar
                SLIDE_HEIGHT = SLIDE_HEIGHT_HIGH_SPECIMEN_DROP; //implement high and low difference
                break;
            case ASCENT:
                //Positions for hanging the arm on the low bar, WIP.
                CLAW_ANGLE_POSITION = CLAW_ANGLE_ASCENT;
                ARM_ANGLE_POSITION = ARM_ANGLE_ASCENT;
                SLIDE_HEIGHT = SLIDE_HEIGHT_ASCENT;
                break;
            case HANG:
                SLIDE_POWER = .2;
                leftSlide.setPower(SLIDE_POWER);
                rightSlide.setPower(SLIDE_POWER);
                extraLeftSlide.setPower(SLIDE_POWER);
                extraRightSlide.setPower(SLIDE_POWER);

                SLIDE_HEIGHT = SLIDE_HEIGHT_HANG;
                break;
            case LOWER:
                SLIDE_HEIGHT = SLIDE_HEIGHT_ASCENT;
        }
        //region Position Updates
        leftSlide.setTargetPosition(SLIDE_HEIGHT);
        rightSlide.setTargetPosition(SLIDE_HEIGHT);
        extraLeftSlide.setTargetPosition(SLIDE_HEIGHT);
        extraRightSlide.setTargetPosition(SLIDE_HEIGHT);

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
        //endregion
    }

    //region telemetry and adjustments
    public int getSlideHeight(){
        return SLIDE_HEIGHT;
    }
    public double getIntakeAngle(){return INTAKE_ANGLE;}
    public double getArmAngle(){return armAngleL.getPosition();}


    public void setSlideHeight(int slideHeight){
        SLIDE_HEIGHT = slideHeight;
    }

    public ArmState getCurrentArmState(){
        return currentArmState;
    }


    public void setArmPos(double armPos){
        ARM_ANGLE_POSITION = armPos;
    }
    public void setIntakePos(double intakePos){
        INTAKE_ANGLE = intakePos;
    }

    public void setClawAnglePos(double clawAnglePos){
        CLAW_ANGLE_POSITION = clawAnglePos;
    }
    //endregion

    //region Time and State regulated systems
    public void startEject(){
        ejectTimer = System.currentTimeMillis() + EJECT_TIME;
        INTAKE_SERVO_POWER = INTAKE_SERVO_EJECT;
        eject = true;
    }

    public void checkIntakeServoPower(){
        if (ejectTimer <= System.currentTimeMillis() && eject) {
            INTAKE_SERVO_POWER = INTAKE_SERVO_POWER_OFF;
            eject = false;
        }
        else if (currentArmState == ArmState.EXTEND && !eject){
            INTAKE_SERVO_POWER = INTAKE_SERVO_INTAKE;
        }
        else if (currentArmState == ArmState.RETRACT && !eject){
            INTAKE_SERVO_POWER = INTAKE_SERVO_POWER_OFF;
        }
    }

    public void startIntake(){
        intakeTimer = System.currentTimeMillis() + INTAKE_TIMER;
    }
    public void checkIntakeAngle(){
        if (currentArmState == ArmState.EXTEND && lowerIntake){
            INTAKE_ANGLE = INTAKE_ANGLE_INTAKE;
        }
        else if (currentArmState == ArmState.EXTEND){
            INTAKE_ANGLE = INTAKE_ANGLE_RETRACT;
        }
    }

    public void startClawTimer(){
        clawTimer = System.currentTimeMillis() + 750;
        clawTimerRan = false;
    }
    public void checkClaw(){
        if (System.currentTimeMillis() >= clawTimer && !clawTimerRan){
            clawTimerRan = true;
            currentArmState = ArmState.CLOSE_CLAW;
        }
    }

    public void checkSlidePower(){
        if (extraRightSlide.getCurrentPosition() <= (SLIDE_HEIGHT + 5) && extraRightSlide.getCurrentPosition() >= (SLIDE_HEIGHT - 5)){
            rightSlide.setPower(SLIDE_POWER_OFF);
        }
        else{
            rightSlide.setPower(SLIDE_POWER_ON);
        }

        if (extraLeftSlide.getCurrentPosition() <= (SLIDE_HEIGHT + 5) && extraLeftSlide.getCurrentPosition() >= (SLIDE_HEIGHT - 5)){
            leftSlide.setPower(SLIDE_POWER_OFF);
        }
        else{
            leftSlide.setPower(SLIDE_POWER_ON);
        }

        if (extraRightSlide.getCurrentPosition() <= (SLIDE_HEIGHT + 1) && extraRightSlide.getCurrentPosition() >= (SLIDE_HEIGHT - 1)){
            extraRightSlide.setPower(SLIDE_POWER_OFF);
        }
        else{
            extraRightSlide.setPower(SLIDE_POWER_ON);
        }

        if (extraLeftSlide.getCurrentPosition() <= (SLIDE_HEIGHT + 1) && extraLeftSlide.getCurrentPosition() >= (SLIDE_HEIGHT - 1)){
            extraLeftSlide.setPower(SLIDE_POWER_OFF);
        }
        else{
            extraLeftSlide.setPower(SLIDE_POWER_ON);
        }
        }
    //endregion


    public void updateArmABS(){
        //Sets Slides Arm and claw to respective positions as determined by the previous logic
        //This section has been moved into updateArmState and is now unnecessary after testing
        leftSlide.setTargetPosition(SLIDE_HEIGHT);
        rightSlide.setTargetPosition(SLIDE_HEIGHT);
        extraLeftSlide.setTargetPosition(SLIDE_HEIGHT);
        extraRightSlide.setTargetPosition(SLIDE_HEIGHT);

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
        extraLeftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extraRightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extraLeftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extraRightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setSlideHeight(0);
    }
}
