package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor


@TeleOp(name = "Setup", group = "Linear Opmode")

class Setup : LinearOpMode() {
    lateinit var vent: RoboVent
    private val calibrationModeDrivePower = 0.15

    override fun runOpMode() {
        vent = RoboVent(hardwareMap)

        telemetry.addData("Step 1", "Touch white button when left blue arrows align.")
        telemetry.update()
        calibrateMotorPosition(vent.leftVentMotor)
        Thread.sleep(500)
        telemetry.addData("Step 2", "Touch white button when right blue arrows align.")
        telemetry.update()
        calibrateMotorPosition(vent.rightVentMotor)
        Thread.sleep(500)
        moveToStartPosition(vent.rightVentMotor)
        moveToStartPosition(vent.leftVentMotor)
        while(vent.rightVentMotor.isBusy || vent.leftVentMotor.isBusy){
            Thread.sleep(100)
        }
        resetMotor(vent.rightVentMotor)
        resetMotor(vent.leftVentMotor)
        telemetry.addData("Setup complete", "Touch PLAY arrow on phone to start.")
        telemetry.update()
        Thread.sleep(5000)
    }

    private fun calibrateMotorPosition(motor: DcMotor) {

        motor.power = calibrationModeDrivePower
        while (vent.button.state) {
            Thread.sleep(5)
        }
        motor.power = 0.0
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    private fun moveToStartPosition(motor: DcMotor){
        motor.targetPosition = CALIBRATION_POSITION_TO_START_POSITION_DISTANCE
        motor.mode = DcMotor.RunMode.RUN_TO_POSITION
        motor.power = 0.2
    }

    private fun resetMotor(motor: DcMotor){
        motor.power = 0.0
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }
}


