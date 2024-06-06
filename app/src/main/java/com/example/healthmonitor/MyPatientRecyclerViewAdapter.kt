package com.example.healthmonitor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthmonitor.databinding.FragmentItemBinding
import kotlin.random.Random

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyPatientRecyclerViewAdapter(
    private val context: Context,
//    private val values: List<PlaceholderItem>
    private val localDataSet: ArrayList<PatientData>

) : RecyclerView.Adapter<MyPatientRecyclerViewAdapter.ViewHolder>() {

    private val TAG = "MyPatientRecyclerViewAdapter"
//    private lateinit var mListener = onItemClickListener
//
//    interface onItemClickListener{
//        fun onItemClick(position: Int)
//    }
//
//    fun setOnItemClickListener(listener: onItemClickListener){
//        mListener = listener
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = localDataSet[position]
        holder.sNoView.text = item.id.toString()
        holder.nameView.text = item.name
        holder.ageView.text = "Age: " + Random.nextInt(25,70).toString()
        holder.imgView.setImageResource(item.imgResId)

        holder.cardView.setOnClickListener {
            // Invoking the ClickInterface
            Toast.makeText(context, "CardView Clicked", Toast.LENGTH_LONG)
            Log.d(TAG, "onBindViewHolder: CardView Clicked $position")

            val intent = Intent(context, MainActivity::class.java)
        }

    }

    override fun getItemCount(): Int = localDataSet.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val sNoView: TextView = binding.slNoTv
        val nameView: TextView = binding.nameTv
        val ageView: TextView = binding.ageTv
        val imgView: ImageView = binding.patientImgView

        val cardView: CardView = binding.listCardView

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "'"
        }
    }



}