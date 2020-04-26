package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor


@TeleOp(name = "Setup", group = "Linear Opmode")

class Setup:  LinearOpMode() {
val calibrationModeDrivePower = 0.15

    override fun runOpMode(){
        val vent = RoboVent(hardwareMap)
        telemetry.addData("Step 1", "Touch white button when left blue arrows align.")
        telemetry.update()
        vent.leftVentMotor.power = calibrationModeDrivePower
        while (vent.button.state) {
            Thread.sleep(10)
        }
        vent.leftVentMotor.power = 0.0
        vent.leftVentMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        vent.leftVentMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        telemetry.addData("Left motor done.", null)
        telemetry.update()
        Thread.sleep(1000)

        telemetry.addData("Step 2", "Touch white button when right blue arrows align.")
        telemetry.update()
        vent.rightVentMotor.power = calibrationModeDrivePower
        while (vent.button.state) {
            Thread.sleep(10)
        }
        vent.rightVentMotor.power = 0.0
        vent.rightVentMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        vent.rightVentMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        telemetry.addData("Right motor done.", null)
        telemetry.update()
        Thread.sleep(1000)





        telemetry.addData("Setup complete.", null)
        telemetry.update()

    }
}


