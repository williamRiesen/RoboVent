package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.steps.*

const val ALARM_RESET_POSITION = 0.0
const val ALARM_STRIKE_POSITION = 0.12

class RoboVent(hardwareMap: HardwareMap) {

    val rightVentMotor: DcMotor = hardwareMap.get(DcMotor::class.java, "vent_motor")
    val leftVentMotor: DcMotor = hardwareMap.get(DcMotor::class.java, "vent_motor2")
    val button: DigitalChannel = hardwareMap.get<DigitalChannel>(DigitalChannel::class.java, "sensor_digital")
    private val airflowSensor = hardwareMap.get(I2cDeviceSynch::class.java, "airflow_sensor")!!
    val leftKnob = hardwareMap.get(AnalogInput::class.java, "rate_control")
    val rightKnob = hardwareMap.get(AnalogInput::class.java, "volume_control")
    private val alarmBell: Servo = hardwareMap.get(Servo::class.java, "alarm_bell")

    var apneaAlarm = false
    var noReturnFlowAlarm = false
    var tachypneaAlarm = false
    var highPressureAlarm = false


    private val alarmsSilenced
        get() = silenceTimer.seconds() < 60
    var peakPower = 0.0

    private val apneaTimer = ElapsedTime()
    private val returnFlowTimer = ElapsedTime()
    private val bellTimer = ElapsedTime()
    private val silenceTimer = ElapsedTime()
    private val blinkTimer = ElapsedTime()
    val cycleTimer = ElapsedTime()


    val respiratoryRateSetting
        get() = leftKnob.voltage / leftKnob.maxVoltage * 24

    val tidalVolumeSetting
        get() = rightKnob.voltage / rightKnob.maxVoltage * 950

//    val respiratoryRateCounter = RespiratoryRateCounter()

    init {
        rightVentMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        leftVentMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        rightVentMotor.direction = DcMotorSimple.Direction.REVERSE
        button.mode = DigitalChannel.Mode.INPUT
        airflowSensor.engage()
        val manufacturerAddress = I2cAddr.create7bit(0x49)
        airflowSensor.i2cAddress = manufacturerAddress
//        respiratoryRateCounter.breathLog.add(0.0)
    }


    fun readAirflow(): Int {
        val reading = airflowSensor.read(0, 2)
        return reading[0] * 64 + reading[1]
    }

    fun updateAlarmConditions() {
        if (!button.state) silenceTimer.reset()
        highPressureAlarm = peakPower > HIGH_PRESSURE_ALARM_THRESHOLD
        val airflow = readAirflow()
        if (airflow > 1000) apneaTimer.reset()
        apneaAlarm = apneaTimer.seconds() > 10.0
        if (airflow < 250) returnFlowTimer.reset()
        noReturnFlowAlarm = returnFlowTimer.seconds() > 10.0
//        tachypneaAlarm = respiratoryRateCounter.readRate() > TACHYPNEA_THRESHOLD
    }

    fun updateAlarmBell() {

        if (bellTimer.seconds() > 2.5) bellTimer.reset()
        val alarmBellCount = when {
            alarmsSilenced -> 0
            apneaAlarm -> 4
            noReturnFlowAlarm -> 3
            tachypneaAlarm -> 2
            highPressureAlarm  -> 1
            else -> 0
        }
        when (bellTimer.seconds()) {
            in (0.0..0.2) -> {
                if (alarmBellCount > 0) strikeAlarmBell()
            }
            in (0.2..0.5) -> resetAlarmStriker()
            in (0.5..0.7) -> {
                if (alarmBellCount > 1) strikeAlarmBell()
            }
            in (0.7..1.0) -> resetAlarmStriker()
            in (1.0..1.2) -> {
                if (alarmBellCount > 2) strikeAlarmBell()
            }
            in (1.2..1.5) -> resetAlarmStriker()
            in (1.5..1.7) -> {
                if (alarmBellCount > 3) strikeAlarmBell()
            }
            in (1.7..2.0) -> resetAlarmStriker()
            in (2.0..2.5) -> {
            }
            else -> bellTimer.reset()

        }
    }

    private fun strikeAlarmBell() {
        alarmBell.position = ALARM_STRIKE_POSITION
    }

    private fun resetAlarmStriker() {
        alarmBell.position = ALARM_RESET_POSITION
    }

    fun setPowerBothMotors(power: Double) {
        rightVentMotor.power = power
        synchronizeMotors(power)
    }

    private val motorSynchronizer = PidController(
            setPoint = 0.0,
            initialOutput = 0.3,
            kp = 0.001,
            ki = 0.0,
            kd = 0.0
    )

    private fun synchronizeMotors(power: Double) {
        val motorDiscrepancy = (leftVentMotor.currentPosition - rightVentMotor.currentPosition).toDouble()
        motorSynchronizer.initialOutput = power
        leftVentMotor.power = motorSynchronizer.run(motorDiscrepancy)
    }

    val inspiration: BreathCycleStep = Inspiration()
    val postInspiratoryPause: BreathCycleStep = PostInspiratoryPause((tidalVolumeSetting * TIDAL_VOLUME_CALIBRATION).toInt())
    val expiration: BreathCycleStep = Expiration()
    val postExpiratoryPause: BreathCycleStep = PostExpiratoryPause()
    val steadyDrive: BreathCycleStep = SteadyDrive() // (Diagnostic)

}