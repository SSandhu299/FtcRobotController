// This is the code for the teleop phase of the code - where players put live inputs to control how the robots movements
// import needed packages

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

//create a public class that will contain all of our code'
@TeleOp // this is said to inform the drive station app where this file should be shown - the autonomous or teleop side of the code
public class Teleop_phase extends LinearOpMode {
    //intialize by creating a public method
    public void runOpMode(){
        // get motors from congiguration
        Servo wrist = hardwareMap.get(Servo.class, "wrist");
        CRServo hand  = hardwareMap.get(CRServo.class, "hand");
        DcMotor shoulder1 = hardwareMap.dcMotor.get("shoulder1");
        DcMotor shoulder2 = hardwareMap.dcMotor.get("shoulder2");
        DcMotor rightAntDrive  = hardwareMap.dcMotor.get("rightAntDrive");
        DcMotor rightPostDrive = hardwareMap.dcMotor.get("rightPostDrive");
        DcMotor leftPostDrive  = hardwareMap.dcMotor.get("leftPostDrive");
        DcMotor leftAntDrive   = hardwareMap.dcMotor.get("leftAntDrive");

        //reset motors so that their start position is seen as zero
        //set direction of motors so that they move the right way
        shoulder1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shoulder1.setDirection(DcMotor.Direction.REVERSE);
        shoulder2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shoulder2.setDirection(DcMotor.Direction.REVERSE);
        leftAntDrive.setDirection(DcMotor.Direction.REVERSE);
        leftPostDrive.setDirection(DcMotor.Direction.REVERSE);
        // declare variables needed to be declared before hand

        double shoulderIncrement = 0.4; //this determines how fast the shoulder moves
        //coefficient for total drive base speed
        double driveCoefficient = 0.75;
        //scale and increments
        double shoulderScale = 0;
        double wrist_scale = 0;
        double handScale = 35;

        //run loop that loops through code while playing
        // loop is to change things happening to the robot live
        // code within should contain buttons that chang shoulder position, actuate the servo, etc
        waitForStart();
        while (opModeIsActive()){

            //drive train
            double drive  =  gamepad1.left_stick_y;
            double strafe = -gamepad1.left_stick_x;
            double turn   =  gamepad1.right_stick_x;
            double frontLeftPower  = (drive + strafe - turn);
            double backLeftPower   = (drive - strafe - turn);
            double frontRightPower = (drive - strafe + turn);
            double backRightPower  = (drive + strafe + turn);

            //Armjoystick
            //needs to be in the while loop because it continously checks for the joystick value
            double armjoystick = -gamepad2.left_stick_y;
            double wristjoystick = gamepad2.right_stick_y;

            //arm go up
            if (armjoystick > 0.2 && shoulderScale < 100 ){
                shoulderScale += shoulderIncrement;
            }
            //arm go down
            if (armjoystick < -0.2 && shoulderScale > 0){
                shoulderScale -= shoulderIncrement;
            }

            //INTAKE SYSTEM CONTROL
            //right bumper hit -> spin one direction
            //left bumper hit -> spin other direction
            // if left and right bumper hit at same time -> no power = stop spin
            if (gamepad2.right_bumper) {hand.setPower(1);}
            if (gamepad2.left_bumper) {hand.setPower(-1);}
            if (gamepad2.left_bumper & gamepad2.right_bumper){hand.setPower(0);}

            //preset condition for key button "y"
            if (gamepad2.y) {
                if (shoulderScale < 55) {shoulderScale += shoulderIncrement;}
                if (shoulderScale > 55) {shoulderScale -= shoulderIncrement;}
                wrist.setPosition(0.2);
            }


            if (gamepad2.left_trigger < 0){
                wrist.setPosition(0.85);
            }

            //speed modulation
            if (gamepad1.right_bumper) {driveCoefficient = 0.3;}
            else {driveCoefficient = 0.9;}

            //failsafe for if robot glitches out mid ,match
            //this code sets it back to where it is supposed to be and then resets its encoders to make sure everything works
            if (gamepad2.dpad_down){
                shoulder1.setPower(-1);
                shoulder2.setPower(-1);
                sleep(2000); // sleep is here to make the command above work for 2000 miliseconds
                shoulder1.setPower(0);
                shoulder2.setPower(0);
                shoulder1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                shoulder2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }

            //SETTING POWER FOR MOTORS AND SERVOs

            //SETTING POWER FOR WHEELS
            rightAntDrive.setPower(Math.pow(frontRightPower, 1)*driveCoefficient);
            rightPostDrive.setPower(Math.pow(backRightPower, 1)*driveCoefficient);
            leftPostDrive.setPower(Math.pow(backLeftPower, 1)*driveCoefficient);
            leftAntDrive.setPower(Math.pow(frontLeftPower, 1)*driveCoefficient);

            //SETTING POWER FOR SHOULDER JOINT MOTORS "shoulder1" and "shoulder2"
            shoulder1.setPower(0.8);
            shoulder1.setTargetPosition((int)(1230 * (shoulderScale)/100));
            shoulder1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            shoulder2.setPower(0.8);
            shoulder2.setTargetPosition((int)(1230 * (shoulderScale-fudge)/100));
            shoulder2.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            //telemetry
            // telemetry is stuff that shows up on your phone
            //It can be used to check variables
            telemetry.addData("right trigger: ", gamepad2.right_bumper);
            telemetry.addData("left trigger: ", gamepad2.left_bumper);
            telemetry.update(); //updates the telemetry every run through - hence why it is at the bottom of the code

    }


}


