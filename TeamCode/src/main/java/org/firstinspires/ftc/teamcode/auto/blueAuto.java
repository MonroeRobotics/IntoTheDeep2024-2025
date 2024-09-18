package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Blue Auto", group = "Main")
@Config
public class blueAuto extends LinearOpMode {

    //region Dashboard Variables

    //region Auto Timer variables
    public  static double SPECIMEN_PLACEMENT_TIME;
    public static double INTAKE_TIME;
    public static double HUMAN_PLAYER_WAIT_TIME;
    public static double ERROR_THRESHOLD = 10;
    double waitTimer;

    //endregion

    int autoCycleCount = 0;
    @Override
    public void runOpMode() throws InterruptedException {

    }
}
