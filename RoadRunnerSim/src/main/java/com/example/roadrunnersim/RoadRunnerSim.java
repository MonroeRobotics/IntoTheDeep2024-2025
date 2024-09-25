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
        Pose2d sampleCollect = new Pose2d(30,0, Math.toRadians(180));
        Pose2d start = new Pose2d(60,0,Math.toRadians(180));
        Pose2d Sample = new Pose2d(28,46,Math.toRadians(180));
        Pose2d ObservationDeck = new Pose2d(56,54,Math.toRadians(360));
        Pose2d observationTurn = new Pose2d(28.001, 46.001,Math.toRadians(360));
        Pose2d Basket = new Pose2d(52,-54,Math.toRadians(180));
        Pose2d basketReturn = new Pose2d(55,0,Math.toRadians(130));
        Pose2d collectBackUp = new Pose2d(35,0, Math.toRadians(180));
        Pose2d hang = new Pose2d(0,-27,Math.toRadians(270));

        //endregion
        MeepMeep roadRunnerSim = new MeepMeep(800);
        DefaultBotBuilder defaultBotBuilder = new DefaultBotBuilder(roadRunnerSim);
//region first block
        defaultBotBuilder.setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15);// Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
        RoadRunnerBotEntity myBot = defaultBotBuilder
                    .followTrajectorySequence(drive ->
                            drive.trajectorySequenceBuilder(new Pose2d(60, 0, Math.PI))
                                    .lineToLinearHeading(sampleCollect)
                                    .lineToLinearHeading(collectBackUp)
                                    .lineToLinearHeading(Basket)
                                    .turn(Math.toRadians(-50))
                                    .lineToLinearHeading(sampleCollect)
                                    .build()
                    );
//endregion
        Image img = null;
        try { img = ImageIO.read(new File("/Users/monroerobotics/Documents/Into The Deep.png")); }
        catch (IOException e) {}

        roadRunnerSim.setBackground(img).setDarkMode(true).addEntity(myBot).start();
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