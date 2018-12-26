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

@Autonomous(name="AWESOMEAUTOREVERSE(plsWork)", group="Linear Opmode")

public class dogeAuto4 extends LinearOpMode {

    // Declare OpMode members.
    // Detector object
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

        }




    public void goUp(double time) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            frontL.setPower(-1.0);
            frontR.setPower(1.0);
            backR.setPower(1.0);
            backL.setPower(-1.0);
        }
        frontL.setPower(0.0);
        frontR.setPower(0.0);
        backR.setPower(0.0);
        backL.setPower(0.0);
    }

    public void goDown(double time) {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time) {
            frontL.setPower(1.0);
            frontR.setPower(-1.0);
            backR.setPower(-1.0);
            backL.setPower(1.0);
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

    public void rotateSTOP(double time)
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
        if(isAligned() == true)
        {
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
            if(time == ms.time())
            {
                totalTime = true;
            }
        }
        frontL.setPower(0);
        frontR.setPower(0);
        backR.setPower(0);
        backL.setPower(0);
        if(isAligned() == true)
        {
            return -1*ms.time();
        }
        if(totalTime)
        {
            return -1*time;
        }
        return 0;
    }
    public void goBack(double time)
    {
        ElapsedTime ms = new ElapsedTime();
        ms.reset();
        while (ms.time() <= time)
        {
            frontL.setPower(1.0);
            frontR.setPower(-1.0);
            backR.setPower(-1.0);
            backL.setPower(1.0);
        }
        frontL.setPower(0.0);
        frontR.setPower(0.0);
        backR.setPower(0.0);
        backL.setPower(0.0);
    }

    public void Stop()
    {
        detector.disable();
    }
}
