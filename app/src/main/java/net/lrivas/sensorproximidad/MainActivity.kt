package net.lrivas.sensorproximidad

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var miSensor : Sensor
    private lateinit var administradorDeSensores : SensorManager
    private lateinit var disparadorEventoSensor : SensorEventListener
    private lateinit var lblValor : TextView
    private lateinit var btnValor : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        lblValor = findViewById(R.id.lblValorProximidad)
        btnValor = findViewById(R.id.btnValor)

        // Inicializar mi sensor
        administradorDeSensores = getSystemService(SENSOR_SERVICE) as SensorManager
        miSensor = administradorDeSensores.getDefaultSensor(Sensor.TYPE_PROXIMITY) ?: run {
            Toast.makeText(this, "Su dispositivo no tiene sensor de proximidad", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        Toast.makeText(this, "Sensor de Proximidad detectado", Toast.LENGTH_SHORT).show();

        disparadorEventoSensor = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                // Colocar el codigo de lo que queremos que haga nuestra app
                //cuando se acerque o se aleje el objeto del sensor
                val valor = event.values[0];
                lblValor.text = "Valor del sensor: $valor"

                if (valor < miSensor.maximumRange){
                    //Condicion para determinar cuando se acerque
                    btnValor.setBackgroundColor(Color.RED)
                    btnValor.text = "Se ha acercado al sensor!"
                } else {
                    //Que es lo que va hacer cuando se aleje
                    btnValor.setBackgroundColor(Color.GREEN)
                    btnValor.text = "Se ha alejado del sensor!"
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }
        }

        iniciarSensor();
    }

    private fun iniciarSensor(){
       administradorDeSensores.registerListener(disparadorEventoSensor, miSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun detenerSensor(){
        administradorDeSensores.unregisterListener(disparadorEventoSensor)
    }

    override fun onPause() {
        detenerSensor()
        super.onPause()
    }

    override fun onResume() {
        iniciarSensor()
        super.onResume()
    }
}