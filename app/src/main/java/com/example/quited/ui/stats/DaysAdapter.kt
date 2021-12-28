package com.example.quited.ui.stats

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.quited.R
import com.example.quited.domain.model.DayStat
import com.example.quited.presentation.util.Date

class DaysAdapter(val rView: RecyclerView, var days: List<DayStat>):
        RecyclerView.Adapter<DaysAdapter.MyViewHolder>(){

    val todayDate = Date.fromUTC(System.currentTimeMillis())

    class MyViewHolder(iv: View) : RecyclerView.ViewHolder(iv){
        var number: TextView? = iv.findViewById(R.id.numberText)
        var ciggs: TextView? = iv.findViewById(R.id.ciggsText)
        var date: TextView? = iv.findViewById(R.id.dateText)
        val cardView: CardView? = iv.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.day_stat_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(h: MyViewHolder, i: Int) {
        h.number?.text = String.format(rView.resources.getString(R.string.day_number), (i+1).toString())
        h.ciggs?.text = String.format(rView.resources.getString(R.string.ciggs), days[i].ciggsAmount.toString(), days[i].maxAmount.toString())
        h.date?.text = days[i].date.dateStr
        if (days[i].date.dateLong == todayDate.dateLong){
            h.cardView?.setCardBackgroundColor(rView.resources.getColor(R.color.teal_700))
            h.cardView?.cardElevation = 5F
        }
        else {
            h.cardView?.setCardBackgroundColor(Color.TRANSPARENT)
            h.cardView?.cardElevation = 0F

        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

}