package com.example.healthmonitor

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.healthmonitor.databinding.ActivityPatientListBinding

class PatientListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPatientListBinding
    private val TAG = "PatientListActivity"

    private var fManager: FragmentManager? = null
    private lateinit var pItemList: ArrayList<PatientData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPatientListBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fManager = supportFragmentManager
        pItemList = ArrayList<PatientData>()
        val patientNameArray = getResources().getStringArray(R.array.patients)

        // Storing the patient array from strings.xml file to an ArrayList
        for (i in patientNameArray.indices) {
            val patientName: String = patientNameArray[i]
            // To get the drawable image resource ID based on a string variable

            val imageResId = resources.getDrawable(R.drawable.patient, null)

//            val imageResId = getResources().getIdentifier(
//                country.lowercase(Locale.getDefault()), "drawable",
//                packageName
//            )

            val patientItem = PatientData(i + 1, patientName, 34, R.drawable.patient)
            pItemList!!.add(patientItem)
        }

        val patientFragment: PatientFragment = PatientFragment.newInstance(1, pItemList)

//        patientFragment.setCallBackInterface(this);

        loadFragment(patientFragment);
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentTransaction = fManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.addToBackStack(fragment.toString())
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commit()
    }
}