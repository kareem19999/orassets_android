package com.example.tempstuff
/**
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.google.firebase.firestore.Query

import com.firebase.ui.firestore.FirestoreRecyclerOptions


const val EXTRA_MESSAGE = "com.example.karim.tempstuff.MESSAGE"

class SearchForDevices : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val DevicesRef = db.collection("Devices")

    private var adapter: ListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_device)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val query = DevicesRef.orderBy("Device ID", Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<MyList>()
            .setQuery(query, MyList::class.java)
            .build()

        adapter = ListAdapter(options)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.setAdapter(adapter)
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }
}

//The class for the items in the list to be added, can be expanded
class MyList {
    var Name: String = ""
    var Type: String = ""
    var Model: String = ""
    constructor() {}
    constructor(DName: String , DType: String, DModel: String) {
        this.Name = DName
        this.Type = DType
        this.Model= DModel
    }



}
class ListAdapter(options: FirestoreRecyclerOptions<MyList>) :
    FirestoreRecyclerAdapter<MyList, ListAdapter.MyListHolder>(options) {

    override fun onBindViewHolder(holder: MyListHolder, position: Int, model: MyList) {
        holder.textDeviceName.setText(model.Name)
        holder.textDeviceType.setText(model.Type)
        holder.textDeviceModel.setText(model.Model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.list_name_button,
            parent, false
        )
        return MyListHolder(v)
    }

    inner class MyListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textDeviceName: TextView
        var textDeviceType: TextView
        var textDeviceModel: TextView

        init {
            textDeviceName = itemView.findViewById(R.id.DeviceTypeView)
            textDeviceType = itemView.findViewById(R.id.DeviceNameView)
            textDeviceModel = itemView.findViewById(R.id.DeviceModelView)
        }
    }
}
        */