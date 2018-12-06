package org.firstinspires.ftc.teamcode.Code;
import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Detectors.SampleAlignDetector;

@Autonomous(name = "Basic Autonomous", group = "Slave")

public class AutoAttempt1 extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontL;
    private DcMotor frontR;
    private DcMotor backL;
    private DcMotor backR;
    private Servo armSpin;
    private SampleAlignDetector detector;
    static final double SPEED = 1;

    @Override
    public void runOpMode()
    {
        try
        {
            frontL = hardwareMap.get(DcMotor.class, "DC1");
            frontL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        }
        catch(Exception e)
        {
        }
        try
        {
            frontR = hardwareMap.get(DcMotor.class, "DC2");
            frontR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch(Exception e){}
        try
        {
            backR = hardwareMap.get(DcMotor.class, "DC3");
            backR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch(Exception e){}
        try
        {
            backL = hardwareMap.get(DcMotor.class, "DC4");
            backL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch(Exception e){}

        //For convenience, we will print on the phone that the robot is ready
        telemetry.addData("Status", "Ready to run"); //Same as System.out.println();
        telemetry.update(); //Makes it show up on the phone

        /**
         * SETS UP DETECTOR STUFF
         */
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

        /**
         * Now that we have finished INITIALIZING (connecting our variables to the robot), our robot will just stand there UNTIL
         the driver hits the PLAY button (which appears after hitting the INIT button)
         * INIT is when you would connect your robot to your code, PLAY is when your movement code would start running
         */
        waitForStart();
        runtime.reset(); //Since we created the variable, the timer has been counting so we have to reset it for our movement code
                        //so that we start at zero (and it's easier to code with)

        //Finding the position of the gold mineral


        //Driving forward for 2 seconds
        forward();
        while (opModeIsActive() && (runtime.seconds() < 2))
        {
            telemetry.addData("Moving Forward", "Time Elapsed:", runtime.seconds());
            telemetry.update();
        }
        //Rotating left for 1.3 seconds
        rotateLeft();
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1.3)
        {
            telemetry.addData("Rotating Left", "Time Elapsed:", runtime.seconds());
            telemetry.update();
        }
        //Rotating right for 0.5 seconds
        rotateRight();
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 0.5)
        {
            telemetry.addData("Rotating Right", "Time Elapsed:", runtime.seconds());
            telemetry.update();
        }
        //Moving right for 2 seconds
        right();
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 2)
        {
            telemetry.addData("Moving Right", "Time Elapsed:", runtime.seconds());
        }
        //Stop the autonomous program
        stop();
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);

    }
    public void stop(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while (timer.seconds() <= time)
        {
            frontL.setPower(0);
            backL.setPower(0);
            frontR.setPower(0);
            backR.setPower(0);
        }
    }
    public void forward(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(SPEED);
            backL.setPower(SPEED);
            frontR.setPower(-SPEED);
            backR.setPower(-SPEED);
        }
        stop(0.5);
    }
    public void backward(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(-SPEED);
            backL.setPower(-SPEED);
            frontR.setPower(SPEED);
            backR.setPower(SPEED);
        }
        stop(0.5);
    }
    public void right(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(SPEED);
            frontR.setPower(SPEED);
            backR.setPower(-SPEED);
            backL.setPower(-SPEED);
        }
        stop(0.5);
    }
    public void left(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(-SPEED);
            frontR.setPower(-SPEED);
            backL.setPower(SPEED);
            backR.setPower(SPEED);
        }
        stop(0.5);
    }
    public void rotateRight(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(SPEED);
            frontR.setPower(SPEED);
            backL.setPower(SPEED);
            backR.setPower(SPEED);
        }
        stop(0.5);
    }
    public void rotateLeft(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(-SPEED);
            frontR.setPower(-SPEED);
            backL.setPower(-SPEED);
            backR.setPower(-SPEED);
        }
        stop(0.5);
    }
    public void forward()
    {
        frontL.setPower(SPEED);
        backL.setPower(SPEED);
        frontR.setPower(-SPEED);
        backR.setPower(-SPEED);
    }
    public void backward()
    {
        frontL.setPower(-SPEED);
        backL.setPower(-SPEED);
        frontR.setPower(SPEED);
        backR.setPower(SPEED);
    }
    public void right()
    {
        frontL.setPower(SPEED);
        frontR.setPower(SPEED);
        backR.setPower(-SPEED);
        backL.setPower(-SPEED);
    }
    public void left()
    {
        frontL.setPower(-SPEED);
        frontR.setPower(-SPEED);
        backL.setPower(SPEED);
        backR.setPower(SPEED);
    }
    public void rotateRight()
    {
        frontL.setPower(SPEED);
        frontR.setPower(SPEED);
        backL.setPower(SPEED);
        backR.setPower(SPEED);
    }
    public void rotateLeft()
    {
        frontL.setPower(-SPEED);
        frontR.setPower(-SPEED);
        backL.setPower(-SPEED);
        backR.setPower(-SPEED);
    }
    public void forwards(long time)
    {
        frontL.setPower(SPEED);
        backL.setPower(SPEED);
        frontR.setPower(-SPEED);
        backR.setPower(-SPEED);
        sleep(time);
    }
    public void backwards(long time)
    {
        frontL.setPower(-SPEED);
        backL.setPower(-SPEED);
        frontR.setPower(SPEED);
        backR.setPower(SPEED);
        sleep(time);
    }
    public void rights(long time)
    {
        frontL.setPower(SPEED);
        frontR.setPower(SPEED);
        backR.setPower(-SPEED);
        backL.setPower(-SPEED);
        sleep(time);
    }
    public void lefts(long time)
    {
        frontL.setPower(-SPEED);
        frontR.setPower(-SPEED);
        backL.setPower(SPEED);
        backR.setPower(SPEED);
        sleep(time);
    }
    public void rotateRights(long time)
    {
        frontL.setPower(SPEED);
        frontR.setPower(SPEED);
        backL.setPower(SPEED);
        backR.setPower(SPEED);
        sleep(time);
    }
    public void rotateLefts(long time)
    {
        frontL.setPower(-SPEED);
        frontR.setPower(-SPEED);
        backL.setPower(-SPEED);
        backR.setPower(-SPEED);
        sleep(time);
    }


/*
    /*@Override
    public void loop() {

    }

    static final double     FORWARD_SPEED = 0.6;
    static final double     TURN_SPEED    = 0.5;

    public void runOpMode() {

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

        // Step 1:  Drive forward for 3 seconds
        robot.leftDrive.setPower(FORWARD_SPEED);
        robot.rightDrive.setPower(FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 2:  Spin right for 1.3 seconds
        robot.leftDrive.setPower(TURN_SPEED);
        robot.rightDrive.setPower(-TURN_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.3)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 3:  Drive Backwards for 1 Second
        robot.leftDrive.setPower(-FORWARD_SPEED);
        robot.rightDrive.setPower(-FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Leg 3: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 4:  Stop and close the claw.
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.leftClaw.setPosition(1.0);
        robot.rightClaw.setPosition(0.0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
    */
}
