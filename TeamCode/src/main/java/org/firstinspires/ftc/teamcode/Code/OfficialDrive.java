package org.firstinspires.ftc.teamcode.Code;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Helpers.Slave;

/**
 * UPDATE THIS SINCE WE CHANGED THE SLAVE CLASS
 */
@TeleOp(name = "Official Drive")
public class OfficialDrive extends OpMode
{
    private Slave slave = new Slave();

    @Override
    public void init()
    {
        slave.init(hardwareMap);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
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
        double leftPress = -gamepad1.left_trigger;
        boolean dPadUp = gamepad2.dpad_up;
        boolean dPadDown = gamepad2.dpad_down;

        //For movement and rotating
        double y = gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;

        //For arm extension and vertical movement as well as latch
        double x2 = gamepad2.left_stick_x;
        double y2 = gamepad2.left_stick_y;
        double xR2 = gamepad2.right_stick_x;
        double yR2 = gamepad2.right_stick_y;
        double rTrig2 = gamepad2.right_trigger;
        double lTrig2 = gamepad2.left_trigger;

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
        {
            rad = 1;
        }
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
        if (y2 > 0)
            slave.armFaB.setPosition(1);
        else if(y2 < 0)
            slave.armFaB.setPosition(-1);
        else
            slave.armFaB.setPosition(0);
        if (yR2 > 0)
            slave.armUaD.setPower(0.35);
        else if(yR2 <0)
            slave.armUaD.setPower(-.45);
        else
            slave.armUaD.setPower(0);

        //SERVOS
        if(dPadUp)
            slave.armIntake.setPosition(1);
        else if(dPadDown)
            slave.armIntake.setPosition(0);
        else
            slave.armIntake.setPosition(0.5);

        //LATCH
        if(rTrig2 > 0)
        {
            slave.latchUp.setPower(1.0);
            slave.latchDown.setPower(1.0);
        }
        else
        {
            slave.latchUp.setPower(0);
            slave.latchDown.setPower(0);
        }
        if(rTrig2 < 0)
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
