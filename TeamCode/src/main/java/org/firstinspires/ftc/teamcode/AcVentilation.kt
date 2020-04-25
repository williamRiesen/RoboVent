package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp


@TeleOp(name = "Assist / Control", group = "RoboVent")
class AcVentilation: OpMode() {
    private lateinit var vent: RoboVent
    private lateinit var currentBreathCycleStep: BreathCycleStep

    override fun init(){
        vent = RoboVent(hardwareMap)
        currentBreathCycleStep =  vent.inspiration
    }

    override fun init_loop() {
        telemetry.addData("Resp Rate Setting: ", vent.respiratoryRateSetting)
        telemetry.addData("Tidal Volume Setting: ", vent.tidalVolumeSetting)
        telemetry.update()
        vent.respiratoryRateCounter.respiratoryRateTimer.reset()
    }

    override fun loop() {
        currentBreathCycleStep = currentBreathCycleStep.checkForTransition(vent)
        currentBreathCycleStep.runMotors(vent)
        vent.updateAlarmConditions()
        vent.updateAlarmBell()
        updateDisplay()
    }

    private fun updateDisplay(){
        var alarmString = ""
        if (vent.apneaAlarm) alarmString += "APNEA  "
        if (vent.noReturnFlowAlarm)alarmString += "NO RETURN FLOW  "
        if (vent.tachypneaAlarm) alarmString += "TACHYPNEA   "
        if (vent.highPressureAlarm) alarmString += "HIGH PRESSURE  "
        if (alarmString == "") alarmString = "OK"
        telemetry.addData("-", alarmString)
        telemetry.addLine("Resp Rate-  ")
                .addData("Set", vent.respiratoryRateSetting.toInt())
                .addData("Actual", vent.respiratoryRateCounter.readRate())
        telemetry.update()
    }
}