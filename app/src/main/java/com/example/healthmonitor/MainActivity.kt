package com.example.healthmonitor

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.healthmonitor.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val TAG = "MainActivity"


    private lateinit var auth: FirebaseAuth;
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    var userId: String? = null
    var monitorId : String? = null

    private lateinit var mChart : LineChart
    private var thread: Thread? = null
    private var plotData = true

    private var previousTimestamp: String = "00000"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("UsersData")

        userId = auth.uid
        Log.d(TAG, "Current User: $userId")

        mChart = binding.lineChart

        configLineChart();



        val data = LineData()
        data.setValueTextColor(Color.WHITE)
        mChart.setData(data)


        //Listener for the database
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "New Values: ${dataSnapshot.value.toString()}")

                val timestamp = dataSnapshot.child("timestamp").value as String
                val pulse = dataSnapshot.child("pulse").value as String

//                if (previousTimestamp != timestamp) {
                    Log.d(TAG, "New pulse value: $pulse at timestamp $timestamp")
                    addEntry(pulse)
                    previousTimestamp = timestamp
//                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Error: ${databaseError.message}")
            }
        }

        monitorId = "kg6gVSHnGUVz2sv97B4dqHvx4Jh1"
        var readingsQuery = databaseReference.child(monitorId!!).child("readings")

//        Log.d(TAG, "readings: $readings")
        readingsQuery.addValueEventListener(listener)

    }

    private fun configLineChart() {
        mChart.description.isEnabled = true
        mChart.description.text = "Real-time Pulse Rate"
        // enable touch gestures
        mChart.setTouchEnabled(true)

        // enable scaling and dragging
        mChart.setDragEnabled(true)
        mChart.setScaleEnabled(true)
        mChart.setDrawGridBackground(true)

        mChart.setPinchZoom(true)
        mChart.setBackgroundColor(Color.WHITE)

        val l = mChart.legend
        // modify the legend ...
        l.form = Legend.LegendForm.LINE
        l.textColor = Color.WHITE

        val xl = mChart.xAxis
        xl.textColor = Color.BLUE
        xl.setDrawGridLines(true)
        xl.setAvoidFirstLastClipping(true)
        xl.isEnabled = true

        val leftAxis = mChart.axisLeft
        leftAxis.textColor = Color.BLUE
        leftAxis.setDrawGridLines(false)
//        leftAxis.setAxisMaximum(90f)
        leftAxis.setDrawGridLines(true)

        val rightAxis = mChart.axisRight
        rightAxis.isEnabled = false

        mChart.axisLeft.setDrawGridLines(true)
        mChart.xAxis.setDrawGridLines(true)
        mChart.setDrawBorders(true)
    }

        /*    private fun plotData() {
                if (thread != null) {
                    thread.interrupt()
                }
                thread = Thread {
                    while (true) {
                        plotData = true
                        try {
                            Thread.sleep(10)
                        } catch (e: InterruptedException) {
                            // TODO Auto-generated catch block
                            e.printStackTrace()
                        }
                    }
                }
                thread.start()
            }*/

    private fun addEntry(value: String) {
        val data = mChart.data
        if (data != null) {
            var set = data.getDataSetByIndex(0)
            // set.addEntry(...); // can be called as well
            if (set == null) {
                set = createSet()
                data.addDataSet(set)
            }

//            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
            data.addEntry(Entry(set.entryCount.toFloat(), value.toFloat() ),0)
            data.notifyDataChanged()

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged()

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(150f)
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount().toFloat())
        }
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "Dynamic Data")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.setLineWidth(3f)
        set.setColor(Color.MAGENTA)
        set.isHighlightEnabled = false
        set.setDrawValues(false)
        set.setDrawCircles(false)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setCubicIntensity(0.2f)
        return set
    }
}