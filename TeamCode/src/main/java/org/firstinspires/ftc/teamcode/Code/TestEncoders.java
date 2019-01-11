/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Encoder Movements", group="Linear Opmode")
public class TestEncoders extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    @Override
    public void runOpMode() {

        frontLeft = hardwareMap.get(DcMotor.class, "DC1");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight = hardwareMap.get(DcMotor.class, "DC2");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backLeft = hardwareMap.get(DcMotor.class, "DC4");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backRight = hardwareMap.get(DcMotor.class, "DC3");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();

        if (opModeIsActive()) {
            //moveForward(0.5, 1000);
            //strafeLeft(0.5, 1000);
            //strafeRight(0.5, 2000);
            //strafeLeft(0.5, 1000);
            //moveBackward(0.5, 1000);
            rotateLeft(.5,1000);
        }

    }

    public void moveBackward(double power, int distance) {

        // Set Mode: RESET and RUN TO POSITION
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set Target Position
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + distance);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() - distance);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + distance);
        backRight.setTargetPosition(backRight.getCurrentPosition() - distance);

        // Wait until start is pressed
        waitForStart();

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive() && frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {

            // While its attempting to move into position, give power
            telemetry.addLine("Current Position: " + frontLeft.getCurrentPosition());
            telemetry.addLine("Current Position: " + frontRight.getCurrentPosition());
            telemetry.addLine("Current Position: " + backLeft.getCurrentPosition());
            telemetry.addLine("Current Position: " + backRight.getCurrentPosition());

            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);

            telemetry.update();

        }

        // Stop robot
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        // Reset mode
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void moveForward(double power, int distance) {

        // Set Mode: RESET and RUN TO POSITION
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set Target Position
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() - distance);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + distance);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() - distance);
        backRight.setTargetPosition(backRight.getCurrentPosition() + distance);

        // Wait until start is pressed
        waitForStart();

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive() && frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {

            // While its attempting to move into position, give power
            telemetry.addLine("Current Position: " + frontLeft.getCurrentPosition());
            telemetry.addLine("Current Position: " + frontRight.getCurrentPosition());
            telemetry.addLine("Current Position: " + backLeft.getCurrentPosition());
            telemetry.addLine("Current Position: " + backRight.getCurrentPosition());

            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);

            telemetry.update();

        }

        // Stop robot
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        // Reset mode
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void rotateRight(double power, int distance) {

        // Set Mode: RESET and RUN TO POSITION
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set Target Position
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() - distance);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() - distance);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() - distance);
        backRight.setTargetPosition(backRight.getCurrentPosition() - distance);

        // Wait until start is pressed
        waitForStart();

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive() && frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {

            // While its attempting to move into position, give power
            telemetry.addLine("Current Position: " + frontLeft.getCurrentPosition());
            telemetry.addLine("Current Position: " + frontRight.getCurrentPosition());
            telemetry.addLine("Current Position: " + backLeft.getCurrentPosition());
            telemetry.addLine("Current Position: " + backRight.getCurrentPosition());

            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);

            telemetry.update();

        }

        // Stop robot
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        // Reset mode
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void rotateLeft(double power, int distance) {

        // Set Mode: RESET and RUN TO POSITION
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set Target Position
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + distance);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + distance);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + distance);
        backRight.setTargetPosition(backRight.getCurrentPosition() + distance);

        // Wait until start is pressed
        waitForStart();

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive() && frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {

            // While its attempting to move into position, give power
            telemetry.addLine("Current Position: " + frontLeft.getCurrentPosition());
            telemetry.addLine("Current Position: " + frontRight.getCurrentPosition());
            telemetry.addLine("Current Position: " + backLeft.getCurrentPosition());
            telemetry.addLine("Current Position: " + backRight.getCurrentPosition());

            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);

            telemetry.update();

        }

        // Stop robot
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        // Reset mode
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void strafeLeft(double power, int distance) {

        // Set Mode: RESET and RUN TO POSITION
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set Target Position
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() - distance);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() - distance);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + distance);
        backRight.setTargetPosition(backRight.getCurrentPosition() + distance);

        // Wait until start is pressed
        waitForStart();

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive() && frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {

            // While its attempting to move into position, give power
            telemetry.addLine("Current Position: " + frontLeft.getCurrentPosition());
            telemetry.addLine("Current Position: " + frontRight.getCurrentPosition());
            telemetry.addLine("Current Position: " + backLeft.getCurrentPosition());
            telemetry.addLine("Current Position: " + backRight.getCurrentPosition());

            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);

            telemetry.update();

        }

        // Stop robot
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        // Reset mode
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void strafeRight(double power, int distance) {

        // Set Mode: RESET and RUN TO POSITION
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set Target Position
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + distance);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + distance);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() - distance);
        backRight.setTargetPosition(backRight.getCurrentPosition() - distance);

        // Wait until start is pressed
        waitForStart();

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive() && frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()) {

            // While its attempting to move into position, give power
            telemetry.addLine("Current Position: " + frontLeft.getCurrentPosition());
            telemetry.addLine("Current Position: " + frontRight.getCurrentPosition());
            telemetry.addLine("Current Position: " + backLeft.getCurrentPosition());
            telemetry.addLine("Current Position: " + backRight.getCurrentPosition());

            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);

            telemetry.update();

        }

        // Stop robot
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        // Reset mode
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

}
