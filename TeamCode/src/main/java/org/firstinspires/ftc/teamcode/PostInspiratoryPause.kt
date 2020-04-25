package org.firstinspires.ftc.teamcode

class PostInspiratoryPause(initialTargetPosition: Int) : BreathCycleStep {

    private val endInspirationHoldPositionController = PidController(
            setPoint = initialTargetPosition.toDouble(),
            initialOutput = 0.0,
            kp = 0.01,
            ki = 0.0,
            kd = 0.0
    )

    override fun checkForTransition(vent: RoboVent): BreathCycleStep{
        var updatedBreathCycleStep: BreathCycleStep = this
        val cycleTime = SECONDS_IN_A_MINUTE / vent.respiratoryRateSetting
        val startExpirationTime = cycleTime * I_TO_E_RATIO
        if (vent.cycleTimer.seconds() > startExpirationTime) {
            updatedBreathCycleStep = vent.expiration
            endInspirationHoldPositionController.reset()
        }
        return updatedBreathCycleStep
    }

    override fun runMotors(vent: RoboVent): Double {
        endInspirationHoldPositionController.setPoint = vent.tidalVolumeSetting * TIDAL_VOLUME_CALIBRATION
        vent.setPowerBothMotors(0.0)
        return endInspirationHoldPositionController.run(vent.rightVentMotor.currentPosition.toDouble())
    }


    override fun toString(): String {
        return "End-Inspiratory Pause"
    }


}