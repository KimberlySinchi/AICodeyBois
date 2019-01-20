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

package org.firstinspires.ftc.teamcode.Code;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Helpers.SlaveAuto;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the AutoRobot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name = "AWESOMEAUTOREVERSE(plsWork)p2", group = "Linear Opmode")
public class dogeAuto6 extends LinearOpMode {

    // Declare OpMode members.
    // Detector object
    public SlaveAuto slave = new SlaveAuto();
    private boolean bool1 = true;
    private boolean bool2 = true;
    private boolean bool3 = true;
    private GoldAlignDetector detector;
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontL;
    private DcMotor frontR;
    private DcMotor backL;
    private DcMotor backR;
    private DcMotor latch;
    private DcMotor arm;
    private Servo armIntake;
    private Servo armSpin;
    static final double SPEED = 1;

    public boolean isBlock() {
        return detector.getAligned();
    }

    @Override
    public void runOpMode() {
        // Set up detector
        detector = new GoldAlignDetector(); // Create detector
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance()); // Initialize it with the app context and camera
        detector.useDefaults(); // Set detector to use default settings

        // Optional tuning
        detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.8; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005; //

        detector.ratioScorer.weight = 5; //
        detector.ratioScorer.perfectRatio = 1.0; // Ratio adjustment

        detector.enable(); // Start the detector!
        try
        {
            frontL = hardwareMap.get(DcMotor.class, "DC1");
            frontL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        } catch (Exception e) {
        }
        try
        {
            frontR = hardwareMap.get(DcMotor.class, "DC2");
            frontR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {
        }
        try
        {
            backR = hardwareMap.get(DcMotor.class, "DC3");
            backR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {
        }
        try
        {
            backL = hardwareMap.get(DcMotor.class, "DC4");
            backL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e)
        {
        }
        try
        {
            latch = hardwareMap.get(DcMotor.class, "DC5");
            latch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            latch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch(Exception e)
        {
            telemetry.addLine(e + "");
        }
        try
        {
            arm  = hardwareMap.get(DcMotor.class, "DC7");
            arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch(Exception e)
        {
            telemetry.addLine("" + e);
        }
        try
        {
            armIntake = hardwareMap.get(Servo.class, "S1");

        }
        catch(Exception e)
        {
            telemetry.addLine("" + e);
        }

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (bool1) {
                /**
                 int timeUntilFirstBlock = 3;
                 double timeLeft = rotateLeft(7, .5);
                 //goUp(1.3); //in the future go back: goBack(1.5); //1.3 seconds with full power
                 //goBack(1.3);//1.3 seconds with full power
                 goUp(2.6,.5);//half power, so double time for equal distance (test)
                 motorsOff(.6);
                 goBack(2.6,.5);//half power, so double time for equal distance(test)
                 motorsOff(.3);
                 rotateRightB(.5,timeLeft - timeUntilFirstBlock);
                 //make sure this works, then continue.
                 **/
                //NEW CODE USING MEASUREMENTS AND VELOCITIES
                latchDown(3.5, .9);
                double timeUntilFirstBlock = angleConv(83); //angles only at .3
                goUp(forwardConv(10), .6);
                double timeLeft = rotateLeft(7, .15);
                /**
                 goUp(forwardConv(70), .6);
                 motorsOff(.6);
                 goDown(forwardConv(42), .6);
                 motorsOff(.6);
                 goUp(forwardConv(26.29),.6);
                 motorsOff(.6);
                 rotateRightB(.3,angleConv(88.99));
                 motorsOff(.6);
                 goUp(forwardConv(31.36),.6);
                 motorsOff(.6);
                 rotateRightB(.3,angleConv(127.69));
                 motorsOff(.6);
                 goUp(forwardConv(39.98),.6);
                 motorsOff(.6);
                 rotateLeftB(.3,angleConv(171.44));
                 motorsOff(.6);
                 goUp(forwardConv(71.89),.6);
                 /* *
                 *
                 */


                /**
                timeUntilFirstBlock = angleConv(83); //angles only at .3
                goUp(forwardConv(10), .6);
                 **/
                timeLeft = rotateLeft(7, .15);
                goUp(forwardConv(70), .6);
                motorsOff(.6);
                goDown(forwardConv(42), .6);
                motorsOff(.6);
                rotateRightB(.11, timeLeft - 2.61);
                motorsOff(.4);
                rotateLeftB(.15, 2.31);
                goUp(forwardConv(80), .6);
                rotateLeftB(.15, 2.6);
                goDown(forwardConv(65.5), .6);
                motorsOff(.6);
                goUp(forwardConv(63.6), .6);
                rotateLeftB(angleConv(98), .15);//this is in degrees ==> it will rotate this many degrees, given that the power is .3
                //Stuff to test for velocity:

                //rotateRightB(.15,10); //at .15 power the robot needs 9 seconds to go one revolution
                //motorsOff(10);
                //goUp(10,.6);


                bool1 = false;
            }
            detector.disable();
        }
    }

    public void rotateRightB(double power, double time) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        boolean totalTime = false;
        double newTime = time * -1;
        if (time > 0) {
            while (opModeIsActive() && ms.time() <= time) {
                frontL.setPower(-power);
                frontR.setPower(-power);
                backR.setPower(-power);
                backL.setPower(-power);

            }
        } else {
            while (opModeIsActive() && ms.time() <= time) {
                frontL.setPower(power);
                frontR.setPower(power);
                backR.setPower(power);
                backL.setPower(power);

            }
        }
        frontL.setPower(0);
        frontR.setPower(0);
        backR.setPower(0);
        backL.setPower(0);
    }

    public void rotateLeftB(double power, double time) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        double newTime = time * -1;
        boolean totalTime = false;
        if (time > 0) {
            while (ms.time() <= time) {
                frontL.setPower(power);
                frontR.setPower(power);
                backR.setPower(power);
                backL.setPower(power);

            }
        } else {
            while (ms.time() <= time) {
                frontL.setPower(-power);
                frontR.setPower(-power);
                backR.setPower(-power);
                backL.setPower(-power);
            }
        }
        frontL.setPower(0);
        frontR.setPower(0);
        backR.setPower(0);
        backL.setPower(0);
    }

    public void motorsOff(double time) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            frontL.setPower(0.0);
            frontR.setPower(0.0);
            backR.setPower(0.0);
            backL.setPower(0.0);
        }

    }

    public void goRightB() {
    }

    public void goLeftB() {
    }

    public void goUp(double time, double power) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            frontL.setPower(-power);
            frontR.setPower(power);
            backR.setPower(power);
            backL.setPower(-power);
        }
        frontL.setPower(0.0);
        frontR.setPower(0.0);
        backR.setPower(0.0);
        backL.setPower(0.0);
    }

    public void goDown(double time, double power) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            frontL.setPower(power);
            frontR.setPower(-power);
            backR.setPower(-power);
            backL.setPower(power);
        }
        frontL.setPower(0);
        frontR.setPower(0);
        backR.setPower(0);
        backL.setPower(0);
    }

    public void goLeft(double time) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            frontL.setPower(-1);
            frontR.setPower(1);
            backR.setPower(-1);
            backL.setPower(1);
        }
        frontL.setPower(0);
        frontR.setPower(0);
        backR.setPower(0);
        backL.setPower(0);
    }

    public void goRight(double time) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            frontL.setPower(1);
            frontR.setPower(-1);
            backR.setPower(1);
            backL.setPower(-1);
        }
        frontL.setPower(0);
        frontR.setPower(0);
        backR.setPower(0);
        backL.setPower(0);
    }

    public void rotateCWise(double time) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            frontL.setPower(1);
            frontR.setPower(1);
            backR.setPower(1);
            backL.setPower(1);
        }
        frontL.setPower(0);
        frontR.setPower(0);
        backR.setPower(0);
        backL.setPower(0);
    }

    public void rotateCCWise(double time) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            frontL.setPower(-1);
            frontR.setPower(-1);
            backR.setPower(-1);
            backL.setPower(-1);
        }
        frontL.setPower(0);
        frontR.setPower(0);
        backR.setPower(0);
        backL.setPower(0);
    }

    public void rotateCWiseNoNum(double power) {
        frontL.setPower(power);
        frontR.setPower(power);
        backR.setPower(power);
        backL.setPower(power);
    }

    public void rotateCCWiseNoNum(double power) {
        frontL.setPower(-power);
        frontR.setPower(-power);
        backR.setPower(-power);
        backL.setPower(-1 * power);
    }

    public void rotateSTOP(double time) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            frontL.setPower(0);
            frontR.setPower(0);
            backR.setPower(0);
            backL.setPower(0);
        }
    }

    public boolean isAligned() {
        return detector.getAligned();
    }

    public double rotateLeft(double time, double power) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time && isAligned() == false) {
            frontL.setPower(power);
            frontR.setPower(power);
            backR.setPower(power);
            backL.setPower(power);

        }

        frontL.setPower(0);
        frontR.setPower(0);
        backR.setPower(0);
        backL.setPower(0);
        if (ms.time() <= time) {
            return ms.time();
        }
        return time;//LEFT IS POSITIVE TIME, RIGHT IS NEGATIVE TIME
    }


    public double rotateRight(double time, double power) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        boolean totalTime = false;
        while (ms.time() <= time && isAligned() == false) {
            frontL.setPower(-power);
            frontR.setPower(-power);
            backR.setPower(-power);
            backL.setPower(-power);
            if (time == ms.time()) {
                totalTime = true;
            }
        }
        frontL.setPower(0);
        frontR.setPower(0);
        backR.setPower(0);
        backL.setPower(0);
        if (ms.time() <= time) {
            return -1 * ms.time();
        }
        if (totalTime) {
            return -1 * time;
        }
        return 0;
    }

    public void goBack(double time, double power) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            frontL.setPower(power);
            frontR.setPower(-power);
            backR.setPower(-power);
            backL.setPower(power);
        }
        frontL.setPower(0.0);
        frontR.setPower(0.0);
        backR.setPower(0.0);
        backL.setPower(0.0);
    }

    public void Stop() {
        detector.disable();
    }

    public double forwardConv(double length) //length is in cm
    {
        //forward by a power of: .6
        //velocity is 39.37cm/seconds
        return length / 39.37;

    }

    public double rotateConv(double length) {
        //rotational power: .6
        //velocity: 5.48 cm/sec
        return length / 5.48;
    }

    public double angleConv(double angle) //at .35 power : 6 sec = 1 rev, 6sec = 360 degree
    {
        return (angle / 360.0) * 9.5;
    }

    public double tileConv(double tiles)//1 tile is 62 cm this is at .6
    {
        return ((tiles) / 62) * 1.125;
    }

    public void off() {
        frontL.setPower(0.0);
        frontR.setPower(0.0);
        backR.setPower(0.0);
        backL.setPower(0.0);
    }

    public void latchUp(double time, double power) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            latch.setPower(-1 * power);
        }
        frontL.setPower(0.0);
        frontR.setPower(0.0);
        backR.setPower(0.0);
        backL.setPower(0.0);
    }

    public void latchDown(double time, double power) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            latch.setPower(power);
        }
        frontL.setPower(0.0);
        frontR.setPower(0.0);
        backR.setPower(0.0);
        backL.setPower(0.0);
    }
}
