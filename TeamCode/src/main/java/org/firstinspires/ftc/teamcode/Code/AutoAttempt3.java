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

@Autonomous(name = "aHHHH pt 2", group = "Slave")

public class AutoAttempt3 extends LinearOpMode
{
    private Slave slave = new Slave();
    private ElapsedTime runtime = new ElapsedTime();

    private ElapsedTime rotLeftTime = new ElapsedTime();
    private double rotationTime = 0;

    private DcMotor frontL;
    private DcMotor frontR;
    private DcMotor backL;
    private DcMotor backR;
    private Servo armSpin;
    private SampleAlignDetector detector;
    static final double SPEED = 0.6;

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
            rotLeftTime.reset();
            while (opModeIsActive() && runtime.seconds() < 10 && detect)
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
                                        telemetry.addLine("TRYING TO FIND GOLD-------");
                                        goldMineralX = (int) r.getLeft();
                                        goldMineralXR = (int) r.getRight();
                                        goldMineralCent = (int) ((goldMineralX + goldMineralXR) / 2);
                                        aligned = isAligned(goldMineralCent, 640-125, 640+125);
                                        rotateLeftP(0.05);
                                        rotationTime = rotLeftTime.time();
                                        telemetry.addLine("Rotation Time: "+rotationTime);
                                        telemetry.addLine("Rotating left");
                                        telemetry.addLine("Aligned: " + aligned);
                                        if(aligned)
                                            detect = false;
                                    }
                                }
                            }
                        }
                        /*if (updatedRecognitions.size() == 3)
                        {
                            goldMineralX = -1;
                            goldMineralXR = -1;
                            goldMineralCent = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;
                            for (Recognition recognition : updatedRecognitions)
                            {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
                                {
                                    goldMineralX = (int) recognition.getLeft();
                                    goldMineralXR = (int) recognition.getRight();
                                    goldMineralCent = (int)((goldMineralX+goldMineralXR))/2;
                                }
                                else if (silverMineral1X == -1)
                                {
                                    silverMineral1X = (int) recognition.getLeft();
                                }
                                else
                                {
                                    silverMineral2X = (int) recognition.getLeft();
                                }
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
                        }*/
                        telemetry.addLine("Gold cords: (" + goldMineralX + " to " + goldMineralXR + ")");
                        telemetry.addLine("Gold Center x (" + goldMineralCent +")");
                        telemetry.addData("Position of Gold", pos);
                        telemetry.addData("Gold Mineral Aligned", aligned);
                        telemetry.update();
                    }
                }
            }
            forward();
            runtime.reset();
            while(opModeIsActive() && runtime.seconds() < 2)
            {
                telemetry.addLine("Moving forward");
                telemetry.addLine("Rotation time: "+rotationTime);
                telemetry.update();
            }
            backward();
            runtime.reset();
            while(opModeIsActive() && runtime.seconds() < 2)
            {
                telemetry.addLine("Moving backward");
                telemetry.update();
            }
            /*rotateRight();
            runtime.reset();
            while(opModeIsActive() && runtime.seconds() < rotationTime)
            {
                telemetry.addLine("hi, we're rotating back");
                telemetry.update();
            }*/

        }
        if (tfod != null)
        {
            tfod.shutdown();
        }


        //Driving forward for 1.4 seconds
        /*
        forward(0.2);
        while (opModeIsActive() && (runtime.seconds() < 1.4))
        {
            telemetry.addData("Moving Forward", "Time Elapsed:", runtime.seconds());
            telemetry.update();
        }
        //Rotating left for 1.3 seconds
        rotateLeft(0.2);
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1.3)
        {
            telemetry.addData("Rotating Left", "Time Elapsed:", runtime.seconds());
            telemetry.update();
        }
        //Rotating right for 1.3 seconds
        rotateRight(0.2);
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1.3)
        {
            telemetry.addData("Rotating Right", "Time Elapsed:", runtime.seconds());
            telemetry.update();
        }
        //Moving left for 2 seconds
        left(0.2);
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
        */

        //THIS WILL NOT WORK WITH POWER METHODS BECAUSE IT WILL ONLY JOLT FOR A SECOND BEFORE STOPPING
        /*
        while(opModeIsActive())
        {
            if(runtime.seconds() < 1.4)
                forwardP(0.2);
            runtime.reset();
            if(runtime.seconds() < 1.3)
                rotateLeftP(0.2);
            runtime.reset();
            if(runtime.seconds() < 1.3)
                rotateRightP(0.2);
            runtime.reset();
            if(runtime.seconds() < 2)
                leftP(0.2);
            stop();
            sleep(1000);
        }
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
