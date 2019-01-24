package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Slave {
    /* Declare OpMode members. */
    public DcMotor frontL;
    public DcMotor frontR;
    public DcMotor backR;
    public DcMotor backL;
    public DcMotor latch;
    public DcMotor armFaB;
    public DcMotor armUaD;
    public String status = "";
    public Servo armIntake;

    HardwareMap hwmap = null; //Need a reference to hardware map because otherwise, the code will think this is an opmode to use right now

    public Slave() {
    }

    public void init(HardwareMap ahwmap) {
        hwmap = ahwmap;
        try {
            frontL = hwmap.get(DcMotor.class, "DC1");
            frontL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {
            status += "\nFrontL (DC1) motor not mapping";
        }
        try {
            frontR = hwmap.get(DcMotor.class, "DC2");
            frontR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {
            status += "\nFrontR (DC2) motor not mapping";
        }
        try {
            backR = hwmap.get(DcMotor.class, "DC3");
            backR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {
            status += "\nBackR (DC3) motor not mapping";
        }
        try {
            backL = hwmap.get(DcMotor.class, "DC4");
            backL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {
            status += "\nBackL (DC4) motor not mapping";
        }
        try
        {
            latch = hwmap.get(DcMotor.class, "DC5");
            latch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            latch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            status += "\nLatch (latch) motor not mapping";
        }
        try {
            armIntake = hwmap.get(Servo.class, "S1");
        } catch (Exception e) {
            status += "\nArm Intake servo not mapping";
        }
        try {
            armFaB = hwmap.get(DcMotor.class, "DC7");
            armFaB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            armFaB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            status += "Arm F&B failed to initialize";
        }
        try {
            armUaD = hwmap.get(DcMotor.class, "DC8");
            armUaD.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            armUaD.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            status += "Arm U&D failed to initialize";
        }
    }

    public String getStatus() {
        return status;
    }
}