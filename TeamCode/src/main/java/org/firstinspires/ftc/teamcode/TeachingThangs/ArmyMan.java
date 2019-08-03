package org.firstinspires.ftc.teamcode.TeachingThangs;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Helpers.Dancer;
import org.firstinspires.ftc.teamcode.Helpers.Slave;

@TeleOp (name = "ArmyMan")
public class ArmyMan extends OpMode {
    private Dancer dance = new Dancer();

    public void init(){
        dance.init(hardwareMap);
        telemetry.addLine("RUNNING");
        telemetry.update();
    }

    public void init_loop(){
        telemetry.addLine(dance.getStatus());
        if(dance.getStatus().equals(""))
            telemetry.addData("Status", "WORKING");
        telemetry.addData("Front", dance.front);
        telemetry.addData("Back", dance.back);
        telemetry.addData("Right", dance.right);
        telemetry.addData("Left", dance.left);
        telemetry.addData("Arm", dance.arm);
        telemetry.update();
    }

    public void start(){

    }

    public void stop(){

    }

    public void loop(){
        boolean butY = gamepad1.y;
        boolean butX = gamepad1.x;

        if(butY){
            dance.arm.setPower(.5);
        }
        else if(butX){
            dance.arm.setPower(-.5);
        }
        else
            dance.arm.setPower(0);
    }
}
