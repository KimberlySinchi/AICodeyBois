/*
Copyright 2018 FIRST Tech Challenge Team 12178

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR O`THER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Helpers.Slave;
import org.firstinspires.ftc.teamcode.Helpers.SlaveAuto;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * Remove a @Disabled the on the next line or two (if present) to add this opmode to the Driver Station OpMode list,
 * or add a @Disabled annotation to prevent this OpMode from being added to the Driver Station
 */
@TeleOp(name = "Driver Mode Encoder", group = "TeleOp")

public class DriverModeEncoder extends OpMode {

    /* Declare OpMode members. */
    private SlaveAuto slave = new SlaveAuto();
    @Override
    public void init()
    {
        slave.init(hardwareMap);
        telemetry.addLine(slave.getStatus());
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop()
    {
        telemetry.addData("Status ", "WORKING");
        telemetry.update();
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start()
    {

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop()
    {
        double rightTrigger = gamepad1.right_trigger;
        double leftTrigger = -gamepad1.left_trigger;

        boolean lBumper = gamepad1.left_bumper;
        boolean rBumper = gamepad1.right_bumper;

        boolean dPadUp = gamepad1.dpad_up;
        boolean dPadDown = gamepad1.dpad_down;
        boolean dPadUp2 = gamepad2.dpad_up;
        boolean dPadDown2 = gamepad2.dpad_down;

        double xL = gamepad1.left_stick_x;
        double yL = gamepad1.left_stick_y;
        double xR = gamepad1.right_stick_x;
        double yR = gamepad1.right_stick_y;

        double x2 = gamepad2.left_stick_x;
        double y2 = gamepad2.left_stick_y;


        boolean back = gamepad1.back;

        telemetry.addData("Status:","x = " + xL + ", y =  " + yL);

        double theta = Math.atan(yL/xL);
        String compare = "-0.0";
        String sTheta = "" + theta;
        boolean isnegZ = true;
        for(int i = 0; i<4; i++)
        {
            if((compare.charAt(i) + "").equals(sTheta.charAt(i) + "") == false)
            {
                isnegZ = false;
                i = 10;
            }
        }
        if(isnegZ == true && xL < 0)
        {
            theta = Math.PI;
        }
        //Q2
        else if(yL > 0 && xL < 0)
        {
            theta = Math.PI + theta;
            telemetry.addData("Q2", "");
        }
        //Q3
        else if((xL >= -1 && xL < 0) && (yL < 0 && yL >= -1))
        {
            theta += Math.PI;
            telemetry.addData("Q3","");
        }
        //Q4
        else if((xL > 0 && xL <= 1) && (yL < 0 && yL >= -1))
        {
            theta = 2*Math.PI + theta;
            telemetry.addData("Q4", "");
        }
        //Q1
        else //theta = theta
            telemetry.addData("Q1", "");
        double degree = (theta/Math.PI) * 180;
        telemetry.addData("Status:","degree = " + degree);

        double rad = Math.sqrt(xL*xL + yL*yL);
        if(rad > 1)
            rad = 1;
        double v1 = rad*(-1*Math.sin(theta + Math.PI/4));
        double v2 = rad*Math.cos(theta + Math.PI/4);
        telemetry.addData("v1 = " + v1, " v2 = " + v2);

        //Rotate right
        if(rightTrigger != 0)
        {
            slave.frontL.setPower(-rightTrigger);
            slave.frontR.setPower(-rightTrigger);
            slave.backR.setPower(-rightTrigger);
            slave.backL.setPower(-rightTrigger);
        }
        //Rotate left
        else if(leftTrigger != 0)
        {
            slave.frontL.setPower(-leftTrigger);
            slave.frontR.setPower(-leftTrigger);
            slave.backR.setPower(-leftTrigger);
            slave.backL.setPower(-leftTrigger);
        }
        else if(xL==0 && yL==0)
        {
            slave.frontL.setPower(0);
            slave.frontR.setPower(0);
            slave.backR.setPower(0);
            slave.backL.setPower(0);
        }
        else
        {
            slave.frontL.setPower(-v2);
            slave.frontR.setPower(v1);
            slave.backR.setPower(v2);
            slave.backL.setPower(-v1);
        }

        //OUR LATCH, 10% OF THE TIME IT WORKS EVERY TIME
        if(dPadUp)
            slave.latch.setPower(0.8);
        else if(dPadDown)
            slave.latch.setPower(-0.8);
        else
            slave.latch.setPower(0);

        //Our Arm Movement
        if(xR > 0)
            slave.armExtend.setPower(xR);
        else if (xR < 0)
            slave.armExtend.setPower(xR);
        else
            slave.armExtend.setPower(0);

        //THE SERVO INTAKE SYSTEM, ==SUCC MACHINE 5000==
        if(rBumper)
            slave.armIntake.setPosition(1);
        else if(lBumper)
            slave.armIntake.setPosition(0);
        else
            slave.armIntake.setPosition(0.5);

        //~~WOW~~ !ENCODER STUFF! ~~WOW~~
        if(back)
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
        int fL = slave.frontL.getCurrentPosition();
        int fR = slave.frontR.getCurrentPosition();
        int bL = slave.backL.getCurrentPosition();
        int bR = slave.backR.getCurrentPosition();

        telemetry.addData("FL" , fL);
        telemetry.addData("FR" , fR);
        telemetry.addData("BL" , bL);
        telemetry.addData("BR" , bR);

        telemetry.update();

    }
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop()
    {

    }
}
