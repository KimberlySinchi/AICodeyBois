package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SlaveAuto
{
    /* Declare OpMode members. */
    public DcMotor frontL;
    public DcMotor frontR;
    public DcMotor backR;
    public DcMotor backL;
    public DcMotor latch;
    public String status;

    HardwareMap hwmap = null; //Need a reference to hardware map because otherwise, the code will think this is an opmode to use right now

    public SlaveAuto()
    {
    }

    public void init(HardwareMap ahwmap)
    {
        hwmap = ahwmap;
        try
        {
            frontL = hwmap.get(DcMotor.class, "DC1");
            frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        catch(Exception e)
        {
            status += "\nFrontL (DC1) motor not mapping";
        }
        try
        {
            frontR = hwmap.get(DcMotor.class, "DC2");
            frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        catch(Exception e)
        {
            status += "\nFrontR (DC2) motor not mapping";
        }
        try
        {
            backR = hwmap.get(DcMotor.class, "DC3");
            backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        catch(Exception e)
        {
            status += "\nBackR (DC3) motor not mapping";
        }
        try
        {
            backL = hwmap.get(DcMotor.class, "DC4");
            backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        catch(Exception e)
        {
            status += "\nBackL (DC4) motor not mapping";
        }
        try
        {
            latch = hwmap.get(DcMotor.class, "latch");
            latch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        catch(Exception e)
        {
            status += "\nLatch (latch) motor not mapping";
        }
    }
    public String getStatus()
    {
        return status;
    }
}