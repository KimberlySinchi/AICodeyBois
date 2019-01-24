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

import org.firstinspires.ftc.teamcode.Helpers.Slave;
import org.firstinspires.ftc.teamcode.Helpers.SlaveAuto;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the AutoRobot Controller and executed.
 *
 * Remove a @Disabled the on the next line or two (if present) to add this opmode to the Driver Station OpMode list,
 * or add a @Disabled annotation to prevent this OpMode from being added to the Driver Station
 */
@TeleOp(name = "DriveData", group = "TeleOp")

public class DriverModeAuto extends OpMode {

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
        boolean resetEncod = gamepad1.y;
        boolean resetEncod2 = gamepad1.dpad_up;

        double y = gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double yRight = gamepad1.right_stick_y;
        double xLeft = gamepad1.right_stick_x;
        telemetry.addData("Status:","x = " + x + " ,y =  " +y  );
        telemetry.update();

        if(yRight>0)
            slave.latch.setPower(-.9);
        else if(yRight<0)
            slave.latch.setPower(.9);
        else
            slave.latch.setPower(0);
        telemetry.addData("Latch Pos: ", slave.latch.getCurrentPosition());
        telemetry.update();

        if(resetEncod)
            slave.latch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        else
            slave.latch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if(resetEncod2)
            slave.latch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        else
            slave.latch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}
