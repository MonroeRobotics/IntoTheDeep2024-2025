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

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(roadRunnerSim)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(0, 0, 0))
                                .forward(30)
                                .turn(Math.toRadians(90))
                                .forward(30)
                                .turn(Math.toRadians(90))
                                .forward(30)
                                .turn(Math.toRadians(90))
                                .forward(30)
                                .turn(Math.toRadians(90))
                                .build()
                );
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
    }
}