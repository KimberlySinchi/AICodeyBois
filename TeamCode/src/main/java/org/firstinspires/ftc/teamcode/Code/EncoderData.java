package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.teamcode.Helpers.SlaveAuto;

@Autonomous(name = "EncoderData", group = "LinearOpMode")
@Disabled
public class EncoderData extends LinearOpMode {
    private SlaveAuto slave = new SlaveAuto();
    private ElapsedTime runtime = new ElapsedTime();

    private static final int COUNTS_PER_MOTOR_REV = 1440;
    private static final double DRIVE_GEAR_REDUCTION = 2.0; //This value has yet to be discovered

    private static final double WHEEL_DIAMETER_INCHES = 3.78;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    private static final double ROBOT_DIAMETER_INCHES = 17.5;
    static final double COUNTS_PER_NINETY = (3.1415*ROBOT_DIAMETER_INCHES/4)*COUNTS_PER_INCH;

    private static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    @Override
    public void runOpMode() {
        slave.init(hardwareMap);
        telemetry.addLine("Mapping Successful");

        upOneRev();
    }
    private void upOneRev(){
        //Sets encoder values to 0
        slave.frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Sets front right and back right to reverse so encoders track properly
        slave.frontR.setDirection(DcMotor.Direction.REVERSE);
        slave.backR.setDirection(DcMotor.Direction.REVERSE);

        //Sets the target encoder value to reach
        slave.frontL.setTargetPosition(COUNTS_PER_MOTOR_REV);
        slave.frontR.setTargetPosition(COUNTS_PER_MOTOR_REV);
        slave.backL.setTargetPosition(COUNTS_PER_MOTOR_REV);
        slave.backR.setTargetPosition(COUNTS_PER_MOTOR_REV);

        //Prepares motors to run towards position
        slave.frontL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.frontR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slave.backR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Makes the motors move
        slave.frontL.setPower(DRIVE_SPEED);
        slave.frontR.setPower(DRIVE_SPEED);
        slave.backL.setPower(DRIVE_SPEED);
        slave.backR.setPower(DRIVE_SPEED);

        while(slave.frontL.isBusy() && slave.frontR.isBusy() && slave.backL.isBusy() && slave.backR.isBusy()){
            //does nothing, just makes the method stuck in a while loop until it's done
            int fL = slave.frontL.getCurrentPosition(),fR = slave.frontR.getCurrentPosition();
            int bL = slave.backL.getCurrentPosition(),bR = slave.backR.getCurrentPosition();
            telemetry.addData("FL",fL);
            telemetry.addData("FR",fR);
            telemetry.addData("BL",bL);
            telemetry.addData("BR",bR);

            telemetry.update();
        }

        //Sets motor mode back to encoder
        //This also makes it so we avoid stopping the robot because motors are no longer in run to pos mode
        slave.frontL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.frontR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slave.backR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Resets motor directions
        slave.frontR.setDirection(DcMotor.Direction.FORWARD);
        slave.backR.setDirection(DcMotor.Direction.FORWARD);
    }
}
