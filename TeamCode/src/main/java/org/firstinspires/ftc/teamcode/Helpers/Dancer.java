package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Dancer
{
    /* Declare OpMode members. */
    public DcMotor front;
    public DcMotor left;
    public DcMotor back;
    public DcMotor right;
    public DcMotor latchDown;
    public DcMotor latchUp;
    public DcMotor armUaD;
    public DcMotor armFaB;
    public String status = "";

    public Servo armIntake;
    public Servo mineralServo;
    public Servo markerServo;

    HardwareMap hwmap = null;

    public Dancer()
    {

    }

    public void init(HardwareMap ahwmap)
    {
        hwmap = ahwmap;

        //4 MOVEMENT MOTORS
        try
        {
            front = hwmap.get(DcMotor.class, "DC4");
            front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //w/o before
        }
        catch (Exception e)
        {
            status += "\nFront (DC4) motor not mapping";
        }
        try
        {
            right = hwmap.get(DcMotor.class, "DC1");
            right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch (Exception e)
        {
            status += "\nRight (DC1) motor not mapping";
        }
        try
        {
            back = hwmap.get(DcMotor.class, "DC2");
            back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch (Exception e)
        {
            status += "\nBack (DC2) motor not mapping";
        }
        try
        {
            left = hwmap.get(DcMotor.class, "DC3");
            left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch (Exception e)
        {
            status += "\nLeft (DC3) motor not mapping";
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

        //ARM MOTORS
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
        try
        {
            armFaB = hwmap.get(DcMotor.class, "DC8");
            armFaB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            armFaB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (Exception e)
        {
            status += "\nArm F&B failed to initialize";
        }

        //SERVOS
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
            mineralServo = hwmap.get(Servo.class, "S3");
        }
        catch (Exception e)
        {
            status += "\nMineral servo not mapping";
        }
        try
        {
            markerServo = hwmap.get(Servo.class, "S4");
        }
        catch (Exception e)
        {
            status += "\nMarker servo not mapping";
        }
    }

    public String getStatus()
    {
        return status;
    }
}