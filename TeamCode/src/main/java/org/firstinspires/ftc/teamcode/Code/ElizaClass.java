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
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "Elizard", group = "TeleOp")

public class ElizaClass extends OpMode {
    public DcMotor armFaB;
    public DcMotor armUaD;
    HardwareMap hardware;

    @Override
    public void init() {

        try {
            armFaB = hardware.get(DcMotor.class, "DC7");
            armFaB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {
            telemetry.addLine("Arm F&B failed to initialize");
        }
        try {
            armUaD = hardware.get(DcMotor.class, "DC8");
            armUaD.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {
            telemetry.addLine("Arm U&D failed to initialize");
        }
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        double armForwardAndBack = gamepad1.left_stick_y;
        double armUpAndDown = gamepad1.right_stick_y;

        if (armForwardAndBack != 0) {
            armFaB.setPower(-armForwardAndBack);
        } else {
            armFaB.setPower(0);
        }
        if (armUpAndDown != 0) {
            armUaD.setPower(-armUpAndDown);
        } else {
            armUaD.setPower(0);
        }
    }

    @Override
    public void stop() {

    }
}