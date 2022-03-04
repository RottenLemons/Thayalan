package com.example.thayalan

import android.content.Context
import android.content.Intent
import android.hardware.*
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged


class MainActivity : AppCompatActivity() {
    private lateinit var unit: String
    private lateinit var sensorManager: SensorManager
    private var compass: Sensor? = null
    private var image: ImageView? = null
    private var currentDegree = 0f
    lateinit var geoField: GeomagneticField
    private val location: Location = Location("A")
    private val target: Location = Location("B")
    private val locationManager: LocationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        location.setLatitude(1.30726808971077);
        location.setLongitude(103.76929483724936);
        geoField = GeomagneticField(location.latitude.toFloat(), location.longitude.toFloat(), 100F, System.currentTimeMillis())

        target.setLatitude(1.4359767045623293);
        target.setLongitude(103.8241452525948);

        val spinner: Spinner = findViewById(R.id.spinner)
        val editText: EditText = findViewById(R.id.editText)
        val textView: TextView = findViewById(R.id.textView)
        val callBtn: Button = findViewById(R.id.call)
        val messageBtn: Button = findViewById(R.id.message)
        val emailBtn: Button = findViewById(R.id.email)
        val redditBtn: Button = findViewById(R.id.reddit)
        image = findViewById(R.id.imageViewCompass)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        compass = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        if(compass != null){
            sensorManager.registerListener(object : SensorEventListener {
                override fun onSensorChanged(p0: SensorEvent) {
                    var degree = Math.round(p0.values.get(0)).toFloat()
                    degree += geoField.getDeclination()

                    val bearing: Float = location.bearingTo(target)
                    degree = (bearing - degree) * -1
                    degree = normalizeDegree(degree)

                    // create a rotation animation (reverse turn degree degrees)

                    // create a rotation animation (reverse turn degree degrees)
                    val ra = RotateAnimation(
                        currentDegree,
                        -degree,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f
                    )

                    // how long the animation will take place

                    // how long the animation will take place
                    ra.duration = 210

                    // set the animation after the end of the reservation status

                    // set the animation after the end of the reservation status
                    ra.fillAfter = true

                    // Start the animation

                    // Start the animation
                    image!!.startAnimation(ra)
                    currentDegree = -degree
                }

                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                }
            }, compass, SensorManager.SENSOR_DELAY_GAME);
        } else {
            Toast.makeText(getApplicationContext(),"Device have no compass!",Toast.LENGTH_SHORT).show();
        }
        callBtn.setOnClickListener {
            var intent : Intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+6583553809")
            startActivity(intent)
        }

        messageBtn.setOnClickListener {
            var intent : Intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("smsto:+6583553809")
            startActivity(intent)
        }

        emailBtn.setOnClickListener {
            var intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:h1910152@nushigh.edu.sg")
            startActivity(intent)
        }

        redditBtn.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.reddit.com/r/thayafandom/"))
            startActivity(intent)
        }
        val unitList = resources.getStringArray(R.array.units)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, unitList)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                unit = unitList[position]
                if (unit == "Thayam") {
                    editText.hint = "Kilogram"
                } else if (unit == "Thayal") {
                    editText.hint = "Metre"
                } else {
                    editText.hint = "Seconds"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(
                    this@MainActivity,
                    "No planet selected! Select a planet.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        editText.doOnTextChanged { text, start, before, count ->
            if (editText.text.isNotEmpty()) {
                try {
                    if (unit == "Thayam") {
                        textView.text = (editText.text.toString().toDouble() / 72).toString()
                    } else if (unit == "Thayal") {
                        textView.text = (editText.text.toString().toDouble() / 1.69).toString()
                    } else {
                        textView.text = (editText.text.toString().toDouble() / 32).toString()
                    }
                } catch (e : Exception) {

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(object : SensorEventListener {
            override fun onSensorChanged(p0: SensorEvent) {
                var degree = Math.round(p0.values.get(0)).toFloat()
                degree += geoField.getDeclination()

                val bearing: Float = location.bearingTo(target)
                degree = (bearing - degree) * -1
                degree = normalizeDegree(degree)

                // create a rotation animation (reverse turn degree degrees)

                // create a rotation animation (reverse turn degree degrees)
                val ra = RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )

                // how long the animation will take place

                // how long the animation will take place
                ra.duration = 210

                // set the animation after the end of the reservation status

                // set the animation after the end of the reservation status
                ra.fillAfter = true

                // Start the animation

                // Start the animation
                image!!.startAnimation(ra)
                currentDegree = -degree
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            }
        }, compass, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(object : SensorEventListener {
            override fun onSensorChanged(p0: SensorEvent) {
                var degree = Math.round(p0.values.get(0)).toFloat()
                degree += geoField.getDeclination()

                val bearing: Float = location.bearingTo(target)
                degree = (bearing - degree) * -1
                degree = normalizeDegree(degree)

                // create a rotation animation (reverse turn degree degrees)

                // create a rotation animation (reverse turn degree degrees)
                val ra = RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )

                // how long the animation will take place

                // how long the animation will take place
                ra.duration = 210

                // set the animation after the end of the reservation status

                // set the animation after the end of the reservation status
                ra.fillAfter = true

                // Start the animation

                // Start the animation
                image!!.startAnimation(ra)
                currentDegree = -degree
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            }
        })
    }

    private fun normalizeDegree(value: Float): Float {
        return if (value >= 0.0f && value <= 180.0f) {
            value
        } else {
            180 + (180 + value)
        }
    }

}