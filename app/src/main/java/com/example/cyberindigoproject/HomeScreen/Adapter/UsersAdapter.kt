package com.example.cyberindigoproject.HomeScreen.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cyberindigoproject.R

class UsersAdapter( var context: Context,  var items: ArrayList<UserData>): RecyclerView.Adapter<UsersAdapter.ViewHolder>(),
    Filterable {

    var filterData = ArrayList<UserData>()
    // exampleListFull . exampleList

    init {
        filterData = items
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
            .inflate(R.layout.user_list_layout, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UsersAdapter.ViewHolder, position: Int) {
        var userData = filterData[position]
        holder?.txtName?.text = userData.name
        holder?.txtComment?.text = "Email-Id : "+userData.email


        if (userData.avatar !== null) {
            Glide.with(context)
                .load(userData.avatar)
                .into(holder.imgUserImage!!)
        } else {
            holder.imgUserImage?.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    override fun getItemCount(): Int {
        return filterData.size
    }

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var txtName: TextView? = null
        var txtComment: TextView? = null
        var imgUserImage: ImageView? = null

        init {
            this.txtName = row?.findViewById<TextView>(R.id.tv_name)
            this.txtComment = row?.findViewById<TextView>(R.id.tv_email)
            this.imgUserImage = row?.findViewById<ImageView>(R.id.iv_user_image)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) filterData = items else {
                    val filteredList = ArrayList<UserData>()
                    items
                        .filter { it.name!!.toLowerCase().contains(charString.toLowerCase())  }
                        .forEach { filteredList.add(it) }
                    filterData = filteredList
                }
                return FilterResults().apply { values = filterData }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterData = results?.values as ArrayList<UserData>

                notifyDataSetChanged()
            }
        }
    }
}