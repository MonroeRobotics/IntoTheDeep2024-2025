package org.firstinspires.ftc.teamcode.archive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.vision.cameraThing;

@TeleOp (name = "cameraTest", group = "Main")
public class cameraTest extends OpMode {

    cameraThing camera;
    @Override
    public void init() {
    camera = new cameraThing(hardwareMap);
    camera.initCam();
    }


    @Override
    public void loop() {

    }
}
