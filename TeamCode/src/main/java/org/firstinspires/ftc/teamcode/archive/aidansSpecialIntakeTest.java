package org.firstinspires.ftc.teamcode.archive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "aidan's intake test", group = "main")
public class aidansSpecialIntakeTest extends OpMode {
    CRServo intakeL;
    CRServo intakeR;
    Gamepad gamepad;
    @Override
    public void init() {
       intakeL = hardwareMap.get(CRServo.class, "intakeL");
       intakeR = hardwareMap.get(CRServo.class, "intakeR");
       gamepad = gamepad1;
    }

    @Override
    public void loop() {
        if(gamepad.left_bumper){
            intakeL.setPower(1.0);
            intakeR.setPower(-1.0);
        }
        if(gamepad.right_bumper){
            intakeL.setPower(-1.0);
            intakeR.setPower(1.0);
        }
        if(gamepad.x){
            intakeL.setPower(0.0);
            intakeR.setPower(0.0);
        }
    }
}
