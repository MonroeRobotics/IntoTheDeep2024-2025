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
    Double driveAngle;
    Double actualDriveRadian;
    Double getActualDriveDegree;
    @Override
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
        if ((currentGamepad.right_stick_x >.1) || (currentGamepad.right_stick_y >.1) || (currentGamepad.right_stick_x <-.1) || (currentGamepad.right_stick_y <-.1)){
            driveAngle = (Math.atan2(currentGamepad.right_stick_y, currentGamepad.right_stick_x));
            actualDriveRadian = driveAngle;
            driveAngle /= Math.PI;
            getActualDriveDegree = driveAngle;
            if (driveAngle <0){
                driveAngle+=1;
            }
            heading.setPosition(driveAngle);

            //CR
        }

        //telemetry.addData("Motor Power", driveMotor.getPower());
        telemetry.addData("Status", "Running");
        telemetry.addData("actualRad", actualDriveRadian);
        telemetry.addData("actualDeg", getActualDriveDegree);
        telemetry.addData("driveAngle", driveAngle);
        telemetry.addData("Servo Position", heading.getPosition());
        telemetry.addData("rightStickX", currentGamepad.right_stick_x);
        telemetry.addData("rightStickY", currentGamepad.right_stick_y);
        telemetry.update();
    }
}