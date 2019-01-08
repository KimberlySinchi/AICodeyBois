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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Helpers.Slave;

import java.text.SimpleDateFormat;
import java.util.Date;

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
@TeleOp(name = "Drive", group = "TeleOp")

public class DriverMode extends OpMode {

    /* Declare OpMode members. */
    private Slave slave = new Slave();

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
        double bufferA = 5;
        double rightPress = gamepad1.right_trigger;
        double leftPress = gamepad1.left_trigger * -1;
        double x2 = gamepad2.left_stick_x;
        double y2 = gamepad2.left_stick_y;
        boolean dPadUp = gamepad1.dpad_up;
        boolean dPadDown = gamepad1.dpad_down;

        double y = gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double yRight = gamepad1.right_stick_y;
        double xLeft = gamepad1.right_stick_x;
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
        {
            theta = Math.PI;
        }
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
        {
            rad = 1;
        }
        double v1 = rad*(-1*Math.sin(theta + Math.PI/4));
        double v2 = rad*Math.cos(theta + Math.PI/4);
        telemetry.addData("v1 = " + v1, " v2 = " + v2);

        //Rotate right
        if(rightPress != 0)
        {
            /*
            frontL.setPower(-1*rightPress);
            frontR.setPower(-1*rightPress);
            backR.setPower(-1*rightPress);
            backL.setPower(-1*rightPress);
            */
            slave.frontL.setPower(-1*rightPress);
            slave.frontR.setPower(-1*rightPress);
            slave.backR.setPower(-1*rightPress);
            slave.backL.setPower(-1*rightPress);
        }
        //Rotate left
        else if(leftPress != 0)
        {
            /*
            frontL.setPower(-1*leftPress);
            frontR.setPower(-1*leftPress);
            backR.setPower(-1*leftPress);
            backL.setPower(-1*leftPress);
            */
            slave.frontL.setPower(-1*leftPress);
            slave.frontR.setPower(-1*leftPress);
            slave.backR.setPower(-1*leftPress);
            slave.backL.setPower(-1*leftPress);
        }
        else if(x==0 && y==0)
        {
            /*
            frontL.setPower(0);
            frontR.setPower(0);
            backR.setPower(0);
            backL.setPower(0);
            */
            slave.frontL.setPower(0);
            slave.frontR.setPower(0);
            slave.backR.setPower(0);
            slave.backL.setPower(0);
        }
        else
        {
            /*
            frontL.setPower(-v2);
            frontR.setPower(v1);
            backR.setPower(v2);
            backL.setPower(-v1);
            */
            slave.frontL.setPower(-v2);
            slave.frontR.setPower(v1);
            slave.backR.setPower(v2);
            slave.backL.setPower(-v1);
        }
        //Moving the arm up
        /**
                if(yRight < 0)
        {
            armMotor1.setPower(y);
            armMotor2.setPower(-y);
            telemetry.addLine("Arm Up");
        }
        //Moving the arm down
        else if(yRight > 0)
        {
            armMotor1.setPower(-y);
            armMotor2.setPower(y);
            telemetry.addLine("Arm Down");
        }
        //No vertical arm movement
        else
        {
            armMotor1.setPower(0);
            armMotor2.setPower(0);
        }
        telemetry.update();
**/
        //SERVOS
        if(dPadUp)
            slave.armIntake.setPosition(1);
        else if(dPadDown)
            slave.armIntake.setPosition(0);
        else
            slave.armIntake.setPosition(0.5);

        //upDownMotor = name of the motor that controls vertical arm movement
        //up = the y2 might have to be changed
        /**
        if(y2<0)
        {
            upDownMotor.setPower(y2);
        }
        else
        {
            upDownMotor.setPower(0);
        }
        if(y2>0)
        {
            upDownMotor.setPower(-y2);
        }
        else
        {
            upDownMotor.setPower(0);
        }
         **/
        if(yRight>0)
            slave.latch.setPower(-.3);
        else if(yRight<0)
            slave.latch.setPower(-.3);
        else
            slave.latch.setPower(0);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}
