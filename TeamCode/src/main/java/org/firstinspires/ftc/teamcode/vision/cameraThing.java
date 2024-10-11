package org.firstinspires.ftc.teamcode.vision;

import android.graphics.Canvas;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Config
public class cameraThing implements VisionProcessor {
    HardwareMap hardwareMap;
    int cameraMonitorViewId;
    WebcamName webcamName;
    OpenCvCamera camera;

    Scalar lowHSV;
    Scalar highHSV;

    Telemetry telemetry;
    Mat cropC = new Mat();
    int width = 640;
    int height = 480;

    public Scalar getLowHSV() {
        return lowHSV;
    }

    public Scalar getHighHSV() {
        return highHSV;
    }

    double[] currentValues;
    Scalar currentScalar;

    public cameraThing (HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    public void initCam() {
        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcamName = hardwareMap.get(WebcamName.class, "webcam");
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
                FtcDashboard.getInstance().startCameraStream(camera, 24);
            }
            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        width = 640;
        height =480;
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos){
        //changes Mat input from RGB to HSV and saves to Mat HSV
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HSV);

        //creates center sqaure
        Rect centerScreen = new Rect(width/2 - width/8, height - height/4, width/4, height/4);


        cropC = input.submat(centerScreen);

        Imgproc.rectangle(input, centerScreen, new Scalar(50,180,180));

        return null; // No context object
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }
    /*public void pipeline extends Object OpenCvPipeline{
            final Mat grey = new Mat();
            public Mat processFrame(Mat input){
                Imgproc.cvtColor(input, grey, Imgproc.COLOR_RGB2GRAY);
                return grey;
                }
            }*/
}
