package org.firstinspires.ftc.teamcode.steps

import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.PidController
import org.firstinspires.ftc.teamcode.RoboVent
import org.firstinspires.ftc.teamcode.TIDAL_VOLUME_CALIBRATION

class Inspiration : BreathCycleStep {

    private val loopTimer = ElapsedTime()
    private var priorPosition = 0
    private val inspirationSpeedController = PidController(
            setPoint = 750.0,
            initialOutput = 0.3,
            kp = 0.0001,
            ki = 0.0,
            kd = 0.0
    )

    override fun checkForTransition(vent: RoboVent): BreathCycleStep {
        var updatedBreathCycleStep: BreathCycleStep = this
        val endInspiratoryPosition = (vent.tidalVolumeSetting * TIDAL_VOLUME_CALIBRATION).toInt()
        if (vent.rightVentMotor.currentPosition > endInspiratoryPosition) {
            updatedBreathCycleStep = vent.postInspiratoryPause
            inspirationSpeedController.reset()
        }
        return updatedBreathCycleStep
    }

    override fun runMotors(vent: RoboVent): Double {
        val loopTime = loopTimer.seconds()
        loopTimer.reset()
        val currentPosition = vent.rightVentMotor.currentPosition
        val currentSpeed = (currentPosition - priorPosition) / loopTime
        priorPosition = currentPosition
        val power = inspirationSpeedController.run(currentSpeed)
        if (power > vent.peakPower) vent.peakPower = power
        vent.setPowerBothMotors(power)
        return power
    }

    override fun toString(): String {
        return "Inspiration"
    }
}