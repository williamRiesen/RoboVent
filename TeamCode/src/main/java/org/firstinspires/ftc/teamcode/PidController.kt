package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.util.ElapsedTime

/**
 * Provides output (e.g. motor power) calculated based
 * on PID (Proportional - Integral - Derivative) method and using
 * observed result of prior output passed as argument.
 *
 * Description
 *     <ul>
 *      <li>item 1</li>
 *      <li>item 2</li>
 *      <li>item 3</li>
 *      </ul>
 *
 */
class PidController(var setPoint: Double,
                    var initialOutput: Double,
                    private val kp: Double,
                    private val ki: Double,
                    private val kd: Double) {

    private val pidTimer = ElapsedTime()
    private var integralPrior = 0.0
    private var deltaPrior = 0.0

    fun run(currentValue: Double): Double {
        val timeInterval = pidTimer.seconds()
        pidTimer.reset()
        val delta = currentValue - setPoint
        val integral =  integralPrior + delta * timeInterval
        val derivative = (delta - deltaPrior) / timeInterval
        val output = kp * delta + ki * integral + kd * derivative + initialOutput
        deltaPrior = delta
        integralPrior = integral
        return output
    }

    fun reset(){
        integralPrior = 0.0
        deltaPrior = 0.0
    }
}