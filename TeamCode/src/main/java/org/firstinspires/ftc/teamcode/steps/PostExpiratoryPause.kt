package org.firstinspires.ftc.teamcode.steps

import org.firstinspires.ftc.teamcode.*

class PostExpiratoryPause : BreathCycleStep {

    override val controller = PidController(
            setPoint = 0.0,
            initialOutput = 0.0,
            kp = 0.01,
            ki = 0.0,
            kd = 0.0
    )
    override fun checkForTransition(vent: RoboVent): BreathCycleStep {
        var updatedBreathCycleStep: BreathCycleStep = this
        val cycleTime = SECONDS_IN_A_MINUTE / vent.respiratoryRateSetting
        val patientTriggers = vent.readAirflow() > BASELINE_AIRFLOW_SENSOR_OUTPUT + TRIGGER_THRESHOLD
        if (vent.cycleTimer.seconds() > cycleTime || patientTriggers ) {
            updatedBreathCycleStep = vent.inspiration
            vent.cycleTimer.reset()
//            vent.respiratoryRateCounter.recordBreathGiven()
            controller.reset()
        }
        return updatedBreathCycleStep
        }


    override fun runMotors(vent: RoboVent): Double {
//        vent.setPowerBothMotors(endExpirationHoldPositionController.run(vent.rightVentMotor.currentPosition.toDouble()) )
        vent.setPowerBothMotors(0.0)
        return 0.0
    }

    override fun toString(): String {
        return "End-Expiratory Pause"
    }
}