package org.firstinspires.ftc.teamcode.Tests;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Detectors.SampleAlignDetector;


@TeleOp(name="Sample Align Doge", group="DogeCV")

public class SampleAlign extends OpMode
{
    // Detector object
    private SampleAlignDetector detector;


    @Override
    public void init() {
        telemetry.addData("Status", "Find gold position and align ;-;");

        // Setup detector
        detector = new SampleAlignDetector(); // Create the detector
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance()); // Initialize detector with app context and camera
        detector.useDefaults(); // Set detector to use default settings

        detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        // Optional tuning
        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.001;

        detector.ratioScorer.weight = 15;
        detector.ratioScorer.perfectRatio = 1.0;

        detector.enable(); // Start detector
        //
    }

    /*
     * Code to run REPEATEDLY when the driver hits INIT
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }


    /*
     * Code to run REPEATEDLY when the driver hits PLAY
     */
    @Override
    public void loop() {
        telemetry.addData("Current Order" , detector.getCurrentOrder().toString()); // The current result for the frame
        telemetry.addData("Last Order" , detector.getLastOrder().toString()); // The last known result
        telemetry.addData("IsAligned" , detector.getAligned()); // Is the bot aligned with the gold mineral?
        telemetry.addData("X Pos" , detector.getXPosition()); // Gold X position.
    }
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        detector.disable();
    }

}