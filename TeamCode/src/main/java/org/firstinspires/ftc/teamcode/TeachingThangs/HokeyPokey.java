package org.firstinspires.ftc.teamcode.TeachingThangs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Helpers.Dancer;
import org.firstinspires.ftc.teamcode.Helpers.SlaveAuto;

@Autonomous(name = "Hokey Pokey", group = "Slave")
public class HokeyPokey extends LinearOpMode {
    private Dancer dancer = new Dancer();
    static double SPEED = 1;

    @Override
    public void runOpMode(){
        dancer.init(hardwareMap);

        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        waitForStart();
        if(opModeIsActive()){
            leftE(470);
            rightE(470);
            leftE(470);
            rotateLeft(3720);
            rightE(470);
            leftE(470);
            rightE(470);
            rotateRight(3720);
        }
    }

    public void forwardE(int ticks){
        encodeResetAndRun();

        dancer.right.setTargetPosition(-ticks);
        dancer.left.setTargetPosition(ticks);

        power();

        while(opModeIsActive() && dancer.right.isBusy())
        {
            telemetry.addLine("" + dancer.right.getCurrentPosition());
            telemetry.addLine("" + dancer.left.getCurrentPosition());
            telemetry.addData("Moving forward", ticks);
            telemetry.update();
        }

        nStop();
    }

    public void backwardE(int ticks){
        encodeResetAndRun();

        dancer.right.setTargetPosition(ticks);
        dancer.left.setTargetPosition(-ticks);

        power();

        while(opModeIsActive() && dancer.right.isBusy())
        {
            telemetry.addLine("" + dancer.right.getCurrentPosition());
            telemetry.addLine("" + dancer.left.getCurrentPosition());
            telemetry.addData("Moving Backward", ticks);
            telemetry.update();
        }

        nStop();
    }

    public void rightE(int ticks){
        encodeResetAndRun();

        dancer.front.setTargetPosition(ticks);
        dancer.back.setTargetPosition(-ticks);

        power();

        while(opModeIsActive() && dancer.front.isBusy()){
            telemetry.addLine("" + dancer.front.getCurrentPosition());
            telemetry.addLine("" + dancer.back.getCurrentPosition());
            telemetry.addData("Moving Right", ticks);
            telemetry.update();
        }

        nStop();
    }

    public void leftE(int ticks){
        encodeResetAndRun();

        dancer.front.setTargetPosition(-ticks);
        dancer.back.setTargetPosition(ticks);

        power();

        while(opModeIsActive() && dancer.front.isBusy()){
            telemetry.addLine("" + dancer.front.getCurrentPosition());
            telemetry.addLine("" + dancer.back.getCurrentPosition());
            telemetry.addData("Moving Left", ticks);
            telemetry.update();
        }

        nStop();
    }

    public void rotateRight(int ticks){
        encodeResetAndRun();

        dancer.front.setTargetPosition(ticks);
        dancer.back.setTargetPosition(ticks);
        dancer.left.setTargetPosition(ticks);
        dancer.right.setTargetPosition(ticks);

        power();

        while(opModeIsActive() && dancer.front.isBusy())
        {
            telemetry.addLine("" + dancer.front.getCurrentPosition());
            telemetry.addLine("" + dancer.back.getCurrentPosition());
            telemetry.addLine("" + dancer.right.getCurrentPosition());
            telemetry.addLine("" + dancer.left.getCurrentPosition());
            telemetry.addData("Rotating Right", ticks);
            telemetry.update();
        }

        nStop();

        System.out.printf("rotated right");
    }

    public void rotateLeft(int ticks){
        encodeResetAndRun();

        dancer.front.setTargetPosition(-ticks);
        dancer.back.setTargetPosition(-ticks);
        dancer.left.setTargetPosition(-ticks);
        dancer.right.setTargetPosition(-ticks);

        power();

        while(opModeIsActive() && dancer.front.isBusy())
        {
            telemetry.addLine("" + dancer.front.getCurrentPosition());
            telemetry.addLine("" + dancer.back.getCurrentPosition());
            telemetry.addLine("" + dancer.right.getCurrentPosition());
            telemetry.addLine("" + dancer.left.getCurrentPosition());
            telemetry.addData("Rotating Left", ticks);
            telemetry.update();
        }

        nStop();

        System.out.printf("rotated left");
    }

    public void encodeResetAndRun(){
        dancer.front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dancer.back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dancer.left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dancer.right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dancer.front.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        dancer.back.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        dancer.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        dancer.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void nStop(){
        dancer.front.setPower(0);
        dancer.back.setPower(0);
        dancer.left.setPower(0);
        dancer.right.setPower(0);
    }

    public void power(){
        dancer.front.setPower(SPEED);
        dancer.back.setPower(SPEED);
        dancer.left.setPower(SPEED);
        dancer.right.setPower(SPEED);
    }
}
