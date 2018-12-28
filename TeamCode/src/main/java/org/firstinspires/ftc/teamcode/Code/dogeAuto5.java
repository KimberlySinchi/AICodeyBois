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
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="AWESOMEAUTOREVERSE(plsWork)2.0AAAAA", group="Linear Opmode")

 public class dogeAuto5 extends LinearOpMode {

    // Declare OpMode members.
    // Detector object
    private SlaveAuto slave= new SlaveAuto();
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
        slave.init(hardwareMap); //Mapping all devices
        /*
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
        */

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


        }
    }
        public void up(double power)
        {
            frontL.setPower(-power);
            frontR.setPower(power);
            backR.setPower(power);
            backL.setPower(-power);
        }
        public void down(double power)
        {
            frontL.setPower(power);
            frontR.setPower(-power);
            backR.setPower(-power);
            backL.setPower(power);
        }
        public void right(double power)
        {
            frontL.setPower(-power);
            frontR.setPower(-power);
            backR.setPower(-power);
            backL.setPower(-power);
        }
        public void left(double power)
        {
            frontL.setPower(power);
            frontR.setPower(power);
            backR.setPower(power);
            backL.setPower(power);
        }
        public int convInchToTicks(double inches)
        {
            //Note: one revolution is 1440 ticks and 1 rev is 11.873 inch ==> so easy conversion from inches to ticks
            return (int)(inches/11.873) * 1140;
        }
        public void goUp(int power, int ticks) //NOTE: THIS DISTANCE IS IN INCHES
        {
            frontL.setMode(DcMotor.RunMode.RESET_ENCODERS);
            frontR.setMode(DcMotor.RunMode.RESET_ENCODERS);
            backL.setMode(DcMotor.RunMode.RESET_ENCODERS);
            backR.setMode(DcMotor.RunMode.RESET_ENCODERS);

            frontL.setTargetPosition(ticks);
            frontR.setTargetPosition(ticks);
            backL.setTargetPosition(ticks);
            backR.setTargetPosition(ticks);

            frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            /**
             frontL
             frontR
             backL
             backR

             **/
            up(power);
            while(frontL.isBusy() &&  frontR.isBusy() && backL.isBusy() &&  backR.isBusy()) {

                Stop();
                frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            }
            }
            public void goDown(int power, int ticks) //NOTE: THIS DISTANCE IS IN INCHES
            {
                frontL.setMode(DcMotor.RunMode.RESET_ENCODERS);
                frontR.setMode(DcMotor.RunMode.RESET_ENCODERS);
                backL.setMode(DcMotor.RunMode.RESET_ENCODERS);
                backR.setMode(DcMotor.RunMode.RESET_ENCODERS);

                frontL.setTargetPosition(ticks);
                frontR.setTargetPosition(ticks);
                backL.setTargetPosition(ticks);
                backR.setTargetPosition(ticks);

                frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                /**
                 frontL
                 frontR
                 backL
                 backR

                 **/
                down(power);
                while(frontL.isBusy() &&  frontR.isBusy() && backL.isBusy() &&  backR.isBusy())
                {}

                    Stop();
                    frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


                }
                public void goRight(int power, int ticks)
                {
                    frontL.setMode(DcMotor.RunMode.RESET_ENCODERS);
                    frontR.setMode(DcMotor.RunMode.RESET_ENCODERS);
                    backL.setMode(DcMotor.RunMode.RESET_ENCODERS);
                    backR.setMode(DcMotor.RunMode.RESET_ENCODERS);

                    frontL.setTargetPosition(ticks);
                    frontR.setTargetPosition(ticks);
                    backL.setTargetPosition(ticks);
                    backR.setTargetPosition(ticks);

                    frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                    /**
                     frontL
                     frontR
                     backL
                     backR

                     **/
                    right(power);
                    while(frontL.isBusy() &&  frontR.isBusy() && backL.isBusy() &&  backR.isBusy())
                    {}

                        Stop();
                        frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


                    }
                    public void goLeft(double power,int ticks)// edit this part to send the amount of ticks after the yellow block was found ==> an int
                    {
                        frontL.setMode(DcMotor.RunMode.RESET_ENCODERS);
                        frontR.setMode(DcMotor.RunMode.RESET_ENCODERS);
                        backL.setMode(DcMotor.RunMode.RESET_ENCODERS);
                        backR.setMode(DcMotor.RunMode.RESET_ENCODERS);

                        frontL.setTargetPosition(ticks);
                        frontR.setTargetPosition(ticks);
                        backL.setTargetPosition(ticks);
                        backR.setTargetPosition(ticks);

                        frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                        /**
                         frontL
                         frontR
                         backL
                         backR

                         **/
                        left(power);
                        while (frontL.isBusy() && frontR.isBusy() && backL.isBusy() && backR.isBusy())
                        {}

                            Stop();
                            frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                            frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                            backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                            backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                        }

                        public void Stop()
                        {
                            up(0);
                        }
                    }