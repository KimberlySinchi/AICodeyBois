package org.firstinspires.ftc.teamcode.OfficialCode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Helpers.Slave;
import org.firstinspires.ftc.teamcode.Helpers.SlaveOfficial;

/**
 * UPDATE THIS SINCE WE CHANGED THE SLAVE CLASS
 */
@TeleOp(name = "Official Drive")

public class OfficialDrive extends OpMode
{
    private SlaveOfficial slave = new SlaveOfficial();

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
        telemetry.addData("Front L", slave.frontL);
        telemetry.addData("Front R", slave.frontR);
        telemetry.addData("Back L", slave.backL);
        telemetry.addData("Back R", slave.backR);
        telemetry.addData("Arm", slave.armUaD);
        telemetry.addData("SERVO intake", slave.armIntake);
        telemetry.addData("SERVO extend", slave.armFaB);
        telemetry.addLine("I UPLOADED CODE");
        telemetry.update();
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
        double SPEED = 0.3;
        /**
         * REX'S CONTROLS
         */
        //For movement INITIALLY
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;

        //For latch up and down movement
        boolean aButton = gamepad1.a;
        boolean bButton = gamepad1.b;

        //For switching modes
        boolean xButton = gamepad1.x;

        //Rotating the robot
        double leftPress = -gamepad1.left_trigger;
        double rightPress = gamepad1.right_trigger;

        //For movement AFTER THE SWITCH
        boolean dPadUp = gamepad1.dpad_up;
        boolean dPadRight = gamepad1.dpad_right;
        boolean dPadDown = gamepad1.dpad_down;
        boolean dPadLeft = gamepad1.dpad_left;

        /**
         * ELIZABETH'S CONTROLS
         */
        //For arm intake
        boolean rBump2 = gamepad2.right_bumper;
        boolean lBump2 = gamepad2.left_bumper;

        //Extension and retraction of the arm
        boolean dPadR2 = gamepad2.dpad_right;
        boolean dPadL2 = gamepad2.dpad_left;

        //Physically moving the arm up and down (away from base and towards base)
        boolean dPadU2 = gamepad2.dpad_up;
        boolean dPadD2 = gamepad2.dpad_down;

        //Mineral servos
        boolean yButton = gamepad2.y;

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

        /**
         * REX'S CODE
         */
        if(gamepad1.x) //SWITCH MODES -----------
        {
            //ROTATE RIGHT ----------
            if(rightPress != 0)
            {
                slave.frontL.setPower(-rightPress);
                slave.frontR.setPower(-rightPress);
                slave.backR.setPower(-rightPress);
                slave.backL.setPower(-rightPress);

            }
            //ROTATE LEFT --------------
            else if(leftPress != 0)
            {
                slave.frontL.setPower(-leftPress);
                slave.frontR.setPower(-leftPress);
                slave.backR.setPower(-leftPress);
                slave.backL.setPower(-leftPress);
            }
            //FORWARDS --------------
            else if(dPadUp)
            {
                slave.frontL.setPower(SPEED);
                slave.frontR.setPower(SPEED);
                slave.backL.setPower(-SPEED);
                slave.backR.setPower(-SPEED);
                /*FORWARD
                slave.frontL.setPower(-SPEED);
                slave.frontR.setPower(SPEED);
                slave.backR.setPower(SPEED);
                slave.backL.setPower(-SPEED);*/
            }
            //BACKWARDS -----------------
            else if(dPadDown)
            {
                slave.frontL.setPower(-SPEED);
                slave.frontR.setPower(-SPEED);
                slave.backL.setPower(SPEED);
                slave.backR.setPower(SPEED);
                /*BACKWARDS
                slave.frontL.setPower(SPEED);
                slave.frontR.setPower(-SPEED);
                slave.backR.setPower(-SPEED);
                slave.backL.setPower(SPEED);*/
            }
            //LEFT ---------------
            else if(dPadLeft)
            {
                slave.frontL.setPower(SPEED);
                slave.frontR.setPower(-SPEED);
                slave.backR.setPower(-SPEED);
                slave.backL.setPower(SPEED);
                /*LEFT
                slave.frontL.setPower(SPEED);
                slave.frontR.setPower(SPEED);
                slave.backL.setPower(-SPEED);
                slave.backR.setPower(-SPEED);*/
            }
            //RIGHT ----------------
            else if(dPadRight)
            {
                slave.frontL.setPower(-SPEED);
                slave.frontR.setPower(SPEED);
                slave.backR.setPower(SPEED);
                slave.backL.setPower(-SPEED);
                /*RIGHT
                slave.frontL.setPower(-SPEED);
                slave.frontR.setPower(-SPEED);
                slave.backL.setPower(SPEED);
                slave.backR.setPower(SPEED);*/
            }
            else
            {
                slave.frontL.setPower(0);
                slave.frontR.setPower(0);
                slave.backR.setPower(0);
                slave.backL.setPower(0);
            }
        }
        else //SWITCH MODES ----------
        {
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
        }
        if(aButton) //LATCH ------------
        {
            slave.latchUp.setPower(1);
            slave.latchDown.setPower(1);
        }
        else if(bButton)
        {
            slave.latchUp.setPower(-1);
            slave.latchDown.setPower(-1);
        }
        else
        {
            slave.latchUp.setPower(0);
            slave.latchDown.setPower(0);
        }

        /**
         * ELIZABETH'S CODE
         */
        //ARM INTAKE -------------------
        if(rBump2)
            slave.armIntake.setPosition(0);
        else if(lBump2)
            slave.armIntake.setPosition(1);
        else
            slave.armIntake.setPosition(0.5);

        //ARM EXTENSION -----------------
        if(dPadL2)
            slave.armFaB.setPower(-1);
        else if(dPadR2)
            slave.armFaB.setPower(1);
        else
            slave.armFaB.setPower(0);

        //ARM UP AND DOWN -------------------
        if(dPadU2)
            slave.armUaD.setPower(0.6);
        else if(dPadD2)
            slave.armUaD.setPower(-0.6);
        else
            slave.armUaD.setPower(0);

        //MINERAL DEPOSIT ----------------
        if(yButton)
            slave.mineralServo.setPosition(0);
        else
            slave.mineralServo.setPosition(0.5);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop()
    { }
}
