package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "aidan's intake test", group = "main")
public class aidansSpecialIntakeTest extends OpMode {
    CRServo servo0;
    CRServo servo1;
    Gamepad gamepad;
    @Override
    public void init() {
       servo0 = hardwareMap.get(CRServo.class, "servo0");
       servo1 = hardwareMap.get(CRServo.class, "servo1");
       gamepad = gamepad1;
    }

    @Override
    public void loop() {
        if(gamepad.left_bumper){
            servo0.setPower(1.0);
            servo1.setPower(-1.0);
        }
        if(gamepad.right_bumper){
            servo0.setPower(-1.0);
            servo1.setPower(1.0);
        }
        if(gamepad.x){
            servo0.setPower(0.0);
            servo1.setPower(0.0);
        }
    }
}
