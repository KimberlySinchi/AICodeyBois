package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Helpers.Slave;
import org.firstinspires.ftc.teamcode.Helpers.SlaveAuto;

@TeleOp(name = "Drive Encoders", group = "TeleOp")

public class DriverModeEncoders extends OpMode
{
    private SlaveAuto slave = new SlaveAuto();

    @Override
    public void init()
    {
        slave.init(hardwareMap);
    }

    @Override
    public void init_loop()
    {
        telemetry.addLine(slave.getStatus());
        if(slave.getStatus().equals(""))
            telemetry.addData("Status", "WORKING");
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
        double leftPress = gamepad1.left_trigger * -1;
        double x2 = gamepad2.left_stick_x;
        double y2 = gamepad2.left_stick_y;
        boolean dPadUp = gamepad1.dpad_up;
        boolean dPadDown = gamepad1.dpad_down;
        boolean rBump = gamepad1.right_bumper;
        boolean lBump = gamepad1.left_bumper;
        boolean bButton = gamepad1.b;
        boolean xButton = gamepad1.x;

        double y = gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double yRight = gamepad1.right_stick_y;
        double xRight = gamepad1.right_stick_x;

        //For arm extension and vertical movement as well as latch
        double yR2 = gamepad2.right_stick_y;
        telemetry.addData("Status:","x = " + x + " ,y =  " +y  );
        encoderValues();
        encoderAvg();
        telemetry.update();


        //ROTATION ---------------
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
        //FORWARDS --------------
        else if(y < 0)
        {
            slave.frontL.setPower(y);
            slave.frontR.setPower(-y);
            slave.backR.setPower(-y);
            slave.backL.setPower(y);
        }
        //BACKWARDS -----------------
        else if(y > 0)
        {
            slave.frontL.setPower(y);
            slave.frontR.setPower(-y);
            slave.backR.setPower(-y);
            slave.backL.setPower(y);
        }
        //LEFT ---------------
        else if(x < 0)
        {
            slave.frontL.setPower(-x);
            slave.frontR.setPower(-x);
            slave.backL.setPower(x);
            slave.backR.setPower(x);
        }
        //RIGHT ----------------
        else if(x > 0.5)
        {
            slave.frontL.setPower(-x);
            slave.frontR.setPower(-x);
            slave.backL.setPower(x);
            slave.backR.setPower(x);
        }
        else
        {
            slave.frontL.setPower(0);
            slave.frontR.setPower(0);
            slave.backR.setPower(0);
            slave.backL.setPower(0);
        }
        //QUADRANT ONE NW
        if(yRight < 0 && xRight < 0)
        {
            slave.backL.setPower(yRight);
            slave.frontR.setPower(-yRight);
        }
        //QUADRANT TWO NE
        else if(yRight < 0 && xRight > 0)
        {
            slave.frontL.setPower(-xRight);
            slave.backR.setPower(xRight);
        }
        //QUADRANT THREE SE
        else if(yRight > 0 && xRight > 0)
        {
            slave.backL.setPower(xRight);
            slave.frontR.setPower(-xRight);
        }
        //QUADRANT FOUR SW
        else if(yRight > 0 && xRight < 0)
        {
            slave.frontL.setPower(yRight);
            slave.backR.setPower(-yRight);
        }

        /*SERVOS
        if(dPadUp)
            slave.armIntake.setPosition(1);
        else if(dPadDown)
            slave.armIntake.setPosition(0);
        else
            slave.armIntake.setPosition(0.5);*/

        if(rBump)
        {
            slave.latchUp.setPower(1);
            slave.latchDown.setPower(1);
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

        if(bButton)
            slave.armUaD.setPower(0.6);
        else if(xButton)
            slave.armUaD.setPower(-0.6);
        else
            slave.armUaD.setPower(0);
        /*
        if (y2 > 0)
            slave.armFaB.setPower(-.3);
        else if(y2 < 0)
            slave.armFaB.setPower(.3);
        else
            slave.armFaB.setPower(0);
        if (yR2 > 0)
            slave.armUaD.setPower(0.35);
        else if(yR2 <0)
            slave.armUaD.setPower(-.45);
        else
            slave.armUaD.setPower(0);*/
        if(dPadUp)
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
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
    public void encoderValues()
    {
        telemetry.addLine("BackL: " + slave.frontL.getCurrentPosition());
        telemetry.addLine("FrontL: " + slave.frontR.getCurrentPosition());
        telemetry.addLine("FrontR: " + slave.backL.getCurrentPosition());
        telemetry.addLine("BackR: " + slave.backR.getCurrentPosition());
    }
    public void encoderAvg()
    {
        int sum = Math.abs(slave.frontL.getCurrentPosition()) + Math.abs(slave.frontR.getCurrentPosition()) + Math.abs(slave.backL.getCurrentPosition()) + Math.abs(slave.backR.getCurrentPosition());
        int average = sum/4;
        telemetry.addData("Encoder Average", average);
    }
}
