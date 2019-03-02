package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class SlaveOfficial
{
    /* Declare OpMode members. */
    public DcMotor frontL;
    public DcMotor frontR;
    public DcMotor backR;
    public DcMotor backL;
    public DcMotor latchDown;
    public DcMotor latchUp;
    public DcMotor armUaD;
    public String status = "";

    public Servo armIntake;
    public Servo armFaB;
    public Servo goldServo;
    public Servo silverServo;

    HardwareMap hwmap = null; //Need a reference to hardware map because otherwise, the code will think this is an opmode to use right now

    public SlaveOfficial()
    {

    }

    public void init(HardwareMap ahwmap)
    {
        hwmap = ahwmap;

        //4 MOVEMENT MOTORS
        try
        {
            frontL = hwmap.get(DcMotor.class, "DC3");
            frontL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch (Exception e)
        {
            status += "\nFrontL (DC1) motor not mapping";
        }
        try
        {
            frontR = hwmap.get(DcMotor.class, "DC4");
            frontR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch (Exception e)
        {
            status += "\nFrontR (DC2) motor not mapping";
        }
        try
        {
            backR = hwmap.get(DcMotor.class, "DC1");
            backR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch (Exception e)
        {
            status += "\nBackR (DC3) motor not mapping";
        }
        try
        {
            backL = hwmap.get(DcMotor.class, "DC2");
            backL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch (Exception e)
        {
            status += "\nBackL (DC4) motor not mapping";
        }

        //LATCH MOTORS
        try
        {
            latchDown = hwmap.get(DcMotor.class, "DC5");
            latchDown.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            latchDown.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (Exception e)
        {
            status += "\nBottom Gearbox motor not mapping";
        }
        try
        {
            latchUp = hwmap.get(DcMotor.class, "DC6");
            latchUp.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            latchUp.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (Exception e)
        {
            status += "\nTop Gearbox motor not mapping";
        }

        //ARM MOTOR
        try
        {
            armUaD = hwmap.get(DcMotor.class, "DC7");
            armUaD.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            armUaD.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (Exception e)
        {
            status += "\nArm U&D failed to initialize";
        }

        //ARM SERVOS
        try
        {
            armIntake = hwmap.get(Servo.class, "S1");
        }
        catch (Exception e)
        {
            status += "\nArm Intake servo not mapping";
        }
        try
        {
            armFaB = hwmap.get(Servo.class, "S2");
        }
        catch (Exception e)
        {
            status += "\nArm Extension servo not mapping";
        }

        //DIVIDER SERVOS
        try
        {
            goldServo = hwmap.get(Servo.class, "S3");
        }
        catch(Exception e)
        {
            status += "\nGold Divider not mapping";
        }
        try
        {
            silverServo = hwmap.get(Servo.class, "S4");
        }
        catch(Exception e)
        {
            status += "\nSilver Divider not mapping";
        }
    }

    public String getStatus()
    {
        return status;
    }
}