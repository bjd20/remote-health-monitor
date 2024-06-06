package com.example.healthmonitor

import android.net.wifi.p2p.WifiP2pManager.ActionListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentOnAttachListener
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PatientFragment : Fragment() {

    private val TAG = "PatientListFragment"

    private var columnCount = 1
    private lateinit var patientList : ArrayList<PatientData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        patientList = ArrayList<PatientData>()

        patientList = ArrayList<PatientData>()
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

            val patientItem = PatientData(i + 1, patientName, 34)
            patientList!!.add(patientItem)
        }

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)

            var serializable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(ARG_PATIENT_LIST, PatientData::class.java)
            } else {
                TODO("VERSION.SDK_INT < TIRAMISU")
                it.getSerializable(ARG_PATIENT_LIST)!! as PatientData
            }
//            patientList =  serializable as ArrayList<PatientData>
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_patient_list, container, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.recycler_listView)

        // Set the adapter
        if (recyclerView is RecyclerView) {
            with(recyclerView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyPatientRecyclerViewAdapter(requireActivity().baseContext,patientList)
            }
        }

        for (p in  patientList){
            Log.d(TAG, "onCreateView: $p")
        }
        return view
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_PATIENT_LIST = "patient-list"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, patientList: ArrayList<PatientData>) =
            PatientFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putSerializable(ARG_PATIENT_LIST,patientList)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}