package com.example.roadrunnersim;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RoadRunnerSim {
    public static void main(String[] args) {
        //region cords
        Pose2d redSampleCollect = new Pose2d(30,0, Math.toRadians(180));
        Pose2d redStart = new Pose2d(60,0,Math.toRadians(180));
        Pose2d redSample = new Pose2d(28,46,Math.toRadians(180));
        Pose2d redHumanPlayer = new Pose2d(49,52,Math.toRadians(180));
        Pose2d redBasket = new Pose2d(52,-54,Math.toRadians(130));
        Pose2d redBasketReturn = new Pose2d(55,0,Math.toRadians(130));
        Pose2d redCollectBackUp = new Pose2d(35,0, Math.toRadians(180));
        Pose2d redHang = new Pose2d(0,-27,Math.toRadians(270));

        Pose2d blueSubmersible = new Pose2d(-30,0, Math.toRadians(0));
        Pose2d blueStart = new Pose2d(-60,0,Math.toRadians(180));
        Pose2d blueSample1 = new Pose2d(-28,-46,Math.toRadians(0));
        Pose2d blueSample2 = new Pose2d(-28, -58,Math.toRadians(0));
        Pose2d blueSample3 = new Pose2d(-28, -68,Math.toRadians(0));
        Pose2d blueHumanPlayer = new Pose2d(-49,-52,Math.toRadians(360));
        Pose2d blueBasket = new Pose2d(-52,54,Math.toRadians(-45));
        Pose2d blueBasketReturn = new Pose2d(-55,0,Math.toRadians(130));
        Pose2d blueCollectBackUp = new Pose2d(-35,0, Math.toRadians(0));
        Pose2d blueHang = new Pose2d(0,27,Math.toRadians(270));


        //endregion
        MeepMeep roadRunnerSim = new MeepMeep(800);
        DefaultBotBuilder defaultBotBuilder = new DefaultBotBuilder(roadRunnerSim);
//region first block
        defaultBotBuilder.setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15);// Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
        RoadRunnerBotEntity redBot1 = defaultBotBuilder
                    .followTrajectorySequence(drive ->
                            drive.trajectorySequenceBuilder(new Pose2d(60, 0, Math.PI))
                                    .lineToLinearHeading(redSampleCollect)
                                    .lineToLinearHeading(redCollectBackUp)
                                    .lineToLinearHeading(redBasket)
                                    .lineToLinearHeading(redSampleCollect)
                                    .build()
                    );
        defaultBotBuilder.setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15);// Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
        RoadRunnerBotEntity redBot2 = defaultBotBuilder
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(60, 0, Math.PI))
                                .lineToLinearHeading(redSample)
                                .lineToLinearHeading(redHumanPlayer)
                                .lineToLinearHeading(redSampleCollect)
                                .build()
                );
        defaultBotBuilder.setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15);// Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
        RoadRunnerBotEntity blueBot1 = defaultBotBuilder
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-60, 0, Math.toRadians(0)))
                                .lineToLinearHeading(blueSubmersible)
                                .lineToLinearHeading(blueCollectBackUp)
                                .lineToLinearHeading(blueBasket)
                                .lineToLinearHeading(blueSubmersible)
                                .build()
                );
        defaultBotBuilder.setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15);// Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
        RoadRunnerBotEntity blueBot2 = defaultBotBuilder
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-60, 0, Math.toRadians(0)))
                                .lineToLinearHeading(blueSample1)
                                .lineToLinearHeading(blueHumanPlayer)
                                .lineToLinearHeading(blueSubmersible)
                                .build()
                );
        defaultBotBuilder.setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15);// Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
        RoadRunnerBotEntity autoPath = defaultBotBuilder
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-60, 0, Math.toRadians(0)))
                                .lineToLinearHeading(blueBasket)
                                .lineToLinearHeading(blueSample1)
                                .lineToLinearHeading(blueHumanPlayer)
                                .lineToLinearHeading(blueSample2)
                                .lineToLinearHeading(blueHumanPlayer)
                                .lineToLinearHeading(blueSample3)
                                .lineToLinearHeading(blueHumanPlayer)
                                .lineToLinearHeading(blueSubmersible)
                                .lineToLinearHeading(blueBasket)
                                .lineToLinearHeading(blueSubmersible)
                                .lineToLinearHeading(blueBasket)
                                .lineToLinearHeading(blueSubmersible)
                                .lineToLinearHeading(blueBasket)
                                .lineToLinearHeading(blueSubmersible)
                                .lineToLinearHeading(blueBasket)
                                .build()
                );
//endregion
        Image img = null;
        try { img = ImageIO.read(new File("/Users/monroerobotics/Documents/Into The Deep.png")); }
        catch (IOException e) {}

        roadRunnerSim.setBackground(img).setDarkMode(true).addEntity(autoPath).start();

//  <following code you were using previously>
        /*roadRunnerSim.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();*/
        /* .forward(46)
                                    .forward(-16)
                                    .turn(Math.toRadians(-90))
                                    .forward(45)
                                    .turn(Math.toRadians(90))
                                    .forward(15)
                                    .forward(-32.5)
                                    .turn(Math.toRadians(180))
                                    .turn(Math.toRadians(-90))
                                    .forward(45)
                                    .turn(Math.toRadians(-90))
                                    .forward(34)

                                    .forward(-16)
                                    .turn(Math.toRadians(-90))
                                    .forward(55)
                                    .turn(Math.toRadians(90))
                                    .forward(15)
                                    .forward(-32.5)
                                    .turn(Math.toRadians(180))
                                    .turn(Math.toRadians(-90))
                                    .forward(55)
                                    .turn(Math.toRadians(-90))
                                    .forward(34)

                                    .forward(-16)
                                    .turn(Math.toRadians(-90))
                                    .forward(65)
                                    .turn(Math.toRadians(90))
                                    .forward(15)
                                    .forward(-32.5)
                                    .turn(Math.toRadians(180))
                                    .turn(Math.toRadians(-90))
                                    .forward(65)
                                    .turn(Math.toRadians(-90))
                                    .forward(34)
         */
    }
}