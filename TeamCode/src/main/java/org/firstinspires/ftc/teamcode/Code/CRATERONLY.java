package org.firstinspires.ftc.teamcode.Code;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Helpers.SlaveAuto;

import java.util.List;

@Autonomous(name = "CRATER Only", group = "Slave")

public class CRATERONLY extends LinearOpMode
{
    private SlaveAuto slave = new SlaveAuto();
    private ElapsedTime runtime = new ElapsedTime();
    static double SPEED = 1;
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
    private boolean detect = true,testing=true;
    private double rotateBack = 0.0;

    @Override
    public void runOpMode()
    {
        slave.init(hardwareMap);

        //For convenience, we will print on the phone that the robot is ready
        telemetry.addData("Status", "Ready to run"); //Same as System.out.println();
        telemetry.addData("Pos", pos);
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
            if(!testing)
            {
                leftE(5000);
            }
            else
            {
                /*latchExtendE(8400);
                rightE(400);
                SPEED = 0.6;
                forwardE(300);
                leftE(400);
                backwardE(150);
                SPEED = 1;*/
                while (opModeIsActive() && runtime.seconds() < 10 && detect)
                {
                    if (tfod != null)
                    {
                        // getUpdatedRecognitions() will return null if no new information is available since
                        // the last time that call was made.
                        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                        if (updatedRecognitions != null) {
                            telemetry.addData("# Object Detected", updatedRecognitions.size());
                            int goldMineralX = -1;
                            int goldMineralXR = -1;
                            int goldMineralCent = -1;
                            int leftRangeX = (1280 / 2) - 50;
                            int rightRangeX = leftRangeX + 100;
                            if (updatedRecognitions.size() >= 3) {
                                for (Recognition r : updatedRecognitions) {
                                    if (r.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                        goldMineralX = (int) r.getLeft();
                                        goldMineralXR = (int) r.getRight();
                                        goldMineralCent = (int) ((goldMineralX + goldMineralXR) / 2);
                                        aligned = isAligned(goldMineralCent, 640 - 125, 640 + 125);
                                        if (aligned) {
                                            detect = false;
                                            pos = 0;
                                        }
                                        if (r.getLeft() < 640 - 125) {
                                            pos = -1;
                                            telemetry.addLine("LEFT");
                                            telemetry.update();
                                            detect = false;
                                        } else if (r.getLeft() > 640 + 125) {
                                            pos = 1;
                                            telemetry.addLine("RIGHT");
                                            telemetry.update();
                                            detect = false;
                                        } else {
                                            pos = 0;
                                            telemetry.addLine("MIDDLE");
                                            telemetry.update();
                                            detect = false;
                                        }
                                    }
                                }
                            } else if (updatedRecognitions.size() == 3) {
                                goldMineralX = -1;
                                goldMineralXR = -1;
                                goldMineralCent = -1;
                                int silverMineral1X = -1;
                                int silverMineral2X = -1;
                                for (Recognition recognition : updatedRecognitions) {
                                    //Getting coordinates of the mineral (leftMost pixel location of the box created around them)
                                    if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                        goldMineralX = (int) recognition.getLeft();
                                        goldMineralXR = (int) recognition.getRight();
                                        goldMineralCent = (goldMineralX + goldMineralXR) / 2;
                                    } else if (silverMineral1X == -1)
                                        silverMineral1X = (int) recognition.getLeft();
                                    else
                                        silverMineral2X = (int) recognition.getLeft();
                                }
                                if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                                    if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                        telemetry.addData("Gold Mineral Position", "Left");
                                        telemetry.addLine("Left code was updated");
                                        pos = -1;
                                        telemetry.update();
                                        detect = false;
                                    } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                        telemetry.addData("Gold Mineral Position", "Right");
                                        telemetry.addLine("Right code was updated");
                                        pos = 1;
                                        telemetry.update();
                                        detect = false;
                                    } else if (goldMineralX > silverMineral1X && goldMineralX < silverMineral2X ||
                                            goldMineralX < silverMineral1X && goldMineralX > silverMineral2X) {
                                        telemetry.addData("Gold Mineral Position", "Center");
                                        telemetry.addLine("Center code was updated");
                                        pos = 0;
                                        telemetry.update();
                                        detect = false;
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
                int rotateBackTicks = slave.frontL.getCurrentPosition();
                if (pos == 0 || pos == -2)
                    forwardE(4800);
                else
                    forwardE(1500);
                if (pos == -1)
                {
                    leftE(1720);
                    forwardE(3300);
                }
                else if (pos == 1)
                {
                    rightE(1720);
                    forwardE(3300);
                }
            }
        }
        if (tfod != null)
        {
            tfod.shutdown();
        }
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
            telemetry.addLine("Latch: " + slave.latchUp.getCurrentPosition());
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
            telemetry.addLine("Latch: " + slave.latchUp.getCurrentPosition());
            telemetry.addData("Moving Backward", ticks);
            telemetry.update();
        }
        nStop();
    }
    public void leftE(int ticks)
    {
        //Sets encoder values to 0 and sets the motors in RUN_TO_POSITION mode
        encodeResetAndRun();

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(ticks);
        slave.frontR.setTargetPosition(ticks);
        slave.backL.setTargetPosition(-ticks);
        slave.backR.setTargetPosition(-ticks);

        power();

        while (opModeIsActive() && slave.frontL.isBusy())
        {
            telemetry.addLine("" + slave.frontL.getCurrentPosition());
            telemetry.addLine("" + slave.frontR.getCurrentPosition());
            telemetry.addLine("" + slave.backL.getCurrentPosition());
            telemetry.addLine("" + slave.backR.getCurrentPosition());
            telemetry.addLine("Latch: " + slave.latchUp.getCurrentPosition());
            telemetry.addData("Moving Backward", ticks);
            telemetry.update();
        }
        nStop();
    }
    public void rightE(int ticks)
    {
        //Sets encoder values to 0 and sets the motors in RUN_TO_POSITION mode
        encodeResetAndRun();

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(-ticks);
        slave.frontR.setTargetPosition(-ticks);
        slave.backL.setTargetPosition(ticks);
        slave.backR.setTargetPosition(ticks);

        power();

        while (opModeIsActive() && slave.frontL.isBusy())
        {
            telemetry.addLine("" + slave.frontL.getCurrentPosition());
            telemetry.addLine("" + slave.frontR.getCurrentPosition());
            telemetry.addLine("" + slave.backL.getCurrentPosition());
            telemetry.addLine("" + slave.backR.getCurrentPosition());
            telemetry.addLine("Latch: " + slave.latchUp.getCurrentPosition());
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
    public void latchExtendE(int ticks)
    {
        //Sets encoder values to 0 and sets the motors in RUN_TO_POSITION mode
        slave.latchUp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.latchDown.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.latchUp.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.latchDown.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Sets the target encoder value to reach
        slave.latchUp.setTargetPosition(ticks);
        slave.latchDown.setTargetPosition(ticks);

        slave.latchUp.setPower(1);
        slave.latchDown.setPower(1);

        while (opModeIsActive() && slave.latchUp.isBusy() && slave.latchUp.getCurrentPosition() < ticks)
        {
            telemetry.addLine("" + slave.latchUp.getCurrentPosition());
            telemetry.addData("Latch Extending", ticks);
            telemetry.addLine("latchUP status " + slave.latchUp.isBusy());
            telemetry.update();
        }
        slave.latchUp.setPower(0);
        slave.latchDown.setPower(0);
    }
    public void latchRetractE(int ticks)
    {
        //Sets encoder values to 0 and sets the motors in RUN_TO_POSITION mode
        slave.latchUp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.latchDown.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.latchUp.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.latchDown.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Sets the target encoder value to reach
        slave.latchUp.setTargetPosition(-ticks);
        slave.latchDown.setTargetPosition(-ticks);

        slave.latchUp.setPower(SPEED);
        slave.latchDown.setPower(SPEED);

        while (opModeIsActive() && slave.latchUp.isBusy())
        {
            telemetry.addLine("" + slave.latchUp.getCurrentPosition());
            telemetry.addData("Latch Retracting", ticks);
            telemetry.update();
        }
        slave.latchUp.setPower(0);
        slave.latchDown.setPower(0);
    }
    public void strafeNW(int ticks)
    {
        //Sets encoder values to 0 and sets the motors in RUN_TO_POSITION mode
        encodeResetAndRun();

        //Sets the target encoder value to reach
        slave.frontR.setTargetPosition(-ticks);
        slave.backL.setTargetPosition(ticks);

        power();

        while (opModeIsActive() && slave.frontL.isBusy())
        {
            telemetry.addLine("" + slave.frontL.getCurrentPosition());
            telemetry.addLine("" + slave.frontR.getCurrentPosition());
            telemetry.addLine("" + slave.backL.getCurrentPosition());
            telemetry.addLine("" + slave.backR.getCurrentPosition());
            telemetry.addData("Strafing NW", ticks);
            telemetry.update();
        }
        nStop();
    }
    public void strafeNE(int ticks)
    {
        //Sets encoder values to 0 and sets the motors in RUN_TO_POSITION mode
        encodeResetAndRun();

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(ticks);
        slave.backR.setTargetPosition(-ticks);

        power();

        while (opModeIsActive() && slave.frontL.isBusy())
        {
            telemetry.addLine("" + slave.frontL.getCurrentPosition());
            telemetry.addLine("" + slave.frontR.getCurrentPosition());
            telemetry.addLine("" + slave.backL.getCurrentPosition());
            telemetry.addLine("" + slave.backR.getCurrentPosition());
            telemetry.addData("Strafing NE", ticks);
            telemetry.update();
        }
        nStop();
    }
    public void strafeSE(int ticks)
    {
        //Sets encoder values to 0 and sets the motors in RUN_TO_POSITION mode
        encodeResetAndRun();

        //Sets the target encoder value to reach
        slave.frontR.setTargetPosition(ticks);
        slave.backL.setTargetPosition(-ticks);

        power();

        while (opModeIsActive() && slave.frontL.isBusy())
        {
            telemetry.addLine("" + slave.frontL.getCurrentPosition());
            telemetry.addLine("" + slave.frontR.getCurrentPosition());
            telemetry.addLine("" + slave.backL.getCurrentPosition());
            telemetry.addLine("" + slave.backR.getCurrentPosition());
            telemetry.addData("Strafing SE", ticks);
            telemetry.update();
        }
        nStop();
    }
    public void strafeSW(int ticks)
    {
        //Sets encoder values to 0 and sets the motors in RUN_TO_POSITION mode
        encodeResetAndRun();

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(-ticks);
        slave.backR.setTargetPosition(ticks);

        power();

        while (opModeIsActive() && slave.frontL.isBusy())
        {
            telemetry.addLine("" + slave.frontL.getCurrentPosition());
            telemetry.addLine("" + slave.frontR.getCurrentPosition());
            telemetry.addLine("" + slave.backL.getCurrentPosition());
            telemetry.addLine("" + slave.backR.getCurrentPosition());
            telemetry.addData("Strafing NW", ticks);
            telemetry.update();
        }
        nStop();
    }
    public void armAwayBase(int ticks)
    {
        slave.armUaD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.armUaD.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slave.armUaD.setTargetPosition(ticks);

        slave.armUaD.setPower(0.6);

        while(opModeIsActive() && slave.armUaD.isBusy())
        {
            telemetry.addLine("" + slave.armUaD.getCurrentPosition());
            telemetry.addData("Moving arm away from base", ticks);
            telemetry.update();
        }
    }
    public void armTowardsBase(int ticks)
    {
        slave.armUaD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.armUaD.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slave.armUaD.setTargetPosition(-ticks);

        slave.armUaD.setPower(0.6);

        while(opModeIsActive() && slave.armUaD.isBusy())
        {
            telemetry.addLine("" + slave.armUaD.getCurrentPosition());
            telemetry.addData("Moving arm towards from base", ticks);
            telemetry.update();
        }
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
