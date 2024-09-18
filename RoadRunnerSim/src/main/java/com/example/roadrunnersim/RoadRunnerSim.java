package com.example.roadrunnersim;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RoadRunnerSim {
    public static void main(String[] args) {
        MeepMeep roadRunnerSim = new MeepMeep(800);
        DefaultBotBuilder defaultBotBuilder = new DefaultBotBuilder(roadRunnerSim);
//region first block
        defaultBotBuilder.setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15);// Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
        RoadRunnerBotEntity myBot = defaultBotBuilder
                    .followTrajectorySequence(drive ->
                            drive.trajectorySequenceBuilder(new Pose2d(70, 0, Math.PI))
                                    .forward(46)
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




                                    .build()
                    );
//endregion
        roadRunnerSim.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK).setDarkMode(true).setBackgroundAlpha(0.95f).addEntity(myBot).start();
//  <following code you were using previously>
        /*roadRunnerSim.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();*/
    }
}