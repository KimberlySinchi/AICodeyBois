package org.firstinspires.ftc.teamcode.Code;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Detectors.SampleAlignDetector;
import org.firstinspires.ftc.teamcode.Helpers.Slave;

import java.util.List;

@Autonomous(name = "Rotating Left Tensor", group = "Slave")

public class AutoAttempt3 extends LinearOpMode
{
    private Slave slave = new Slave();
    private ElapsedTime runtime = new ElapsedTime();

    private ElapsedTime rotRightTime = new ElapsedTime();
    private double rotationTime = 0;

    static final double SPEED = 0.6;
//speedm = 5.488 cm/s
    /**
     * TENSOR FLOW INSTANCE VARIABLES (DO NOT TOUCH)
     */
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "ATMeJeb/////AAAAGaZ47DzTRUyOhcXnfJD+z89ATBWAF+fi+oOutLvXaf0YT/RPuf2mu6VJsJowCDiWiOzGMHUjXKsLBqA4Ziar76oZY/juheUactiQaY6Z3qPHnGmchAMlYuqgKErvggTuqmFca8VvTjtB6YOheJmAbllTDTaCudODpnIDkuFNTa36WCTr4L8HcCnIsB7bjF8pZoivYEBwPkfCVtfAiEpqxbyDAZgNXnuCyp6v/oi3FYZbp7JkFVorcM182+q0PVN5gIr14SKEMlDcDFDiv/sQwNeQOs5iNBd1OSkCoTe9CYbdmtE0gUXxKN2w9NqwATYC6YRJP9uoopxqmr9zkepI10peh2/RnU6pHOmR0KKRAVh8";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private int pos = -2;
    private boolean aligned = false;
    private boolean detect = true;
    private double rotateBack = 0.0;

    private static final double SECONDS_PER_TILE = 11.106; //WE NEED TO PUT WALID'S DATA INTO THIS
    //2.161 inches/sec
    private static final double SECONDS_PER_360_DEG = 0.0; //WE NEED TO PUT WALID'S DATA INTO THIS

    @Override
    public void runOpMode()
    {
        slave.init(hardwareMap);

        //For convenience, we will print on the phone that the robot is ready
        telemetry.addData("Status", "Ready to run"); //Same as System.out.println();
        telemetry.update(); //Makes it show up on the phone

        /**
         * SETS UP DETECTOR STUFF
         */
        telemetry.addData("Status", "Find gold position and align ;-;");

        //Setup for Tensor Flow detector
        initVuforia();
        initTfod();
        tfod.activate();

        waitForStart();
        runtime.reset();

        //Finding the position of the gold mineral
        if(opModeIsActive())
        {
            boolean flagRotTime = true;
            rotRightTime.reset();
            while (opModeIsActive() && runtime.seconds() < 10 && detect)
            {
                rotRightTime.reset();
                if (tfod != null)
                {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    rotateLeftP(0.15);
                    if (updatedRecognitions != null)
                    {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        int goldMineralX = -1;
                        int goldMineralXR = -1;
                        int goldMineralCent = -1;
                        int leftRangeX = (1280/2)-50;
                        int rightRangeX = leftRangeX + 100;
                        if(updatedRecognitions.size()>=1)
                        {
                            for(Recognition r: updatedRecognitions)
                            {
                                if (r.getLabel().equals(LABEL_GOLD_MINERAL))
                                {
                                    //goldMineralX = (int) r.getLeft();
                                    //goldMineralXR = (int)r.getRight();
                                    //goldMineralCent = (int)((goldMineralX+goldMineralXR)/2);
                                    //aligned = isAligned(goldMineralCent, 640-75, 640+75);
                                    if(!aligned)
                                    {
                                        telemetry.addLine("---TRYING TO FIND GOLD---");
                                        goldMineralX = (int) r.getLeft();
                                        goldMineralXR = (int) r.getRight();
                                        goldMineralCent = (int) ((goldMineralX + goldMineralXR) / 2);
                                        aligned = isAligned(goldMineralCent, 640-125, 640+125);
                                        rotateLeftP(0.15);
                                        telemetry.addLine("Rotation Time: " + rotationTime);
                                        telemetry.addLine("Rotating left");
                                        telemetry.addLine("Aligned: " + aligned);
                                        if(aligned)
                                        {
                                            rotateBack = rotRightTime.milliseconds()/1000;
                                            detect = false;
                                        }
                                    }
                                }
                            }
                        }
                        telemetry.addLine("Gold cords: (" + goldMineralX + " to " + goldMineralXR + ")");
                        telemetry.addLine("Gold Center x (" + goldMineralCent + ")");
                        telemetry.addData("Position of Gold", pos);
                        telemetry.addData("Gold Mineral Aligned", aligned);
                        telemetry.update();
                    }
                }
            }
            forward();
            runtime.reset();
            while(opModeIsActive() && runtime.seconds() < 1.5)
            {
                telemetry.addLine("Hitting Mineral");
                telemetry.update();
            }
            backward();
            runtime.reset();
            while(opModeIsActive() && runtime.seconds() < 1)
            {
                telemetry.addLine("Moving back");
                telemetry.update();
            }
            rotateRightP(0.15);
            runtime.reset();
            while(opModeIsActive() && runtime.seconds() <= rotateBack)
            {
                telemetry.addData("Rotating back", "Time Elapsed:", runtime.seconds(), "\nTarget Time:", rotateBack);
                telemetry.update();
            }
            left();
            runtime.reset();
            while(opModeIsActive() && runtime.seconds() <= 2)
            {
                telemetry.addLine("Moving Past Minerals");
                telemetry.update();
            }
            rotateLeft();
            runtime.reset();
            while(opModeIsActive() && runtime.seconds() <= 1.5)
            {
                telemetry.addLine("Aligning with Depot");
                telemetry.update();
            }
            backward();
            runtime.reset();
            while(opModeIsActive() && runtime.seconds() <= 3)
            {
                telemetry.addLine("Backing Up Into Depot");
                telemetry.update();
            }
            runtime.reset();
            while(opModeIsActive() && runtime.seconds() <= 3)
            {
                telemetry.addLine("Deploying Marker");
                telemetry.update();
            }
            rotateRight();
            while(opModeIsActive() && runtime.seconds() <= 0.6)
            {
                telemetry.addLine("To Crater");
                telemetry.update();
            }
            forward();
            while(opModeIsActive() && runtime.seconds() <= 5)
            {
                telemetry.addLine("Parking");
                telemetry.update();
            }
            stop();
            telemetry.addData("Path", "Complete");
            telemetry.update();
            sleep(1000);
        }
        if (tfod != null)
        {
            tfod.shutdown();
        }
        /*
        //Stop the autonomous program
        stop();
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
        */
    }

    /**
     * METHODS BASED ON TIME
     */
    public void stop(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while (timer.seconds() <= time)
        {
            slave.frontL.setPower(0);
            slave.backL.setPower(0);
            slave.frontR.setPower(0);
            slave.backR.setPower(0);
        }
    }
    public void forwardS(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            slave.frontL.setPower(-SPEED);
            slave.backL.setPower(-SPEED);
            slave.frontR.setPower(SPEED);
            slave.backR.setPower(SPEED);
        }
        stop(0.5);
    }
    public void backwardS(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            slave.frontL.setPower(SPEED);
            slave.backL.setPower(SPEED);
            slave.frontR.setPower(-SPEED);
            slave.backR.setPower(-SPEED);
        }
        stop(0.5);
    }
    public void rightS(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            slave.frontL.setPower(-SPEED);
            slave.frontR.setPower(-SPEED);
            slave.backR.setPower(SPEED);
            slave.backL.setPower(SPEED);
        }
        stop(0.5);
    }
    public void leftS(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            slave.frontL.setPower(SPEED);
            slave.frontR.setPower(SPEED);
            slave.backL.setPower(-SPEED);
            slave.backR.setPower(-SPEED);
        }
        stop(0.5);
    }
    public void rotateRightS(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            slave.frontL.setPower(-SPEED);
            slave.frontR.setPower(-SPEED);
            slave.backL.setPower(-SPEED);
            slave.backR.setPower(-SPEED);
        }
        stop(0.5);
    }
    public void rotateLeftS(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            slave.frontL.setPower(SPEED);
            slave.frontR.setPower(SPEED);
            slave.backL.setPower(SPEED);
            slave.backR.setPower(SPEED);
        }
        stop(0.5);
    }

    /**
     * METHODS BASED ON POWER
     */
    public void forwardP(double power)
    {
        slave.frontL.setPower(-power);
        slave.backL.setPower(-power);
        slave.frontR.setPower(power);
        slave.backR.setPower(power);
    }
    public void backwardP(double power)
    {
        slave.frontL.setPower(power);
        slave.backL.setPower(power);
        slave.frontR.setPower(-power);
        slave.backR.setPower(-power);
    }
    public void rightP(double power)
    {
        slave.frontL.setPower(power);
        slave.frontR.setPower(power);
        slave.backR.setPower(-power);
        slave.backL.setPower(-power);
    }
    public void leftP(double power)
    {
        slave.frontL.setPower(-power);
        slave.frontR.setPower(-power);
        slave.backL.setPower(power);
        slave.backR.setPower(power);
    }
    public void rotateRightP(double power)
    {
        slave.frontL.setPower(-power);
        slave.frontR.setPower(-power);
        slave.backL.setPower(-power);
        slave.backR.setPower(-power);
    }
    public void rotateLeftP(double power)
    {
        slave.frontL.setPower(power);
        slave.frontR.setPower(power);
        slave.backL.setPower(power);
        slave.backR.setPower(power);
    }

    /**
     * METHODS BASED ON NO PARAMETERS
     */
    public void forward()
    {
        slave.frontL.setPower(-SPEED);
        slave.backL.setPower(-SPEED);
        slave.frontR.setPower(SPEED);
        slave.backR.setPower(SPEED);
    }
    public void backward()
    {
        slave.frontL.setPower(SPEED);
        slave.backL.setPower(SPEED);
        slave.frontR.setPower(-SPEED);
        slave.backR.setPower(-SPEED);
    }
    public void right()
    {
        slave.frontL.setPower(SPEED);
        slave.frontR.setPower(SPEED);
        slave.backR.setPower(-SPEED);
        slave.backL.setPower(-SPEED);
    }
    public void left()
    {
        slave.frontL.setPower(-SPEED);
        slave.frontR.setPower(-SPEED);
        slave.backL.setPower(SPEED);
        slave.backR.setPower(SPEED);
    }
    public void rotateRight()
    {
        slave.frontL.setPower(-SPEED);
        slave.frontR.setPower(-SPEED);
        slave.backL.setPower(-SPEED);
        slave.backR.setPower(-SPEED);
    }
    public void rotateLeft()
    {
        slave.frontL.setPower(SPEED);
        slave.frontR.setPower(SPEED);
        slave.backL.setPower(SPEED);
        slave.backR.setPower(SPEED);
    }
    /**
     * METHODS FOR TENSOR FLOW (DO NOT TOUCH)
     */
    private void initVuforia()
    {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }
    private void initTfod()
    {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.useObjectTracker = true;
        tfodParameters.minimumConfidence = 0.65;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
    public static boolean isAligned(int cent, int rangeLeft, int rangeRight)
    {
        if(cent >= rangeLeft && cent <= rangeRight)
            return true;
        else
            return false;
    }
    public void findGold()
    {
        stop(2);
        telemetry.addLine("GONNA MOVE");
        telemetry.update();
        //Movement code
        if(pos == -1 && !aligned)
        {
            telemetry.addData("pos", "left");
            telemetry.update();
            while(aligned == false)
            {
                telemetry.addLine("penis moving left");
                telemetry.update();
                rotateLeftS(1);
                aligned = true;
            }
            forwardS(2);
            backwardS(2);
        }
        else if(pos == 1 && !aligned)
        {
            telemetry.addData("pos", "right");
            telemetry.update();
            while (aligned == false)
            {
                telemetry.addLine("penis moving right");
                telemetry.update();
                rotateRightS(1);
            }
            forwardS(2);
            backwardS(2);
        }
        else if(pos == 0)
        {
            telemetry.addLine("PENIS DOING DUMB THING");
            telemetry.update();
            forwardS(2);
            backwardS(2);
        }
    }
}
