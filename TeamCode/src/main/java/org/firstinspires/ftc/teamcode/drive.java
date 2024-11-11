package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.driveClasses.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.ArmController;

@TeleOp(name = "drive", group = "main")
public class drive extends OpMode {

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

    public boolean intakeExtended = false;
    public boolean sample = true;
    public boolean clawOpen = true;

    ArmController armController;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        drive = new MecanumDrive(hardwareMap,pose);

        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();
        currentGamepad2 = new Gamepad();
        previousGamepad2 = new Gamepad();

        armController = new ArmController(hardwareMap);
        armController.initArm();
    }

    @Override
    public void loop() {
        //region drive
        if(currentGamepad1.left_stick_y >= .05 || currentGamepad1.left_stick_y <= -.05){
            xPower = -currentGamepad1.left_stick_y;
        }
        if(currentGamepad1.left_stick_x >= .05 || currentGamepad1.left_stick_x <= -.05){
            yPower = -currentGamepad1.left_stick_x;
        }
        if (currentGamepad1.right_stick_x >= .05 || currentGamepad1.right_stick_x <= -.05){
            headingPower = -currentGamepad1.right_stick_x;
        }

        xPower *= drivePower;
        yPower *= drivePower;
        headingPower *= drivePower;

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

        if (currentGamepad1.right_bumper){
            drivePower = 1;
        } else if(currentGamepad1.left_bumper){
            drivePower = .4;
        } else{
            drivePower = .8;
        }
        //endregion

        //region gamepad2

        //region bumpers
        if(currentGamepad2.right_bumper && !previousGamepad2.right_bumper){
            sample = true;
            if(!intakeExtended){
                armController.currentArmState = ArmController.ArmState.EXTEND;
                intakeExtended = true;
            }
            else {
                armController.currentArmState = ArmController.ArmState.RETRACT;
                intakeExtended = false;
            }
        }

        if(currentGamepad2.left_bumper && !previousGamepad2.right_bumper){
            sample = false;
            if(!intakeExtended){
                armController.currentArmState = ArmController.ArmState.SPECIMEN_PICK_UP;
                intakeExtended = true;
            }
            else {
                intakeExtended = false;
            }
        }
        //endregion

        //region dpad
        if(currentGamepad2.dpad_up && !previousGamepad2.dpad_up){
            if (sample){
                armController.currentArmState = ArmController.ArmState.TALL_BUCKET_READY;
            }
            else {
                armController.currentArmState = ArmController.ArmState.HIGH_SPECIMEN_PLACE;
            }
        }

        if(currentGamepad2.dpad_down & !previousGamepad2.dpad_down){
            if(sample){
                armController.currentArmState = ArmController.ArmState.SHORT_BUCKET_READY;
            }
            else {
                armController.currentArmState = ArmController.ArmState.LOW_SPECIMEN_PLACE;
            }
        }
        //endregion

        //region buttons
        if(currentGamepad2.x && !previousGamepad2.x){
            if(clawOpen){
                armController.currentArmState = ArmController.ArmState.CLOSE_CLAW;
                clawOpen = false;
            }
            else if (!clawOpen && !sample){
                armController.currentArmState = ArmController.ArmState.SPECIMEN_PLACE_SEQUENCE;
                clawOpen = true;
            }
            else{
                armController.currentArmState = ArmController.ArmState.OPEN_CLAW;
                clawOpen = true;
            }
        }
        /*if(currentGamepad2.a && !previousGamepad2.a){
            //point blank intake
        }*/
        if(currentGamepad2.b && !previousGamepad2.b){
            armController.startEdject();
        }
        /*if(currentGamepad2.y && !previousGamepad2.y){
            //free button
        }*/
        //endregion

        //endregion

        Vector2d gamepadInput = new Vector2d(xPower, yPower);
        PoseVelocity2d poseVelocity2d = new PoseVelocity2d(gamepadInput, headingPower);
        drive.setDrivePowers(poseVelocity2d);

        armController.updateArmState();
        armController.updateArmABS();

        armController.checkEdject();
        armController.checkIntake();

        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);

        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

        telemetry.addData("intakeAngle", String.valueOf(armController.getIntakeAngle()));
        telemetry.addData("currentArmState", armController.getCurrentArmState());
        telemetry.addData("slide height", armController.getSlideHeight());
    }
}
