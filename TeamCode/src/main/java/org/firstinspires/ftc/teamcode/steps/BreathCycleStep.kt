package org.firstinspires.ftc.teamcode.steps

import org.firstinspires.ftc.teamcode.PidController
import org.firstinspires.ftc.teamcode.RoboVent

interface BreathCycleStep {

    fun checkForTransition(vent: RoboVent): BreathCycleStep

    fun runMotors(vent: RoboVent): Double

    val controller: PidController
}