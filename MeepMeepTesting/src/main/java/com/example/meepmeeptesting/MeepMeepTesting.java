package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.entity.BotEntity;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        Vector2d redSubmersible = new Vector2d(0,-35); //90
        Vector2d redSample1 = new Vector2d(48,-30); //90
        Vector2d redSample2 = new Vector2d(58,-30);  //90
        Vector2d redSample3 = new Vector2d(68,-30);  //90
        Vector2d redHumanPlayer = new Vector2d(49,52);  //180
        Vector2d redBasket = new Vector2d(-52,-52); //50
        Vector2d redBasketReturn = new Vector2d(55,0); //130
        Vector2d redCollectBackUp = new Vector2d(35,0); //180
        Vector2d redHang = new Vector2d(0,-27); //270

        Vector2d blueStartLeft = new Vector2d(16.58, 62.45);
        Vector2d blueStartRight = new Vector2d(-16.58, 62.45);
        Vector2d blueSubmersible = new Vector2d(0,34); //90
        Vector2d blueSample1 = new Vector2d(-28,-46); //90
        Vector2d blueSample2 = new Vector2d(-58, -26); //90
        Vector2d blueSample3 = new Vector2d(-28, -26); //90
        Vector2d blueHumanPlayer = new Vector2d(-49,-52); //360
        Vector2d blueBasket = new Vector2d(56,56);//225
        Vector2d blueBasketReturn = new Vector2d(-55,0); //130
        Vector2d blueCollectBackUp = new Vector2d(-35,0); //0
        Vector2d blueHang = new Vector2d(0,27);//270

        Vector2d redNeutralSample1Approach = new Vector2d(-57,-24); //180
        Vector2d redNeutralSample1 = new Vector2d(-60,-24); //180
        Vector2d redNeutral2Approach = new Vector2d(-58,-34); //90
        Vector2d redNeutralSample2 = new Vector2d(-58, -30); //90
        Vector2d redNeutralSample3 = new Vector2d(-48,-30); //90

        Vector2d blueNeutralSample1 = new Vector2d(48,33); //-90
        Vector2d blueNeutralSample2Approach = new Vector2d(58,39); //-90
        Vector2d blueNeutralSample2 = new Vector2d(58,33); //-90
        Vector2d blueNeutralSample3Approach = new Vector2d(57,26); //0
        Vector2d blueNeutralSample3 = new Vector2d(62,26); //0

        Vector2d startPos1 = new Vector2d(35, 58);

        Vector2d wallApproach = new Vector2d(-48,50);
        Vector2d wallGrab = new Vector2d(-48, 57);



        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity redBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        RoadRunnerBotEntity blueBot1 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
        RoadRunnerBotEntity blueBot2 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        blueBot1.runAction(blueBot1.getDrive().actionBuilder(new Pose2d(35, 58, Math.toRadians(-90)))
                .strafeToLinearHeading(blueBasket, Math.toRadians(220))
                .strafeToLinearHeading(blueNeutralSample1, Math.toRadians(-90))
                .strafeToLinearHeading(blueBasket,Math.toRadians(220))
                .strafeToLinearHeading(blueNeutralSample2Approach, Math.toRadians(-90))
                .strafeToLinearHeading(blueNeutralSample2, Math.toRadians(-90))
                .strafeToLinearHeading(blueBasket, Math.toRadians(220))
                .strafeToLinearHeading(blueNeutralSample3Approach, Math.toRadians(0))
                .strafeToLinearHeading(blueNeutralSample3, Math.toRadians(0))
                .build());


        RoadRunnerBotEntity bucketBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60,60,Math.toRadians(180), Math.toRadians(180), 12.673259843)
                .build();

        bucketBot.runAction(bucketBot.getDrive().actionBuilder(new Pose2d(blueStartLeft, Math.toRadians(-90)))
                .strafeToLinearHeading(blueBasket, Math.toRadians(225))
                //.strafeToLinearHeading(blueSubmersible, Math.toRadians(90))
                //.strafeToLinearHeading(new Vector2d(0, 38), Math.toRadians(90))
                .strafeToLinearHeading(blueNeutralSample1, Math.toRadians(-90))
                .strafeToLinearHeading(blueBasket, Math.toRadians(225))
                .strafeToLinearHeading(blueNeutralSample2Approach, Math.toRadians(-90))
                .strafeToLinearHeading(blueNeutralSample2, Math.toRadians(-90))
                .strafeToLinearHeading(blueBasket, Math.toRadians(225))
                .strafeToLinearHeading(blueNeutralSample3Approach, Math.toRadians(0))
                .strafeToLinearHeading(blueNeutralSample3, Math.toRadians(0))
                .build());

        RoadRunnerBotEntity specimenBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60,60,Math.toRadians(180),Math.toRadians(180),12.673259843)
                .build();

        specimenBot.runAction(specimenBot.getDrive().actionBuilder(new Pose2d(blueStartRight, Math.toRadians(90)))
                        .strafeToLinearHeading(new Vector2d(0, 48), Math.toRadians(-90))
                        .strafeToLinearHeading(blueSubmersible, Math.toRadians(90))
                        .strafeToLinearHeading(wallApproach, Math.toRadians(90))
                        .strafeToLinearHeading(wallGrab, Math.toRadians(90))
                        .strafeToLinearHeading(blueSubmersible, Math.toRadians(-90))
                .build());

        /*blueBot2.runAction(blueBot2.getDrive().actionBuilder(new Pose2d(-35, 58, Math.toRadians(-90)))
                        .strafeToLinearHeading(blueBasket, Math.toRadians(-90))
                        //.strafeToLinearHeading
                        .build();*/

        /*blueBot1.runAction(blueBot1.getDrive().actionBuilder(new Vector2d(0,0,0)))
                .lineToXLinearHeading(blueSubmersible)
                .lineToLinearHeading(blueNeutralSample1)
                .lineToLinearHeading(blueBasket)
                .lineToLinearHeading(blueNeutralSample2Approach)
                .lineToLinearHeading(blueNeutralSample2)
                .lineToLinearHeading(blueBasket)
                .lineToLinearHeading(blueNeutralSample3Approach)
                .lineToLinearHeading(blueNeutralSample3)
                .lineToLinearHeading(blueBasket)
                .build();
        RoadRunnerBotEntity blueBot1 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Vector2d(35, 58, Math.toRadians(-90)))
                        .lineToLinearHeading(redBasket)
                        .lineToLinearHeading(redNeutralSample3)
                        .lineToLinearHeading(redBasket)
                        .lineToLinearHeading(redNeutral2Approach)
                        .lineToLinearHeading(redNeutralSample2)
                        .lineToLinearHeading(redNeutralSample3)

                        .build());*/


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                //.addEntity(blueBot1)
                .addEntity(bucketBot)
                .addEntity(specimenBot)
                //.addEntity(blueBot1)
                .start();

    }
}