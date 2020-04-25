package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.util.ElapsedTime

class Expiration : BreathCycleStep {

    private val loopTimer = ElapsedTime()
    private var priorPosition = 0
    private val expirationSpeedController =  PidController(
            setPoint = 300.0,
            initialOutput = -0.2,
            kp = 0.0,
            ki = 0.0,
            kd = 0.0
    )

    override fun checkForTransition(vent: RoboVent): BreathCycleStep {
        val endExpiratoryPosition = 0
        var updatedBreathCycleStep: BreathCycleStep = this
         if (vent.rightVentMotor.currentPosition < endExpiratoryPosition) {
             updatedBreathCycleStep = vent.postExpiratoryPause
             expirationSpeedController.reset()
         }
        return updatedBreathCycleStep
    }

    override fun runMotors(vent: RoboVent): Double {
        val loopTime = loopTimer.seconds()
        loopTimer.reset()
        val currentPosition = vent.rightVentMotor.currentPosition
        val currentSpeed = (currentPosition - priorPosition) / loopTime
        priorPosition = currentPosition
        val power = expirationSpeedController.run(currentSpeed)
        if (power > vent.peakPower) vent.peakPower = power
//        vent.setPowerBothMotors(power)
        vent.setPowerBothMotors(-0.2)
        return power
    }
    override fun toString(): String {
        return "Expiration"
    }
}