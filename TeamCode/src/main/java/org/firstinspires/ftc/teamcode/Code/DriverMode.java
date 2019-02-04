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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Helpers.Slave;

@TeleOp(name = "Drive", group = "TeleOp")

public class DriverMode extends OpMode
{

    /* Declare OpMode members. */
    private Slave slave = new Slave();

    @Override
    public void init()
    {
        telemetry.addLine("RUNNING");
        telemetry.update();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop()
    {
        telemetry.addLine(slave.getStatus());
        //if(slave.getStatus().equals(""))
          //  telemetry.addData("Status", "WORKING");
        telemetry.addData("Front L", slave.frontL);
        telemetry.addData("Front R", slave.frontR);
        telemetry.addData("Back L", slave.backL);
        telemetry.addData("Back R", slave.backR);
        telemetry.addData("Arm", slave.armUaD);
        telemetry.addData("SERVO intake", slave.armIntake);
        telemetry.addData("SERVO extend", slave.armFaB);
        telemetry.addLine("I UPLOADED CODE");
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
        double rightPress = gamepad1.right_trigger;
        double leftPress = -gamepad1.left_trigger;
        double x2 = gamepad2.left_stick_x;
        double y2 = gamepad2.left_stick_y;
        boolean dPadUp = gamepad1.dpad_up;
        boolean dPadDown = gamepad1.dpad_down;

        double y = gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rTrig = gamepad1.right_trigger;
        double lTrig = gamepad1.left_trigger;
        boolean rBump = gamepad1.right_bumper;
        boolean lBump = gamepad1.left_bumper;
        double yRight = gamepad1.right_stick_y;
        double xLeft = gamepad1.right_stick_x;

        boolean yButton = gamepad1.y;
        boolean aButton = gamepad1.a;

        //For movement and rotating
        boolean slow = gamepad1.back;

        //For arm extension and vertical movement as well as latch
        telemetry.addData("Status:","x = " + x + " ,y =  " +y  );
        telemetry.update();
        double theta = Math.atan(y/x);
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
        if(isnegZ == true && x<0)
            theta = Math.PI;
        //Q2
        else if(y>0 && x<0)
        {
            theta = Math.PI + theta;
            telemetry.addData("Q2", "");

        }
        //Q3
        else if((x>=-1 && x<0) && (y<0 && y>=-1))
        {
            theta += Math.PI;
            telemetry.addData("Q3","");
        }
        //Q4
        else if((x>0 && x<=1) && (y<0 && y>=-1))
        {
            theta = 2*Math.PI + theta;
            telemetry.addData("Q4", "");
        }
        //Q1
        else
        {
            theta = theta;
            telemetry.addData("Q1", "");
        }
        double degree = 0;
        degree = (theta/Math.PI) * 180;
        telemetry.addData("Status:","degree = " + degree);

        double rad = Math.sqrt(x*x + y*y);
        if(rad > 1)
            rad = 1;
        double v1 = rad*(-1*Math.sin(theta + Math.PI/4));
        double v2 = rad*Math.cos(theta + Math.PI/4);
        telemetry.addData("v1 = " + v1, " v2 = " + v2);

        //Rotate right
        if(rightPress != 0)
        {
            slave.frontL.setPower(-rightPress);
            slave.frontR.setPower(-rightPress);
            slave.backR.setPower(-rightPress);
            slave.backL.setPower(-rightPress);
        }
        //Rotate left
        else if(leftPress != 0)
        {
            slave.frontL.setPower(-leftPress);
            slave.frontR.setPower(-leftPress);
            slave.backR.setPower(-leftPress);
            slave.backL.setPower(-leftPress);
        }
        else if(x==0 && y==0)
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

        //ARM MOVEMENT
        if(rTrig > 0)
            slave.armUaD.setPower(1);
        else if(lTrig > 0)
            slave.armUaD.setPower(-1);
        else
            slave.armUaD.setPower(0);

        //SERVOS
        if(dPadUp)
            slave.armIntake.setPosition(1);
        else if(dPadDown)
            slave.armIntake.setPosition(0);
        else
            slave.armIntake.setPosition(0.5);
        if(yButton)
            slave.armFaB.setPosition(1);
        else if(aButton)
            slave.armFaB.setPosition(0);
        else
            slave.armFaB.setPosition(0.5);


        //LATCH
        if(rBump)
        {
            slave.latchUp.setPower(1.0);
            slave.latchDown.setPower(1.0);
        }
        else if(lBump)
        {
            slave.latchUp.setPower(-1.0);
            slave.latchDown.setPower(-1.0);
        }
        else
        {
            slave.latchUp.setPower(0);
            slave.latchDown.setPower(0);
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}
