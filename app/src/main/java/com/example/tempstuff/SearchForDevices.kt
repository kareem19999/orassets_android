package com.example.tempstuff

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
const val EXTRA_MESSAGE = "com.example.karim.tempstuff.MESSAGE"

class SearchForDevices : AppCompatActivity() {
    private val mylist = ArrayList<MyList>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: ListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_device)

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView

        mAdapter = ListAdapter(mylist)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter


        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle("")
        preparelistData()
    }

    private fun preparelistData() {
        var item = MyList("A","B","C")
        mylist.add(item)
        item = MyList("B","C","D")
        mylist.add(item)

        mAdapter!!.notifyDataSetChanged()
    }
}

//The class for the items in the list to be added, can be expanded
class MyList {
    var DeviceNameText: String = ""
    var DeviceTypeText: String = ""
    var DeviceModelText: String = ""
    constructor() {}
    constructor(DeviceName: String , DeviceType: String, DeviceModel: String) {
        this.DeviceNameText = DeviceName
        this.DeviceTypeText = DeviceType
        this.DeviceModelText= DeviceModel
    }

    fun getName(): String {
        return DeviceNameText
    }

    fun setName(Name: String) {
        this.DeviceNameText = Name
    }
    fun getType(): String {
        return DeviceTypeText
    }

    fun setType(Type: String) {
        this.DeviceTypeText = Type
    }
    fun getModel(): String {
        return DeviceModelText
    }

    fun setModel(Model: String) {
        this.DeviceModelText = Model
    }

}
class ListAdapter(var theList: List<MyList>) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var theType: TextView
        var theButton: Button
        init {
            theType = view.findViewById(R.id.DeviceTypeView)
            theButton = view.findViewById(R.id.button2)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_name_button, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val device = theList[position]
        holder.theType.setText(device.getType())

        //holder.theButton.setOnClickListener {
            //lateinit var context : Context
            //var message = device.getName()
            //var intent= Intent(context, TestScreen::class.java).
                //putExtra(EXTRA_MESSAGE, message)
            //startActivity(context, intent, null)
        //}
    }

    override fun getItemCount(): Int {
        return theList.size
    }
}