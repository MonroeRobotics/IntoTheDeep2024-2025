package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        Pose2d redSubmersible = new Pose2d(0,-35, Math.toRadians(90));
        Pose2d redSample = new Pose2d(28,46,Math.toRadians(180));
        Pose2d redHumanPlayer = new Pose2d(49,52,Math.toRadians(180));
        Pose2d redBasket = new Pose2d(-52,-52,Math.toRadians(50));
        Pose2d redBasketReturn = new Pose2d(55,0,Math.toRadians(130));
        Pose2d redCollectBackUp = new Pose2d(35,0, Math.toRadians(180));
        Pose2d redHang = new Pose2d(0,-27,Math.toRadians(270));

        Pose2d blueSubmersible = new Pose2d(-30,0, Math.toRadians(0));
        Pose2d blueSample1 = new Pose2d(-68,-26,Math.toRadians(180));
        Pose2d blueSample2 = new Pose2d(-58, -26,Math.toRadians(180));
        Pose2d blueSample3 = new Pose2d(-28, -26,Math.toRadians(180));
        Pose2d blueHumanPlayer = new Pose2d(-49,-52,Math.toRadians(360));
        Pose2d blueBasket = new Pose2d(-52,54,Math.toRadians(-45));
        Pose2d blueBasketReturn = new Pose2d(-55,0,Math.toRadians(130));
        Pose2d blueCollectBackUp = new Pose2d(-35,0, Math.toRadians(0));
        Pose2d blueHang = new Pose2d(0,27,Math.toRadians(270));

        Pose2d yellowSample1Approach = new Pose2d(-57,-24,Math.toRadians(180));
        Pose2d yellowSample1 = new Pose2d(-60,-24,Math.toRadians(180));
        Pose2d yellowSample2Approach = new Pose2d(-58,-34,Math.toRadians(90));
        Pose2d yellowSample2 = new Pose2d(-58, -30, Math.toRadians(90));
        Pose2d yellowSample3 = new Pose2d(-48,-30,Math.toRadians(90));




        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity redBot1 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-35, -58, Math.toRadians(90)))
                        .lineToLinearHeading(redBasket)
                        .lineToLinearHeading(yellowSample3)
                        .lineToLinearHeading(redBasket)
                        .lineToLinearHeading(yellowSample2Approach)
                        .lineToLinearHeading(yellowSample2)
                        .lineToLinearHeading(redBasket)
                        .lineToLinearHeading(yellowSample1Approach)
                        .lineToLinearHeading(yellowSample1)
                        .lineToLinearHeading(redBasket)
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(redBot1)
                .start();

    }
}