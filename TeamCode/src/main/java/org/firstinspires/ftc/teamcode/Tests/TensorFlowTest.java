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

/**
 * This 2018-2019 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the gold and silver minerals.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
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

    @Override
    public void runOpMode()
    {
        int pos = 0;
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector())
            initTfod();
        else
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");

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
                        int goldY = -1;

                        if(updatedRecognitions.size()>=1)
                        {
                            for(Recognition r: updatedRecognitions)
                            {
                                if (r.getLabel().equals(LABEL_GOLD_MINERAL))
                                {
                                    goldMineralX = (int) r.getLeft();
                                    goldY = (int)r.getTop();
                                }
                            }
                        }
                        if (updatedRecognitions.size() == 3)
                        {
                            goldMineralX = -1;
                            goldY = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;
                            for (Recognition recognition : updatedRecognitions)
                            {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
                                {
                                    goldMineralX = (int) recognition.getLeft();
                                    goldY = (int)recognition.getTop();
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
                                }
                                else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X)
                                {
                                    telemetry.addData("Gold Mineral Position", "Right");
                                    telemetry.addLine("Right code was updated");
                                    pos = 1;
                                }
                                else
                                {
                                    telemetry.addData("Gold Mineral Position", "Center");
                                    telemetry.addLine("Center code was updated");
                                    pos = 0;
                                }
                            }
                        }
                        telemetry.addLine("Gold cor: (" + goldMineralX + ", " + goldY + ")");
                        telemetry.update();
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
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.7;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}