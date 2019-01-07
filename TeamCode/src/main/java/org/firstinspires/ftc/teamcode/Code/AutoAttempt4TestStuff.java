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
import org.firstinspires.ftc.teamcode.Helpers.SlaveAuto;

import java.util.List;

@Autonomous(name = "sugondese", group = "Slave")

public class AutoAttempt4TestStuff extends LinearOpMode {
    private SlaveAuto slave = new SlaveAuto();
    private ElapsedTime runtime = new ElapsedTime();

    private int rotVal = 0;

    private DcMotor frontL;
    private DcMotor frontR;
    private DcMotor backL;
    private DcMotor backR;
    private Servo armSpin;
    private SampleAlignDetector detector;
    static final double SPEED = 0.6;

    private static final int COUNTS_PER_MOTOR_REV = 1440;
    private static final double DRIVE_GEAR_REDUCTION = 2.0; //This value has yet to be discovered

    private static final double WHEEL_DIAMETER_INCHES = 3.78;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    private static final double ROBOT_DIAMETER_INCHES = 17.5;
    private static final double COUNTS_PER_NINETY_DEG = (3.1415 * ROBOT_DIAMETER_INCHES / 4) * COUNTS_PER_INCH;

    private static final double HOLONOMIC_COMPENSATION_FACTOR = (Math.sin(45) * WHEEL_DIAMETER_INCHES * 3.1415) / (WHEEL_DIAMETER_INCHES * 3.1415);

    private static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

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

        waitForStart();

        forwardE((int)(COUNTS_PER_INCH*8));
        backwardE((int)(COUNTS_PER_INCH*8));
        rotateLeftE((int)(COUNTS_PER_INCH*8));
        rotateRightE((int)(COUNTS_PER_INCH*8));
    }

    /**
     * METHODS BASED ON TIME
     */
    public void stop(double time) {
        ElapsedTime timer = new ElapsedTime();
        while (timer.seconds() <= time) {
            slave.frontL.setPower(0);
            slave.backL.setPower(0);
            slave.frontR.setPower(0);
            slave.backR.setPower(0);
        }
    }

    public void forwardS(double time) {
        ElapsedTime timer = new ElapsedTime();
        while (timer.seconds() <= time) {
            slave.frontL.setPower(-SPEED);
            slave.backL.setPower(-SPEED);
            slave.frontR.setPower(SPEED);
            slave.backR.setPower(SPEED);
        }
        stop(0.5);
    }

    public void backwardS(double time) {
        ElapsedTime timer = new ElapsedTime();
        while (timer.seconds() <= time) {
            slave.frontL.setPower(SPEED);
            slave.backL.setPower(SPEED);
            slave.frontR.setPower(-SPEED);
            slave.backR.setPower(-SPEED);
        }
        stop(0.5);
    }

    public void rightS(double time) {
        ElapsedTime timer = new ElapsedTime();
        while (timer.seconds() <= time) {
            slave.frontL.setPower(-SPEED);
            slave.frontR.setPower(-SPEED);
            slave.backR.setPower(SPEED);
            slave.backL.setPower(SPEED);
        }
        stop(0.5);
    }

    public void leftS(double time) {
        ElapsedTime timer = new ElapsedTime();
        while (timer.seconds() <= time) {
            slave.frontL.setPower(SPEED);
            slave.frontR.setPower(SPEED);
            slave.backL.setPower(-SPEED);
            slave.backR.setPower(-SPEED);
        }
        stop(0.5);
    }

    public void rotateRightS(double time) {
        ElapsedTime timer = new ElapsedTime();
        while (timer.seconds() <= time) {
            slave.frontL.setPower(-SPEED);
            slave.frontR.setPower(-SPEED);
            slave.backL.setPower(-SPEED);
            slave.backR.setPower(-SPEED);
        }
        stop(0.5);
    }

    public void rotateLeftS(double time) {
        ElapsedTime timer = new ElapsedTime();
        while (timer.seconds() <= time) {
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
    public void forwardP(double power) {
        slave.frontL.setPower(-power);
        slave.backL.setPower(-power);
        slave.frontR.setPower(power);
        slave.backR.setPower(power);
    }

    public void backwardP(double power) {
        slave.frontL.setPower(power);
        slave.backL.setPower(power);
        slave.frontR.setPower(-power);
        slave.backR.setPower(-power);
    }

    public void rightP(double power) {
        slave.frontL.setPower(power);
        slave.frontR.setPower(power);
        slave.backR.setPower(-power);
        slave.backL.setPower(-power);
    }

    public void leftP(double power) {
        slave.frontL.setPower(-power);
        slave.frontR.setPower(-power);
        slave.backL.setPower(power);
        slave.backR.setPower(power);
    }

    public void rotateRightP(double power) {
        slave.frontL.setPower(-power);
        slave.frontR.setPower(-power);
        slave.backL.setPower(-power);
        slave.backR.setPower(-power);
    }

    public void rotateLeftP(double power) {
        slave.frontL.setPower(power);
        slave.frontR.setPower(power);
        slave.backL.setPower(power);
        slave.backR.setPower(power);
    }

    /**
     * METHODS BASED ON NO PARAMETERS
     */
    public void forward() {
        slave.frontL.setPower(-SPEED);
        slave.backL.setPower(-SPEED);
        slave.frontR.setPower(SPEED);
        slave.backR.setPower(SPEED);
    }

    public void backward() {
        slave.frontL.setPower(SPEED);
        slave.backL.setPower(SPEED);
        slave.frontR.setPower(-SPEED);
        slave.backR.setPower(-SPEED);
    }

    public void right() {
        slave.frontL.setPower(SPEED);
        slave.frontR.setPower(SPEED);
        slave.backR.setPower(-SPEED);
        slave.backL.setPower(-SPEED);
    }

    public void left() {
        slave.frontL.setPower(-SPEED);
        slave.frontR.setPower(-SPEED);
        slave.backL.setPower(SPEED);
        slave.backR.setPower(SPEED);
    }

    public void rotateRight() {
        slave.frontL.setPower(-SPEED);
        slave.frontR.setPower(-SPEED);
        slave.backL.setPower(-SPEED);
        slave.backR.setPower(-SPEED);
    }

    public void rotateLeft() {
        slave.frontL.setPower(SPEED);
        slave.frontR.setPower(SPEED);
        slave.backL.setPower(SPEED);
        slave.backR.setPower(SPEED);
    }

    /**
     * ENCODER METHODS (TICKS)
     * In order to convert to inches, use the final double:
     * COUNTS_PER_INCH (FOR STRAIGHT MOVEMENT)
     * In order to convert to degrees, use the final double:
     * COUNTS_PER_NINETY_DEG (FOR ROTATIONAL MOVEMENT)
     */
    public void encoderReset()
    {
        slave.frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void toPos()
    {
        slave.frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void power()
    {
        slave.frontL.setPower(DRIVE_SPEED);
        slave.frontR.setPower(DRIVE_SPEED);
        slave.backL.setPower(DRIVE_SPEED);
        slave.backR.setPower(DRIVE_SPEED);
    }
    public void usingEncoders()
    {
        slave.frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void forwardE(int ticks)
    {
        //Sets encoder values to 0
        encoderReset();

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(-ticks);
        slave.frontR.setTargetPosition(ticks);
        slave.backL.setTargetPosition(-ticks);
        slave.backR.setTargetPosition(ticks);

        //Prepares motors to run towards position
        toPos();

        //Makes the motors move
        power();

        while (opModeIsActive() && slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy())
        {
            //Does nothing, just makes the method stuck in a while loop until it's done
        }

        //Sets motor mode back to encoder
        stop();
        usingEncoders();
    }

    public void backwardE(int ticks)
    {
        encoderReset();

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(ticks);
        slave.frontR.setTargetPosition(-ticks);
        slave.backL.setTargetPosition(ticks);
        slave.backR.setTargetPosition(-ticks);

        toPos();
        power();

        while (opModeIsActive() && slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy())
        {
            //does nothing, just makes the method stuck in a while loop until it's done
        }

        usingEncoders();
    }

    public void rotateRightE(int ticks)
    {
        encoderReset();

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(-ticks);
        slave.frontR.setTargetPosition(-ticks);
        slave.backL.setTargetPosition(-ticks);
        slave.backR.setTargetPosition(-ticks);

        toPos();
        power();

        while (opModeIsActive() && slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy())
        {
            //does nothing, just makes the method stuck in a while loop until it's done
        }

        usingEncoders();
    }

    public void rotateLeftE(int ticks)
    {
        //Sets encoder values to 0
        usingEncoders();

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(ticks);
        slave.frontR.setTargetPosition(ticks);
        slave.backL.setTargetPosition(ticks);
        slave.backR.setTargetPosition(ticks);

        toPos();
        power();

        while (slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy())
        {
            //does nothing, just makes the method stuck in a while loop until it's done
        }

        //Sets motor mode back to encoder
        //This also makes it so we avoid stopping the robot because motors are no longer in run to pos mode
        usingEncoders();
    }
}
