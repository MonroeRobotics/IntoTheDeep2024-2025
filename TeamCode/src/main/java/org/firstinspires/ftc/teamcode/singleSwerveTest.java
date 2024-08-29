package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="swerveTest", group = "Main")
public class singleSwerveTest extends OpMode {

    Gamepad gamepad;
    DcMotor driveMotor;
    Servo heading;
    //Double drivePower = .8;
    Gamepad currentGamepad;
    Gamepad previousGamepad;
    double driveAngle;
    double actualDriveRadian;
    double getActualDriveDegree;
    double multiplier =324;
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        heading = hardwareMap.get(Servo.class, "driveAngle");
        //driveMotor = hardwareMap.get(DcMotor.class, "driveMotor");

        currentGamepad = gamepad1;
        previousGamepad = gamepad1;
        heading.setPosition(0);
    }

    @Override
    public void loop() {
        /*if (currentGamepad.dpad_up){
            driveMotor.setPower(drivePower);
        }
        else if (currentGamepad.dpad_down){
            driveMotor.setPower(-drivePower);
        }
        else{driveMotor.setPower(0);}*/
        if ((currentGamepad.right_stick_x >.1) || (currentGamepad.right_stick_y >.1) || (currentGamepad.right_stick_x <-.1) || (currentGamepad.right_stick_y <-.1) && !currentGamepad.x){
            driveAngle = (Math.atan2(-currentGamepad.right_stick_y, currentGamepad.right_stick_x));
            actualDriveRadian = driveAngle;
            driveAngle /= (Math.PI*2.0);
            getActualDriveDegree = driveAngle;
            if (driveAngle <0){
                driveAngle+=1;
            }
            driveAngle *= 360.0/multiplier;
            driveAngle -=(.25*(360/multiplier));
            heading.setPosition(driveAngle);
        }
        if (currentGamepad.dpad_left){
            driveAngle = (.25*(360.0/multiplier));
            heading.setPosition(driveAngle);
        }
        if (currentGamepad.dpad_down){
            driveAngle = (.5*(360.0/multiplier));
            heading.setPosition(driveAngle);
        }
        if (currentGamepad.dpad_right){
            driveAngle = (.75*(360.0/multiplier));
            heading.setPosition(driveAngle);
        }
        if (currentGamepad.dpad_up){
            driveAngle = (0);
            heading.setPosition(driveAngle);
        }

        if (currentGamepad.left_bumper){
            heading.setPosition(0);
        }
        if (currentGamepad.right_bumper){
            heading.setPosition(1);
        }

        if (currentGamepad.cross && !previousGamepad.cross){
            multiplier-=1.0;
        }
        else if (currentGamepad.circle && !previousGamepad.circle){
            multiplier+=1.0;
        }
        //telemetry.addData("Motor Power", driveMotor.getPower());
        telemetry.addData("Status", "Running");
        telemetry.addData("input", actualDriveRadian);
        telemetry.addData("actualDeg", getActualDriveDegree);
        telemetry.addData("driveAngle", driveAngle);
        telemetry.addData("Servo Position", heading.getPosition());
        telemetry.addData("rightStickX", currentGamepad.right_stick_x);
        telemetry.addData("rightStickY", -currentGamepad.right_stick_y);
        telemetry.addData("multiplier", multiplier);
        telemetry.update();
    }
}