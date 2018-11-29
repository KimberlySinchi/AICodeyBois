
/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="AutoDetection", group="DogeCV")

public class autoDetection extends OpMode
{
    // Detector object
    private GoldAlignDetector detector;
    private DcMotor dc1;
    private DcMotor dc2;
    private DcMotor dc3;
    private DcMotor dc4;
    private DcMotor armMotor1;
    private DcMotor armMotor2;
    private DcMotor latchMotor;
    private Servo s1;
    private String status;
    private boolean x = true;
    public autoDetection() {
    }

    @Override
    public void init() {
        telemetry.addData("Status", "DogeCV 2018.0 - Gold Align Example");

        // Set up detector
        detector = new GoldAlignDetector(); // Create detector
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance()); // Initialize it with the app context and camera
        detector.useDefaults(); // Set detector to use default settings

        // Optional tuning

        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005; //

        detector.ratioScorer.weight = 5; //
        detector.ratioScorer.perfectRatio = 1.0; // Ratio adjustment

        detector.enable(); // Start the detector!
        status = "";
        /* */
        telemetry.addData("Status", "Initialized");
        try
        {
            dc1 = hardwareMap.get(DcMotor.class, "DC1");
            //dc1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch(Exception e)
        {
            System.out.println(e + "");
        }
        try
        {
            dc2 = hardwareMap.get(DcMotor.class, "DC2");
        }
        catch(Exception e)
        {
            System.out.println(e + "");
        }
        try
        {
            dc3 = hardwareMap.get(DcMotor.class, "DC3");
        }
        catch(Exception e)
        {
            telemetry.addData("Mistake: " + e, "");
        }
        try
        {

            dc4 = hardwareMap.get(DcMotor.class, "DC4");
        }
        catch(Exception e)
        {
            telemetry.addData("Mistake: " + e, "");
        }
        try
        {
            armMotor1 = hardwareMap.get(DcMotor.class, "DC5");
            armMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }
        catch(Exception e)
        {
            telemetry.addLine("Mistake: " + e);
        }
        try
        {
            armMotor2 = hardwareMap.get(DcMotor.class, "DC6");
            armMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch(Exception e)
        {
            telemetry.addLine("Mistake: " + e);
        }
        try
        {
            s1 = hardwareMap.get(Servo.class, "S1");
            status += "S1 connected";
        }
        catch(Exception e)
        {
            telemetry.addData("Mistake: " + e, "");
            status += "S1 not found";
        }


    }

    /*
     * Code to run REPEATEDLY when the driver hits INIT
     */
    @Override
    public void init_loop()
    {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start()
    {
        //if(x) {
            telemetry.addLine("hello there");
            rotateRightAnalog(.1, 2, true);
            rotateLeftAnalog(.1, 3, true);
            x = false;
        //}
       // else
           // {
        //}



    }

    /*
     * Code to run REPEATEDLY when the driver hits PLAY
     */

    @Override
    public void loop()
    {
        //telemetry.addData("IsAligned" , detector.getAligned()); // Is the bot aligned with the gold mineral?
        telemetry.addData("X Pos" , detector.getXPosition()); // Gold X position.
        start();
    }


    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop()
    {
        // Disable the detector
        telemetry.addLine("hello");
        detector.disable();

    }
    public void extendLatch(double time)
    {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while(ms.time()<=time)
        {
            latchMotor.setPower(1);
        }
        latchMotor.setPower(0);
    }
    public void retractArm(double time)
    {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while(ms.time()<=time)
        {
            latchMotor.setPower(-1);
        }
        latchMotor.setPower(0);
    }
    public void goUp(double time)
    {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while(ms.time()<=time)
        {
            dc1.setPower(1.0);
            dc2.setPower(-1.0);
            dc3.setPower(1.0);
            dc4.setPower(-1.0);
        }
        dc1.setPower(0.0);
        dc2.setPower(0.0);
        dc3.setPower(0.0);
        dc4.setPower(0.0);

    }
    public void goDown(double time)
    {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while(ms.time()<=time)
        {
            dc1.setPower(-1.0);
            dc2.setPower(1.0);
            dc3.setPower(-1.0);
            dc4.setPower(1.0);
        }
        dc1.setPower(0);
        dc2.setPower(0);
        dc3.setPower(0);
        dc4.setPower(0);
    }
    public void goLeft(double time)
    {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while(ms.time()<=time)
        {
            dc1.setPower(-1);
            dc2.setPower(-1);
            dc3.setPower(1);
            dc4.setPower(1);
        }
        dc1.setPower(0);
        dc2.setPower(0);
        dc3.setPower(0);
        dc4.setPower(0);
    }
    public void goRight(double time)
    {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while(ms.time()<=time)
        {
            dc1.setPower(1);
            dc2.setPower(1);
            dc3.setPower(-1);
            dc4.setPower(-1);
        }
        dc1.setPower(0);
        dc2.setPower(0);
        dc3.setPower(0);
        dc4.setPower(0);
    }
    public void rotateRight(double time,boolean bool)
    {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while(ms.time()<=time && bool == false)
        {
            dc1.setPower(1);
            dc2.setPower(1);
            dc3.setPower(1);
            dc4.setPower(1);
        }
        dc1.setPower(0);
        dc2.setPower(0);
        dc3.setPower(0);
        dc4.setPower(0);
    }
    public void rotateLeft(double time, boolean bool)
    {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while(ms.time()<=time && bool == false)
        {
            dc1.setPower(-1);
            dc2.setPower(-1);
            dc3.setPower(-1);
            dc4.setPower(-1);
        }
        dc1.setPower(0);
        dc2.setPower(0);
        dc3.setPower(0);
        dc4.setPower(0);
    }
    public void rotateRightAnalog(double power,double time, boolean bool)
    {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while(ms.time()<=time && bool == false && !isBlock())
        {
            dc1.setPower(power);
            dc2.setPower(power);
            dc3.setPower(power);
            dc4.setPower(power);
        }
        dc1.setPower(0);
        dc2.setPower(0);
        dc3.setPower(0);
        dc4.setPower(0);
    }
    public void rotateLeftAnalog(double power, double time, boolean bool)
    {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while(ms.time()<=time && bool == false && !isBlock())
        {
            dc1.setPower(-power);
            dc2.setPower(-power);
            dc3.setPower(-power);
            dc4.setPower(-power);
        }
        dc1.setPower(0);
        dc2.setPower(0);
        dc3.setPower(0);
        dc4.setPower(0);
    }
    public void rotateSTOP()
    {
        dc1.setPower(0);
        dc2.setPower(0);
        dc3.setPower(0);
        dc4.setPower(0);
    }
    public boolean isBlock()
    {
        return detector.getAligned();
    }

}