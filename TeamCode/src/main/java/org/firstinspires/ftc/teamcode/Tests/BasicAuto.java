/**
 * This is the package ftc gives coders which basically has classes we might need to import for our code instead of us downloading
 and importing every single individual class; the package makes things more convenient
 **/
package org.firstinspires.ftc.teamcode.Tests;

/**
 * These are all the classes we're choosing to import from the package because we need these packages for our code in this file
 * If you see an import line in gray and underlined, it means we imported a class that we ended up not using
 */
import android.view.Display;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; //Details what type of program this is (the AutoAttemptEncoders part can be TeleOp instead)
import com.qualcomm.robotcore.eventloop.opmode.Disabled; //Allows us to use the line @Disabled (explained further down)
import com.qualcomm.robotcore.util.ElapsedTime; //Acts as a timer for our code (this autonomous example is time-based BUT there are
                                                // other ways to do autonomous too)
import com.qualcomm.robotcore.hardware.DcMotor; //DcMotor class that lets us map our DcMotor variable to the robot's motors
import com.qualcomm.robotcore.hardware.Servo; //Servo class that lets us map our Servo variable to the robot's servos
import com.qualcomm.robotcore.eventloop.opmode.OpMode; //Makes our code work for the phone (very basic)
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode; //Stems from OpMode, it makes it so that our code runs once linearly
                                                            //instead of running in a LOOP like a regular, basic OpMode
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * The @ is utilized to detail what type of program this is on the phone that way the phone knows how to run the code
 * After describing what type of program this is, you would put (name = "Whatever you wanna name the file") **NO SEMICOLON**
 * The name part is just you giving a name to the file (it would show up on the phone under this name)
 * You can also do (name = "Name", group = "Robot") where group is used for organization purposes
 */
@Autonomous(name = "Basic Autonomous FTC", group = "Slave")
@Disabled //Makes it so this code does NOT show up on the phone, we don't really use this line since we upload per file not per folder
        //of files

/**
 * OpMode is a program that can run in either Autonomous or TeleOp (defines how the robot behaves)
 * Through [extends OpMode] we inherit the properties of an OpMode (class) which includes
 @Override                  @Override
 public void init(){}       public void loop(){}
 * We're building onto the bare bones of the OpMode class that FTC has created (making it into our OWN code)
 * In autonomous, while we can use [extends OpMode], it's easier if we used [extends LinearOpMode] since it acts similar to
 public static void main(String args[])
 *
 */
public class BasicAuto extends LinearOpMode
{
    //Here is where you would declare all the variables that you will need in your code (this includes robot parts too)
    private ElapsedTime runtime = new ElapsedTime(); //Our timer
    private DcMotor frontL; //Our motors
    private DcMotor frontR;
    private DcMotor backL;
    private DcMotor backR;
    private Servo armSpin; //Our servos

    //This is a final variable that we create in order to make sure the speed the motors move at is CONSTANT and DOESN'T CHANGE
    static final double SPEED = 1;

    /**@Override means we're writing over the bare bones of LinearOpMode
     * This is where you would hardwareMap your variables to the actual motors/servos of the robot
     * Basically, this makes it so that your code is being used on the appropriate part of the robot
     */
    @Override
    public void runOpMode()
    {
        /**
         * Try and catch basically says "try this code but if it doesn't work, catch the error (exception) that happens and do this
         with it
         * It's useful so that if a part of our code doesn't work, it doesn't mean that our robot will be completely useless
         * At least we can use the robot for the parts that DO work
         * One of the options we have with catch(Exception e) is to throw the exception (aka disregard it) which is what we did above
         with [throws InterruptedException]
         * Up until the next set of green comments, this part will run when the driver hits INIT on the phone
         **/
        try
        {
            //The String parameter is in reference to what we named that motor on the phone when it recognized the robot parts
            frontL = hardwareMap.get(DcMotor.class, "DC1"); //frontL = hardwareMap.get(dcMotor.class, "DC1"); is the syntax
            frontL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            /*
            Encoders are wires that send/receive directions between the robot brain and motors (specific measurements of how far
            the motor has moved which is measured in ticks where 1 tick = 1 degree out of the motor's 360 degree range),
            we have to specify if we have them or not as seen above. For time, encoders are NOT needed
            */
        }
        catch(Exception e){}
        try
        {
            frontR = hardwareMap.get(DcMotor.class, "DC2");
            frontR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch(Exception e){}
        try
        {
            backR = hardwareMap.get(DcMotor.class, "DC3");
            backR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch(Exception e){}
        try
        {
            backL = hardwareMap.get(DcMotor.class, "DC4");
            backL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch(Exception e){}

        //For convenience, we will print on the phone that the robot is ready
        telemetry.addData("Status", "Ready to run"); //Same as System.out.println();
        telemetry.update(); //Makes it show up on the phone

        /**
         * Now that we have finished INITIALIZING (connecting our variables to the robot), our robot will just stand there UNTIL
         the driver hits the PLAY button (which appears after hitting the INIT button)
         * INIT is when you would connect your robot to your code, PLAY is when your movement code would start running
         */
        waitForStart();
        runtime.reset(); //Since we created the variable, the timer has been counting so we have to reset it for our movement code
                        //so that we start at zero (and it's easier to code with)

        //Driving forward for 3 seconds
        forward();
        while (opModeIsActive() && (runtime.seconds() < 3))
        {
            telemetry.addData("Moving Forward", "Time Elapsed:", runtime.seconds());
            telemetry.update();
        }
        //Rotating left for 1.3 seconds
        rotateLeft();
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 1.3)
        {
            telemetry.addData("Rotating Left", "Time Elapsed:", runtime.seconds());
            telemetry.update();
        }
        //Rotating right for 0.5 seconds
        rotateRight();
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 0.5)
        {
            telemetry.addData("Rotating Right", "Time Elapsed:", runtime.seconds());
            telemetry.update();
        }
        //Moving right for 2 seconds
        right();
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 2)
        {
            telemetry.addData("Moving Right", "Time Elapsed:", runtime.seconds());
        }
        //Stop the autonomous program
        stop();
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);

    }
    public void stop(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(0);
            backL.setPower(0);
            frontR.setPower(0);
            backR.setPower(0);
        }
    }
    public void forward(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(SPEED);
            backL.setPower(SPEED);
            frontR.setPower(-SPEED);
            backR.setPower(-SPEED);
        }
        stop(0.5);
    }
    public void backward(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(-SPEED);
            backL.setPower(-SPEED);
            frontR.setPower(SPEED);
            backR.setPower(SPEED);
        }
        stop(0.5);
    }
    public void right(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(SPEED);
            frontR.setPower(SPEED);
            backR.setPower(-SPEED);
            backL.setPower(-SPEED);
        }
        stop(0.5);
    }
    public void left(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(-SPEED);
            frontR.setPower(-SPEED);
            backL.setPower(SPEED);
            backR.setPower(SPEED);
        }
        stop(0.5);
    }
    public void rotateRight(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(SPEED);
            frontR.setPower(SPEED);
            backL.setPower(SPEED);
            backR.setPower(SPEED);
        }
        stop(0.5);
    }
    public void rotateLeft(double time)
    {
        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() <= time)
        {
            frontL.setPower(-SPEED);
            frontR.setPower(-SPEED);
            backL.setPower(-SPEED);
            backR.setPower(-SPEED);
        }
        stop(0.5);
    }
    public void forward()
    {
        frontL.setPower(SPEED);
        backL.setPower(SPEED);
        frontR.setPower(-SPEED);
        backR.setPower(-SPEED);
    }
    public void backward()
    {
        frontL.setPower(-SPEED);
        backL.setPower(-SPEED);
        frontR.setPower(SPEED);
        backR.setPower(SPEED);
    }
    public void right()
    {
        frontL.setPower(SPEED);
        frontR.setPower(SPEED);
        backR.setPower(-SPEED);
        backL.setPower(-SPEED);
    }
    public void left()
    {
        frontL.setPower(-SPEED);
        frontR.setPower(-SPEED);
        backL.setPower(SPEED);
        backR.setPower(SPEED);
    }
    public void rotateRight()
    {
        frontL.setPower(SPEED);
        frontR.setPower(SPEED);
        backL.setPower(SPEED);
        backR.setPower(SPEED);
    }
    public void rotateLeft()
    {
        frontL.setPower(-SPEED);
        frontR.setPower(-SPEED);
        backL.setPower(-SPEED);
        backR.setPower(-SPEED);
    }
    public void forwards(long time)
    {
        frontL.setPower(SPEED);
        backL.setPower(SPEED);
        frontR.setPower(-SPEED);
        backR.setPower(-SPEED);
        sleep(time);
    }
    public void backwards(long time)
    {
        frontL.setPower(-SPEED);
        backL.setPower(-SPEED);
        frontR.setPower(SPEED);
        backR.setPower(SPEED);
        sleep(time);
    }
    public void rights(long time)
    {
        frontL.setPower(SPEED);
        frontR.setPower(SPEED);
        backR.setPower(-SPEED);
        backL.setPower(-SPEED);
        sleep(time);
    }
    public void lefts(long time)
    {
        frontL.setPower(-SPEED);
        frontR.setPower(-SPEED);
        backL.setPower(SPEED);
        backR.setPower(SPEED);
        sleep(time);
    }
    public void rotateRights(long time)
    {
        frontL.setPower(SPEED);
        frontR.setPower(SPEED);
        backL.setPower(SPEED);
        backR.setPower(SPEED);
        sleep(time);
    }
    public void rotateLefts(long time)
    {
        frontL.setPower(-SPEED);
        frontR.setPower(-SPEED);
        backL.setPower(-SPEED);
        backR.setPower(-SPEED);
        sleep(time);
    }


/*
    /*@Override
    public void loop() {

    }

    static final double     FORWARD_SPEED = 0.6;
    static final double     TURN_SPEED    = 0.5;

    public void runOpMode() {

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path, ensuring that the AutoAttemptEncoders mode has not been stopped along the way

        // Step 1:  Drive forward for 3 seconds
        robot.leftDrive.setPower(FORWARD_SPEED);
        robot.rightDrive.setPower(FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 2:  Spin right for 1.3 seconds
        robot.leftDrive.setPower(TURN_SPEED);
        robot.rightDrive.setPower(-TURN_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.3)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 3:  Drive Backwards for 1 Second
        robot.leftDrive.setPower(-FORWARD_SPEED);
        robot.rightDrive.setPower(-FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Leg 3: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 4:  Stop and close the claw.
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.leftClaw.setPosition(1.0);
        robot.rightClaw.setPosition(0.0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
    */
}
