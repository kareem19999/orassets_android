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
        var item = MyList("A")
        mylist.add(item)
        item = MyList("B")
        mylist.add(item)
        item = MyList("C")
        mylist.add(item)
        item = MyList("D")
        mylist.add(item)
        item = MyList("E")
        mylist.add(item)
        item = MyList("F")
        mylist.add(item)
        item = MyList("G")
        mylist.add(item)
        item = MyList("H")
        mylist.add(item)
        item = MyList("I")
        mylist.add(item)
        item = MyList("J")
        mylist.add(item)
        item = MyList("K")
        mylist.add(item)
        item = MyList("L")
        mylist.add(item)
        item = MyList("M")
        mylist.add(item)
        item = MyList("N")
        mylist.add(item)
        item = MyList("O")
        mylist.add(item)
        item = MyList("P")
        mylist.add(item)
        item = MyList("Q")
        mylist.add(item)
        item = MyList("R")
        mylist.add(item)
        item = MyList("S")
        mylist.add(item)


        mAdapter!!.notifyDataSetChanged()
    }
}

//The class for the items in the list to be added, can be expanded
class MyList {
    var firstText: String = ""

    constructor() {}
    constructor(firstText: String) {
        this.firstText = firstText
    }

    fun getName(): String {
        return firstText
    }

    fun setName(name: String) {
        this.firstText = name
    }

}
class ListAdapter(var theList: List<MyList>) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var theText: TextView
        var theButton: Button
        init {
            theText = view.findViewById(R.id.firstTextView)
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
        holder.theText.setText(device.getName())

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