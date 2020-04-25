package org.firstinspires.ftc.teamcode

interface BreathCycleStep {

    fun checkForTransition(vent: RoboVent): BreathCycleStep

    fun runMotors(vent: RoboVent): Double
}