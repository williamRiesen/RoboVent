package org.firstinspires.ftc.teamcode.steps

import org.firstinspires.ftc.teamcode.*

class PostInspiratoryPause(initialTargetPosition: Int) : BreathCycleStep {

    override val controller = PidController(
            setPoint = initialTargetPosition.toDouble(),
            initialOutput = 0.0,
            kp = 0.0,
            ki = 0.0,
            kd = 0.0
    )

    override fun checkForTransition(vent: RoboVent): BreathCycleStep {
        var updatedBreathCycleStep: BreathCycleStep = this
        val cycleTime = SECONDS_IN_A_MINUTE / vent.respiratoryRateSetting
        val startExpirationTime = cycleTime * I_TO_E_RATIO
        if (vent.cycleTimer.seconds() > startExpirationTime) {
            updatedBreathCycleStep = vent.expiration
            controller.reset()
        }
        return updatedBreathCycleStep
    }

    override fun runMotors(vent: RoboVent): Double {
        controller.setPoint = vent.tidalVolumeSetting * TIDAL_VOLUME_CALIBRATION
        vent.setPowerBothMotors(0.0)
        return controller.run(vent.rightVentMotor.currentPosition.toDouble())
    }


    override fun toString(): String {
        return "End-Inspiratory Pause"
    }


}