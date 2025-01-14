package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.driveClasses.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.ArmController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TeleOp(name = "soloDrive", group = "main")
public class blueSoloDrive extends OpMode {

    private static final Logger log = LoggerFactory.getLogger(blueSoloDrive.class);
    //region Gamepads
    Gamepad currentGamepad1;
    Gamepad previousGamepad1;

    Gamepad currentGamepad2;
    Gamepad previousGamepad2;
    //endregion

    MecanumDrive drive;
    Vector2d position = new Vector2d(0,0);
    Pose2d pose = new Pose2d(position, 90);

    double drivePower =.8;
    double xPower;
    double yPower;
    double headingPower;

    double distance;
    char sampleColor;
    boolean autoRetractOn = true;
    boolean newSample;
    char wrongAllianceColor = 'b';

    public boolean intakeExtended = false;
    public boolean sampleMode = true;
    public boolean clawOpen = true;
    public boolean specSequenceRan;
    public boolean triggerPressed;

    ArmController armController;
    int stage;

    RevColorSensorV3 intakeSensor;
    Servo swiper;
    public double swipe = 0;
    public double resetSwiper = .45;
    int counter;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        drive = new MecanumDrive(hardwareMap,pose);

        intakeSensor = hardwareMap.get(RevColorSensorV3.class, "intakeSensor");

        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();
        currentGamepad2 = new Gamepad();
        previousGamepad2 = new Gamepad();

        armController = new ArmController(hardwareMap);
        armController.initArm(true);
        stage = 0;

        swiper = hardwareMap.get(Servo.class, "swiper");
    }

    @Override
    public void loop() {
        //region drive
        //Stick controls

        if(currentGamepad2.left_stick_y >= .05 || currentGamepad2.left_stick_y <= -.05){
            xPower = -currentGamepad2.left_stick_y;
        }
        if(currentGamepad2.left_stick_x >= .05 || currentGamepad2.left_stick_x <= -.05){
            yPower = -currentGamepad2.left_stick_x;
        }
        if (currentGamepad2.right_stick_x >= .05 || currentGamepad2.right_stick_x <= -.05){
            headingPower = -currentGamepad2.right_stick_x * 0.7;
        }

        //Multiplier
        xPower *= drivePower;
        yPower *= drivePower;
        headingPower *= drivePower;

        //Cardinal dpad movements
        if (currentGamepad1.dpad_up) {
            xPower = drivePower;
            yPower = 0;
            headingPower = 0;
        } else if (currentGamepad1.dpad_down) {
            xPower = -drivePower;
            yPower = 0;
            headingPower = 0;
        } else if (currentGamepad1.dpad_left) {
            xPower = 0;
            yPower = drivePower;
            headingPower = 0;
        } else if (currentGamepad1.dpad_right) {
            xPower = 0;
            yPower = -drivePower;
            headingPower = 0;
        }

        //Speed controls
        if (currentGamepad1.right_bumper){
            drivePower = 1;
        } else if(currentGamepad1.left_bumper){
            drivePower = .4;
        } else{
            drivePower = .8;
        }
        //endregion

        if (currentGamepad1.right_trigger >= .1){
            /*if (currentGamepad1.right_bumper && !previousGamepad1.right_bumper){
                swipe += .05;
            }
            else if (currentGamepad1.left_bumper && !previousGamepad1.left_bumper){
                swipe -= .05;
            }*/
            swiper.setPosition(swipe);
        }
        else {
            /*if (currentGamepad1.right_bumper && !previousGamepad1.right_bumper){
                resetSwiper += .05;
            }
            else if (currentGamepad1.left_bumper && !previousGamepad1.left_bumper){
                resetSwiper -= .05;
            }*/
            swiper.setPosition(resetSwiper);
        }

        //region gamepad2

        //region bumpers/Intake actions
        if(currentGamepad2.right_bumper && !previousGamepad2.right_bumper){
            if (sampleMode) {
                if (!intakeExtended) {
                    armController.currentArmState = ArmController.ArmState.EXTEND;
                    intakeExtended = true;
                } else {
                    armController.currentArmState = ArmController.ArmState.RETRACT;
                    intakeExtended = false;
                    armController.startClawTimer();
                }
            }
            else{
                if(!intakeExtended){
                    armController.currentArmState = ArmController.ArmState.SPECIMEN_PICK_UP;
                    intakeExtended = true;
                }
                else {
                    intakeExtended = false;
                    //todo add something to retract if this happens
                }
            }
        }

        if(currentGamepad2.dpad_up && !previousGamepad2.dpad_up){
            sampleMode = !sampleMode;
        }
        //endregion

        //region dpad/slide height

        //up
        if(currentGamepad2.left_bumper && !previousGamepad2.left_bumper){
            stage += 1;
            if (stage > 1){
                stage = 1;
            }

            if(sampleMode) {
                if (stage == 1) {
                    armController.currentArmState = ArmController.ArmState.TALL_BUCKET_READY;
                }
                /*else if (stage == 2) {
                    armController.currentArmState = ArmController.ArmState.SHORT_BUCKET_READY;
                }*/
            }
            else {
                if (stage == 1) {
                    armController.currentArmState = ArmController.ArmState.HIGH_SPECIMEN_PLACE;
                    specSequenceRan = false;
                }
                /*else if (stage == 2) {
                    armController.currentArmState = ArmController.ArmState.LOW_SPECIMEN_PLACE;
                }*/
            }
        }

        //down
        if(currentGamepad2.left_trigger >= .05 && !triggerPressed){
            triggerPressed = true;
            stage -= 1;
            if (stage <0 ){
                stage = 0;
            }

            if(sampleMode){
                /*if (stage == 1) {
                    armController.currentArmState = ArmController.ArmState.SHORT_BUCKET_READY;
                }*/
                if(stage == 0){
                    armController.currentArmState = ArmController.ArmState.RETRACT;
                }
            }
            else {
                /*if (stage == 1) {
                    armController.currentArmState = ArmController.ArmState.LOW_SPECIMEN_PLACE;
                }*/
                if (stage == 0){
                    armController.currentArmState = ArmController.ArmState.RETRACT;
                }
            }
        }
        if (currentGamepad2.left_trigger <= .05){
            triggerPressed = false;
        }
        //endregion

        //region buttons

        //Claw
        if(currentGamepad2.x && !previousGamepad2.x){
            //Default close (used in sample mode)
            if(clawOpen){
                armController.currentArmState = ArmController.ArmState.CLOSE_CLAW;
                clawOpen = false;
            }
            //Press for specimen place sequence
            else if (!clawOpen && !sampleMode && !specSequenceRan){
                armController.currentArmState = ArmController.ArmState.SPECIMEN_PLACE_SEQUENCE;
                specSequenceRan = true;
            }
            //open after specimen place sequence
            else if(!clawOpen && !sampleMode && specSequenceRan){
                armController.currentArmState = ArmController.ArmState.OPEN_CLAW;
                specSequenceRan = false;
                clawOpen = true;
            }
            //default open (used in sample mode)
            else{
                armController.currentArmState = ArmController.ArmState.OPEN_CLAW;
                clawOpen = true;
            }
        }
        if(currentGamepad2.a && !previousGamepad2.a){
            armController.lowerIntake = !armController.lowerIntake;
        }

        //eject
        if(currentGamepad2.b && !previousGamepad2.b){
            armController.startEject();
        }

        //Mode switch
        if(currentGamepad2.y && !previousGamepad2.y){
            if(sampleMode){
                sampleMode = false;}
            else{
                sampleMode = true;
                armController.setClawAnglePos(ArmController.CLAW_ANGLE_INTAKE);
            }
        }

         //Weird buttons/Specific one time actions
        if (currentGamepad2.options && !previousGamepad2.options){
            if (counter == 0){
                armController.currentArmState = ArmController.ArmState.ASCENT;
            }
            else if (counter == 1){
                armController.currentArmState = ArmController.ArmState.HANG;
            }
            else if (counter == 2){
                armController.currentArmState = ArmController.ArmState.LOWER;
            }
            else counter = -1;
            counter += 1;
        }
        //endregion

        //endregion
        distance = intakeSensor.getDistance(DistanceUnit.MM);

        //add autoconfig stuffs
        if (intakeSensor.red() > intakeSensor.blue()) {
            sampleColor = 'r';
        } else if (intakeSensor.green() > intakeSensor.blue()){
            sampleColor = 'y';
        } else if (intakeSensor.blue() > intakeSensor.red()) {
            sampleColor = 'b';
        }

        if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left) {
            autoRetractOn = !autoRetractOn;
        }

        if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right){
            if (wrongAllianceColor == 'r'){
                wrongAllianceColor = 'b';
            }
            else wrongAllianceColor = 'r';
        }

        if (autoRetractOn) {
            if (distance <= 40 && sampleColor != wrongAllianceColor && !newSample && armController.currentArmState == ArmController.ArmState.EXTEND) {
                armController.currentArmState = ArmController.ArmState.RETRACT;
                newSample = true;
                armController.startClawTimer();
            }
            else if (distance <= 40 && sampleColor == wrongAllianceColor && !newSample && armController.currentArmState == ArmController.ArmState.EXTEND){
                armController.startEject();
            }
            else {
                newSample = false;
            }
        }

        Vector2d gamepadInput = new Vector2d(xPower, yPower);
        PoseVelocity2d poseVelocity2d = new PoseVelocity2d(gamepadInput, headingPower);
        drive.setDrivePowers(poseVelocity2d);

        armController.updateArmState();
        armController.updateArmABS();

        armController.checkIntakeServoPower();
        armController.checkIntakeAngle();
        armController.checkSlidePower();
        armController.checkClaw();

        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);

        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

        //telemetry.addData("intakeAngle", String.valueOf(armController.getIntakeAngle()));
        telemetry.addData("Auto Intake", autoRetractOn);
        telemetry.addData("Wrong Color", wrongAllianceColor);
        telemetry.addData("red", intakeSensor.red());
        telemetry.addData("green", intakeSensor.green());
        telemetry.addData("blue", intakeSensor.blue());
        telemetry.addData("currentArmState", armController.getCurrentArmState());
        telemetry.addData("slide target", armController.getSlideHeight());
        telemetry.addData("average slide height", ((armController.extraLeftSlide.getCurrentPosition() + armController.extraRightSlide.getCurrentPosition())/2));
        telemetry.addData("arm angle", armController.getArmAngle());
        //telemetry.addData("left slide height", armController.leftSlide.getCurrentPosition());
        //telemetry.addData("extra left slide height", armController.extraLeftSlide.getCurrentPosition());
        //telemetry.addData("extra right slide height", armController.extraRightSlide.getCurrentPosition());
        //telemetry.addData("right slide height", armController.rightSlide.getCurrentPosition());
        //telemetry.addData("distanceSensor", distance);
        //telemetry.addData("swiper position", swiper.getPosition());
        telemetry.update();
    }
}
