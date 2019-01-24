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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@Autonomous(name = "Crater AutoAttemptEncoders", group = "Linear Opmode")
@Disabled
public class dogeAuto5 extends LinearOpMode {

    // Declare OpMode members.
    // Detector object

    public SlaveAuto slave = new SlaveAuto();
    public boolean bool1 = true;
    public boolean bool2 = true;
    public boolean bool3 = true;
    public GoldAlignDetector detector;
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor frontL;
    public DcMotor frontR;
    public DcMotor backL;
    public DcMotor backR;
    public DcMotor latch;
    public Servo armSpin;
    static final double SPEED = 1;

    public boolean isBlock() {
        return detector.getAligned();
    }

    @Override
    public void runOpMode()
    {

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
         //Mapping all devices

        slave.init(hardwareMap);

        telemetry.addLine(slave.getStatus());
        telemetry.update();

        /**
        try
        {
            frontL = hardwareMap.get(DcMotor.class, "DC1");
            frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }catch (Exception e)
        {
        }
        try
        {
            frontR = hardwareMap.get(DcMotor.class, "DC2");
            frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } catch (Exception e)
        {
        }
        try {
            backR = hardwareMap.get(DcMotor.class, "DC3");
            backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } catch (Exception e) {
        }
        try
        {
            backL = hardwareMap.get(DcMotor.class, "DC4");
            backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } catch (Exception e) {
        }
**/

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {
            if (bool1)
            {
                /**
                goUp(-1, 1440);
                bool1 = false;
                **/

                //Autonomous starts here

                int blockFound = goLeftDetect(.15,convAngleToTick(110));
                int center = blockFound - convAngleToTick(61);
                stopMotors(1);
                goRight(.15,1440);
                stopMotors(1);

            }
        }
    }

    public void up(double power) {
        slave.frontL.setPower(power);
        slave.frontR.setPower(power);
        slave.backR.setPower(power);
        slave.backL.setPower(power);
    }

    public void down(double power) {

        slave.frontL.setPower(power);
        slave.frontR.setPower(power);
        slave.backR.setPower(power);
        slave.backL.setPower(power);
    }

    public void right(double power) {
        slave.frontL.setPower(power);
        slave.frontR.setPower(power);
        slave.backR.setPower(power);
        slave.backL.setPower(power);
    }

    public void left(double power) {
        slave.frontL.setPower(power);
        slave.frontR.setPower(power);
        slave.backR.setPower(power);
        slave.backL.setPower(power);
    }

    public int convInchToTicks(double inches) {
        //Note: one revolution is 1440 ticks and 1 rev is 11.873 inch ==> so easy conversion from inches to ticks
        return (int) (inches / 11.873) * 1140;
    }

    public void goUp(double power, int ticks) //NOTE: THIS DISTANCE IS IN INCHES
    {
        slave.frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slave.frontL.setTargetPosition(-ticks);
        slave.frontR.setTargetPosition(ticks);
        slave.backL.setTargetPosition(-ticks);
        slave.backR.setTargetPosition(ticks);

        slave.frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        /**
         frontL
         frontR
         backL
         backR

         **/
        up(power);
        while (slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy() && opModeIsActive()) 
        {}

        Stop();
        slave.frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontL.setDirection(DcMotor.Direction.FORWARD);
        frontR.setDirection(DcMotor.Direction.FORWARD);
        backR.setDirection(DcMotor.Direction.FORWARD);
        backL.setDirection(DcMotor.Direction.FORWARD);

    }

    public void goDown(int power, int ticks) //NOTE: THIS DISTANCE IS IN INCHES
    {
        slave.frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slave.frontL.setTargetPosition(ticks);
        slave.frontR.setTargetPosition(-ticks);
        slave.backL.setTargetPosition(ticks);
        slave.backR.setTargetPosition(-ticks);

        slave.frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        /**
         frontL
         frontR
         backL
         backR

         **/
        down(power);
        while (slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy() && opModeIsActive()) {
        }

        Stop();
        slave.frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        frontL.setDirection(DcMotor.Direction.FORWARD);
        frontR.setDirection(DcMotor.Direction.FORWARD);
        backR.setDirection(DcMotor.Direction.FORWARD);
        backL.setDirection(DcMotor.Direction.FORWARD);


    }

    public void goRight(double power, int ticks) {
        slave.frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slave.frontL.setTargetPosition(-ticks);
        slave.frontR.setTargetPosition(-ticks);
        slave.backL.setTargetPosition(-ticks);
        slave.backR.setTargetPosition(-ticks);

        slave.frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        /**
         frontL
         frontR
         backL
         backR

         **/
        right(power);
        while (slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy() && opModeIsActive()) {
        }

        Stop();
        slave.frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontL.setDirection(DcMotor.Direction.FORWARD);
        frontR.setDirection(DcMotor.Direction.FORWARD);
        backR.setDirection(DcMotor.Direction.FORWARD);
        backL.setDirection(DcMotor.Direction.FORWARD);

    }

    public void goLeft(double power, int ticks)// edit this part to send the amount of ticks after the yellow block was found ==> an int
    {
        slave.frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slave.frontL.setTargetPosition(ticks);
        slave.frontR.setTargetPosition(ticks);
        slave.backL.setTargetPosition(ticks);
        slave.backR.setTargetPosition(ticks);

        slave.frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        /**
         frontL
         frontR
         backL
         backR

         **/
        left(power);
        while (slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy() && opModeIsActive())
        { }

        Stop();
        slave.frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        frontL.setDirection(DcMotor.Direction.FORWARD);
        frontR.setDirection(DcMotor.Direction.FORWARD);
        backR.setDirection(DcMotor.Direction.FORWARD);
        backL.setDirection(DcMotor.Direction.FORWARD);

    }
    public int goLeftDetect(double power, int ticks)
    {
        slave.frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slave.frontL.setTargetPosition(ticks);
        slave.frontR.setTargetPosition(ticks);
        slave.backL.setTargetPosition(ticks);
        slave.backR.setTargetPosition(ticks);

        slave.frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        left(power);
        while (isAligned() == false&&(slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy()) && opModeIsActive())
        { }

        Stop();
        slave.frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontL.setDirection(DcMotor.Direction.FORWARD);
        frontR.setDirection(DcMotor.Direction.FORWARD);
        backR.setDirection(DcMotor.Direction.FORWARD);
        backL.setDirection(DcMotor.Direction.FORWARD);
        return slave.frontL.getCurrentPosition();
    }
    public int goRightDetect(double power, int ticks) {
        slave.frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slave.frontL.setTargetPosition(ticks);
        slave.frontR.setTargetPosition(ticks);
        slave.backL.setTargetPosition(ticks);
        slave.backR.setTargetPosition(ticks);

        slave.frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        /**
         frontL
         frontR
         backL
         backR

         **/
        right(power);
        while (isAligned() == false&&slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy() && opModeIsActive())
        { }

        Stop();
        slave.frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontL.setDirection(DcMotor.Direction.FORWARD);
        frontR.setDirection(DcMotor.Direction.FORWARD);
        backR.setDirection(DcMotor.Direction.FORWARD);
        backL.setDirection(DcMotor.Direction.FORWARD);
        return slave.frontL.getCurrentPosition();
    }
    public int goDownDetect(int power, int ticks) //NOTE: THIS DISTANCE IS IN INCHES
    {
        slave.frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slave.frontL.setTargetPosition(ticks);
        slave.frontR.setTargetPosition(ticks);
        slave.backL.setTargetPosition(ticks);
        slave.backR.setTargetPosition(ticks);

        slave.frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        /**
         frontL
         frontR
         backL
         backR

         **/
        down(power);
        while (isAligned() == false && slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy()&&opModeIsActive())
        { }

        Stop();
        slave.frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontL.setDirection(DcMotor.Direction.FORWARD);
        frontR.setDirection(DcMotor.Direction.FORWARD);
        backR.setDirection(DcMotor.Direction.FORWARD);
        backL.setDirection(DcMotor.Direction.FORWARD);
        return slave.frontL.getCurrentPosition();
    }
    public int goUpDetect(double power, int ticks) //NOTE: THIS DISTANCE IS IN INCHES
    {
        slave.frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slave.frontL.setTargetPosition(ticks);
        slave.frontR.setTargetPosition(ticks);
        slave.backL.setTargetPosition(ticks);
        slave.backR.setTargetPosition(ticks);

        slave.frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        /**
         frontL
         frontR
         backL
         backR

         **/
        up(power);
        while (isAligned() == false && slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy() && opModeIsActive())
        {}

        Stop();
        slave.frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontL.setDirection(DcMotor.Direction.FORWARD);
        frontR.setDirection(DcMotor.Direction.FORWARD);
        backR.setDirection(DcMotor.Direction.FORWARD);
        backL.setDirection(DcMotor.Direction.FORWARD);
        return slave.frontL.getCurrentPosition();
    }
    public void stopMotors(double time)
    {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            frontL.setPower(0);
            frontR.setPower(0);
            backR.setPower(0);
            backL.setPower(0);
        }
    }
    public void Stop() {
        up(0);
    }
    public boolean isAligned() {
        return detector.getAligned();
    }
    public int convInToTick(double inches) //only for up down, side-side movement
    {
        int COUNTS_PER_MOTOR_REV = 1680;
        double DRIVE_GEAR_REDUCTION = 2.0; //This value has yet to be discovered //doesnt matter jason
        double WHEEL_DIAMETER_INCHES = 3.78;
        double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
        double ROBOT_DIAMETER_INCHES = 17.5;
        double COUNTS_PER_NINETY_DEG = (3.1415 * ROBOT_DIAMETER_INCHES / 4) * COUNTS_PER_INCH;
        double HOLONOMIC_COMPENSATION_FACTOR = (Math.sin(45) * WHEEL_DIAMETER_INCHES * 3.1415);//1 rev = this many inches
        return (int)(inches/HOLONOMIC_COMPENSATION_FACTOR)*1680;

    }
    public int convAngleToTick(double angle) //robot from wheel to wheel is 49cm
    {
        return (int)((angle/22.45) * 1680);
    }
}
