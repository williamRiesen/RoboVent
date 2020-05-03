package org.firstinspires.ftc.teamcode

import android.util.Log
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime

@TeleOp(name = "Diagnostic OpMode", group = "Iterative Opmode")
class DiagnosticOpMode: OpMode() {
    private val runtime = ElapsedTime()
    private var loopCount = 0


    override fun init() {
        telemetry.addData("Status", "Initialized")
    }

    override fun loop() {
        if ( runtime.seconds() > 60) resetStartTime()
        loopCount += 1
        telemetry.addData("Loop Count", loopCount)
        telemetry.addData("Status", "Run Time: $runtime")
    }

    override fun stop() {
        super.stop()
        Log.v("Yip", "Yip!")
    }


}