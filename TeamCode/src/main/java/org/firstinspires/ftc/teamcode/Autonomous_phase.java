// This is the code for the autonomous part of the ftc match. The autonomous period goes on for 30 seconds

// import needed package\
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

// create public class that will contain all of our code for the robot

@Autonomous
public class Autonomous_phase extends LinearOpMode{
    @Override
    //create a public method
    public void runOpMode()
        // tell telemetry on phone that the status is initialized
        telemetry.addData("Status: ", "Initialized");
        telemetry.update();
        // get motors and servos from the configuration
        //Servos and Motors are obtained from the configuration with the phones we use. Make sure that the servos have been correctly

        // ie: Servo wrist is a variable that deals with the wrist servo. It is gotten from the Servo class and is called "hand" in the phone configuration
        Servo wrist = hardwareMap.get(Servo.class, "wrist");
        CRServo hand  = hardwareMap.get(CRServo.class, "hand");
        //CrServo is a class that identifies your servo as a continous servo (spinning with power rather setting to a position)
        //CrServos can spin forever (in perfect conditions atleast)
        DcMotor shoulder1      = hardwareMap.dcMotor.get("shoulder1");
        DcMotor shoulder2      = hardwareMap.dcMotor.get("shoulder2");
        //Ant = Anterior Wheel
        // Post = Posterior Wheel
        DcMotor rightAntDrive  = hardwareMap.dcMotor.get("rightAntDrive");
        DcMotor rightPostDrive = hardwareMap.dcMotor.get("rightPostDrive");
        DcMotor leftPostDrive  = hardwareMap.dcMotor.get("leftPostDrive");
        DcMotor leftAntDrive   = hardwareMap.dcMotor.get("leftAntDrive");


        //reset and set the direction that the motors run in
        //reseting is done so that every time you start the robot, it recognizes its start position as being 0 and all other positions being positive
        //directions control which way motors move
        shoulder1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shoulder1.setDirection(DcMotor.Direction.REVERSE);
        shoulder2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shoulder2.setDirection(DcMotor.Direction.REVERSE);
        rightAntDrive.setDirection(DcMotor.Direction.REVERSE);
        rightPostDrive.setDirection(DcMotor.Direction.REVERSE);
        //declare any variables that need to be set before hand
        double shoulderScale     = 0.0;

         //set up substitute values for controller inputs
        double drive  = 0;
        double strafe = 0;
        double turn   = 0;
        boolean grab   = false;

        //set servo positions
        wrist.setPosition(0.1);
        hand.setPower(0);

        //start the robot
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        timer.reset();
        while (opModeIsActive()){
        //////////////EMULATE CONTROLLER INPUTS////////////////
        // as of provincials day, right is negative, left is positive
        //positive in drive is backward whereas forward is neegative

        // FIRST SAMPLE RETRIEVE
        sleep(100);
        //shift right a bit
        moveRobot(0, -0.3, 0, 0, 0.1, false);
        sleep(550);
        //move forward
        moveRobot(-0.5,0,0,0,0,false);
        sleep(1600);
        // move right
        moveRobot(0,-0.5,0,0,0,false);
        sleep(520);
        //move back
        moveRobot(0.5,0,0,0,0,false);
        sleep(1550);

        // SECOND SAMPLE RETRIEVE
        // move forward
        moveRobot(-0.5,0,0,0,0,false);
        sleep(1700);
        //move right
        moveRobot(0,-0.5,0,0,0,false);
        sleep(500);
        //move back
        moveRobot(0.5,0,0,0,0,false);
        sleep(1600);

        // THIRD SAMPLE RETRIEVE
        //move forward
        moveRobot(-0.5,0,0,0,0,false);
        sleep(1500);
        //move right
        moveRobot(0,-0.5,0,0,0,false);
        sleep(450);
        //move backward
        moveRobot(0.5,0,0,0,0,false);
        sleep(1600);

        //PARKING INTO OBSERVATION ZONE
        //move forward
        moveRobot(-0.5,0,0,0,0,false);
        sleep(700);
        //move backward
        moveRobot(0.5,0,0,0,0,false);
        sleep(700);
        //stop forever
        moveRobot(0,0,0,0,0,false);
        sleep(1000000);
    }




    //create a public method that takes a set of variables and uses them to power the robot and control its movements
/////////////////////MOVE ROBOT METHOD///////////////
    public void moveRobot(double drive, double strafe, double turn, double shoulderScale, double wrist, boolean grab){
        //////////////SET UP//////////////////
//        Servo wrist = hardwareMap.get(Servo.class, "wrist");
        CRServo hand  = hardwareMap.get(CRServo.class, "hand");
        DcMotor shoulder1      = hardwareMap.dcMotor.get("shoulder1");
        DcMotor shoulder2      = hardwareMap.dcMotor.get("shoulder2");
        DcMotor rightAntDrive  = hardwareMap.dcMotor.get("rightAntDrive");
        DcMotor rightPostDrive = hardwareMap.dcMotor.get("rightPostDrive");
        DcMotor leftPostDrive  = hardwareMap.dcMotor.get("leftPostDrive");
        DcMotor leftAntDrive   = hardwareMap.dcMotor.get("leftAntDrive");

        shoulder1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shoulder1.setDirection(DcMotor.Direction.REVERSE);
        shoulder2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shoulder2.setDirection(DcMotor.Direction.REVERSE);
        rightAntDrive.setDirection(DcMotor.Direction.REVERSE);
        rightPostDrive.setDirection(DcMotor.Direction.REVERSE);


        /////////////CALCULATE POWER/////////////
        double frontLeftPower  = (drive + strafe - turn);
        double backLeftPower   = (drive - strafe - turn);
        double frontRightPower = (drive - strafe + turn);
        double backRightPower  = (drive + strafe + turn);

        /////////////////SEND POWER////////////////
        rightAntDrive.setPower((double)frontRightPower);
        rightPostDrive.setPower((double)backRightPower);
        leftPostDrive.setPower((double)backLeftPower);
        leftAntDrive.setPower((double)frontLeftPower);

        //grabber
        if (grab) {
            hand.setPower(0.2);
        }
        else {hand.setPower(0);}

        //arm power
        shoulder1.setPower(0.8);
        shoulder1.setTargetPosition((int)(1098 * shoulderScale/100));
        shoulder1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shoulder2.setPower(0.8);
        shoulder2.setTargetPosition((int)(1098 * shoulderScale/100));
        shoulder2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

}



