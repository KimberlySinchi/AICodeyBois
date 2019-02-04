package org.firstinspires.ftc.teamcode.Code;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Helpers.Slave;

import java.util.List;

@Autonomous(name = "Tensor With Encoders W/ 3", group = "Slave")

public class AutoAttemptEncoders3 extends LinearOpMode
{
    private Slave slave = new Slave();
    private ElapsedTime runtime = new ElapsedTime();
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

        //Setup for Tensor Flow detector
        initVuforia();
        initTfod();
        tfod.activate();

        waitForStart();
        runtime.reset();

        //Finding the position of the gold mineral
        if(opModeIsActive())
        {
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
                                    if(!aligned)
                                    {
                                        telemetry.addLine("---TRYING TO FIND GOLD---");
                                        goldMineralX = (int) r.getLeft();
                                        goldMineralXR = (int) r.getRight();
                                        goldMineralCent = (int) ((goldMineralX + goldMineralXR) / 2);
                                        aligned = isAligned(goldMineralCent, 640-125, 640+125);
                                        rotateLeftP(0.1);
                                        telemetry.addLine("Rotating left");
                                        telemetry.addLine("Aligned: " + aligned);
                                        if(aligned)
                                            detect = false;
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
                                    /*if(!aligned)
                                    {
                                        telemetry.addLine("---TRYING TO FIND GOLD---");
                                        goldMineralCent = (goldMineralX + goldMineralXR) / 2;
                                        aligned = isAligned(goldMineralCent, 640-125, 640+125);
                                        rotateLeftP(0.1);
                                        telemetry.addLine("Rotating left");
                                        telemetry.addLine("Aligned: " + aligned);
                                        if(aligned)
                                            detect = false;
                                    }*/
                                }
                                else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X)
                                {
                                    telemetry.addData("Gold Mineral Position", "Right");
                                    telemetry.addLine("Right code was updated");
                                    pos = 1;
                                    /*if(!aligned)
                                    {
                                        telemetry.addLine("---TRYING TO FIND GOLD---");
                                        goldMineralCent = (goldMineralX + goldMineralXR) / 2;
                                        aligned = isAligned(goldMineralCent, 640-125, 640+125);
                                        rotateRightP(0.1);
                                        telemetry.addLine("Rotating right");
                                        telemetry.addLine("Aligned: " + aligned);
                                        if(aligned)
                                            detect = false;
                                    }
                                    telemetry.update();*/
                                }
                                else if(goldMineralX > silverMineral1X && goldMineralX < silverMineral2X ||
                                        goldMineralX < silverMineral1X && goldMineralX > silverMineral2X)
                                {
                                    telemetry.addData("Gold Mineral Position", "Center");
                                    telemetry.addLine("Center code was updated");
                                    pos = 0;
                                    telemetry.update();
                                    detect = false;
                                }
                            }
                        }
                        /*telemetry.addLine("Gold cords: (" + goldMineralX + " to " + goldMineralXR + ")");
                        telemetry.addLine("Gold Center x (" + goldMineralCent + ")");
                        telemetry.addData("Position of Gold", pos);
                        telemetry.addData("Gold Mineral Aligned", aligned);
                        telemetry.update();*/
                    }
                }
            }
            int rotateBackTicks = slave.frontL.getCurrentPosition();
            forwardE(2880);
            backwardE(2880);
            rotateRightE(rotateBackTicks);
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

    public void nStop()
    {
        slave.backR.setPower(0);
        slave.frontL.setPower(0);
        slave.backL.setPower(0);
        slave.frontR.setPower(0);
    }
    public void stopT(double seconds)
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
    public void encodeResetAndRun()
    {
        slave.frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void power()
    {
        slave.frontL.setPower(SPEED);
        slave.frontR.setPower(SPEED);
        slave.backL.setPower(SPEED);
        slave.backR.setPower(SPEED);
    }
    public void forwardE(int ticks)
    {
        //Sets encoder values to 0 and sets the motors in RUN_TO_POSITION mode
        encodeResetAndRun();

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(-ticks);
        slave.frontR.setTargetPosition(ticks);
        slave.backL.setTargetPosition(-ticks);
        slave.backR.setTargetPosition(ticks);

        //Makes the motors move
        power();

        while (opModeIsActive() && slave.frontL.isBusy())
        {
            telemetry.addLine("" + slave.frontL.getCurrentPosition());
            telemetry.addLine("" + slave.frontR.getCurrentPosition());
            telemetry.addLine("" + slave.backL.getCurrentPosition());
            telemetry.addLine("" + slave.backR.getCurrentPosition());
            telemetry.addData("Moving Forward", ticks);
            telemetry.update();
        }
        nStop();
    }
    public void backwardE(int ticks)
    {
        //Sets encoder values to 0 and sets the motors in RUN_TO_POSITION mode
        encodeResetAndRun();

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(ticks);
        slave.frontR.setTargetPosition(-ticks);
        slave.backL.setTargetPosition(ticks);
        slave.backR.setTargetPosition(-ticks);

        power();

        while (opModeIsActive() && slave.frontL.isBusy())
        {
            telemetry.addLine("" + slave.frontL.getCurrentPosition());
            telemetry.addLine("" + slave.frontR.getCurrentPosition());
            telemetry.addLine("" + slave.backL.getCurrentPosition());
            telemetry.addLine("" + slave.backR.getCurrentPosition());
            telemetry.addData("Moving Backward", ticks);
            telemetry.update();
        }
        nStop();
    }

    public void rotateRightE(int ticks)
    {
        //Sets encoder values to 0 and sets the motors in RUN_TO_POSITION mode
        encodeResetAndRun();

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(-ticks);
        slave.frontR.setTargetPosition(-ticks);
        slave.backL.setTargetPosition(-ticks);
        slave.backR.setTargetPosition(-ticks);

        power();

        while (opModeIsActive() && slave.frontL.isBusy())
        {
            telemetry.addLine("" + slave.frontL.getCurrentPosition());
            telemetry.addLine("" + slave.frontR.getCurrentPosition());
            telemetry.addLine("" + slave.backL.getCurrentPosition());
            telemetry.addLine("" + slave.backR.getCurrentPosition());
            telemetry.addData("Rotating Right", ticks);
            telemetry.update();
        }
        nStop();
    }

    public void rotateLeftE(int ticks)
    {
        //Sets encoder values to 0 and sets the motors in RUN_TO_POSITION mode
        encodeResetAndRun();

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(ticks);
        slave.frontR.setTargetPosition(ticks);
        slave.backL.setTargetPosition(ticks);
        slave.backR.setTargetPosition(ticks);

        power();

        while (opModeIsActive() && slave.frontL.isBusy())
        {
            telemetry.addLine("" + slave.frontL.getCurrentPosition());
            telemetry.addLine("" + slave.frontR.getCurrentPosition());
            telemetry.addLine("" + slave.backL.getCurrentPosition());
            telemetry.addLine("" + slave.backR.getCurrentPosition());
            telemetry.addData("Rotating Left", ticks);
            telemetry.update();
        }
        nStop();
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
}