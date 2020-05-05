package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Diagnostic OpMode", group = "Iterative Opmode")
class DiagnosticOpMode: OpMode() {

    override fun init() {
        telemetry.addData("Status", "Initialized")
    }

    override fun loop() {
        Thread.sleep(1000)
    }

}