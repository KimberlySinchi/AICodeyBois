package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@TeleOp(name = "TensorFlowTest")
@Disabled

public class TensorFlowTest extends LinearOpMode
{
    //Importing the models needed to identify them through the camera phone with computer vision
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    //The Vuforia key needed in order to use it
    private static final String VUFORIA_KEY = "ATMeJeb/////AAAAGaZ47DzTRUyOhcXnfJD+z89ATBWAF+fi+oOutLvXaf0YT/RPuf2mu6VJsJowCDiWiOzGMHUjXKsLBqA4Ziar76oZY/juheUactiQaY6Z3qPHnGmchAMlYuqgKErvggTuqmFca8VvTjtB6YOheJmAbllTDTaCudODpnIDkuFNTa36WCTr4L8HcCnIsB7bjF8pZoivYEBwPkfCVtfAiEpqxbyDAZgNXnuCyp6v/oi3FYZbp7JkFVorcM182+q0PVN5gIr14SKEMlDcDFDiv/sQwNeQOs5iNBd1OSkCoTe9CYbdmtE0gUXxKN2w9NqwATYC6YRJP9uoopxqmr9zkepI10peh2/RnU6pHOmR0KKRAVh8";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;
    private boolean aligned = false;
    private boolean detect = true;

    @Override
    public void runOpMode()
    {
        int pos = 0;
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        /**
         * ONCE PLAY IS HIT THIS CODE WILL RUN
         */
        if (opModeIsActive())
        {
            /** Activate Tensor Flow Object Detection. */
            if (tfod != null)
                tfod.activate();
            while (opModeIsActive())
            {
                if (tfod != null)
                {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null)
                    {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        int goldMineralX = -1;
                        int goldMineralXR = -1;
                        int goldMineralCent = -1;
                        int leftRangeX = (1280 / 2) - 50;
                        int rightRangeX = leftRangeX + 100;
                        if (updatedRecognitions.size() >= 3)
                        {
                            for (Recognition r : updatedRecognitions)
                            {
                                if (r.getLabel().equals(LABEL_GOLD_MINERAL))
                                {
                                    goldMineralX = (int) r.getLeft();
                                    goldMineralXR = (int) r.getRight();
                                    goldMineralCent = (int) ((goldMineralX + goldMineralXR) / 2);
                                    aligned = isAligned(goldMineralCent, 640 - 125, 640 + 125);
                                    if (aligned)
                                    {
                                        pos = 0;
                                    }
                                    if (r.getLeft() < 640 - 125)
                                    {
                                        pos = -1;
                                        telemetry.addLine("LEFT");
                                        telemetry.update();
                                    }
                                    else if (r.getLeft() > 640 + 125)
                                    {
                                        pos = 1;
                                        telemetry.addLine("RIGHT");
                                        telemetry.update();
                                    }
                                    else
                                    {
                                        pos = 0;
                                        telemetry.addLine("MIDDLE");
                                        telemetry.update();
                                    }
                                }
                            }
                        }
                        if (updatedRecognitions.size() == 3)
                        {
                            goldMineralX = -1;
                            goldMineralXR = -1;
                            goldMineralCent = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;
                            for (Recognition recognition : updatedRecognitions)
                            {
                                //Getting coordinates of the mineral (leftMost pixel location of the box created around them)
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
                                {
                                    goldMineralX = (int) recognition.getLeft();
                                    goldMineralXR = (int) recognition.getRight();
                                    goldMineralCent = (goldMineralX + goldMineralXR) / 2;
                                }
                                else if (silverMineral1X == -1)
                                    silverMineral1X = (int) recognition.getLeft();
                                else
                                    silverMineral2X = (int) recognition.getLeft();
                            }
                            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1)
                            {
                                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X)
                                {
                                    telemetry.addData("Gold Mineral Position", "Left");
                                    telemetry.addLine("Left code was updated");
                                    pos = -1;
                                    telemetry.update();
                                }
                                else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X)
                                {
                                    telemetry.addData("Gold Mineral Position", "Right");
                                    telemetry.addLine("Right code was updated");
                                    pos = 1;
                                    telemetry.update();
                                }
                                else if (goldMineralX > silverMineral1X && goldMineralX < silverMineral2X ||
                                        goldMineralX < silverMineral1X && goldMineralX > silverMineral2X)
                                {
                                    telemetry.addData("Gold Mineral Position", "Center");
                                    telemetry.addLine("Center code was updated");
                                    pos = 0;
                                    telemetry.update();
                                }
                            }
                        }
                        if (updatedRecognitions.size() == 2)
                        {
                            int silverMineral1X = -1;
                            int silverMineral1XR = -1;
                            int silverMineral2X = -1;
                            int silverMineral2XR = -1;
                            for (Recognition recognition : updatedRecognitions)
                            {
                                //Getting coordinates of the mineral (leftMost pixel location of the box created around them)
                                if (silverMineral1X == -1)
                                    silverMineral1X = (int) recognition.getLeft();
                                else if(silverMineral1XR == -1)
                                    silverMineral1XR = (int) recognition.getRight();
                                else if(silverMineral2X == -1)
                                    silverMineral2X = (int) recognition.getLeft();
                                else
                                    silverMineral2XR = (int) recognition.getRight();
                            }
                            telemetry.addData("Silver Mineral 1 Left Coord", silverMineral1X);
                            telemetry.addData("Silver Mineral 1 Right Coord", silverMineral1XR);
                            telemetry.addData("Silver Mineral 2 Left Coord", silverMineral2X);
                            telemetry.addData("Silver Mineral 2 Right Coord", silverMineral2XR);
                            telemetry.update();
                            if(silverMineral1XR <= 640+125 && silverMineral2XR <= 640+125)
                            {
                                pos = 1;
                            }
                            else if(silverMineral1X >= 640-125 && silverMineral2X >= 640-125)
                            {
                                pos = -1;
                            }
                            else
                            {
                                pos = 0;
                            }
                        }

                    }
                }
            }
        }
        if (tfod != null)
            tfod.shutdown();
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    public static boolean isAligned(int cent, int rangeLeft, int rangeRight)
    {
        if(cent >= rangeLeft && cent <= rangeRight)
            return true;
        else
            return false;
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.7;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}