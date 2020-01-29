package com.example.tempstuff


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    //For scanning
    private var Scan: Button? = null
    private var qrScan: IntentIntegrator? = null
    private var textViewName: TextView? = null
    //End for scanning
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    var Username="kareem1999" //Will be obtained from passing of sign in
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //For QR scanning
        Scan = findViewById(R.id.Scan) as Button
        qrScan = IntentIntegrator(this)
        Scan!!.setOnClickListener(this)
        //End for scanning
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        //To get the name and department
        val db = FirebaseFirestore.getInstance()
        val DevicesRef = db.collection("Users")
        //var   DeviceSearched= DevicesRef.whereEqualTo("Devices",id)
        //Admin Only buttons
        val AddDevice = findViewById(R.id.Add_Device) as Button
        val Approve = findViewById(R.id.Approve) as Button
        val docRef = db.collection("Users").document(Username)//To be changed to login
        var theUser: MyListUser?
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("Found", "DocumentSnapshot data: ${document.data}")
                    docRef.get() //Display Data
                        .addOnSuccessListener { documentSnapshot ->
                            theUser= documentSnapshot.toObject(MyListUser::class.java)


                            val theNameView = findViewById<TextView>(R.id.NameView).apply {
                                text= theUser?.FName + " " + theUser?.LName}
                            val theDeptView = findViewById<TextView>(R.id.DepartmentView).apply {
                                text= theUser?.Department}
                            if(theUser?.Type=="Admin") {
                                AddDevice.visibility = View.VISIBLE
                                Approve.visibility = View.VISIBLE
                            }else{
                                AddDevice.visibility = View.GONE
                                Approve.visibility = View.GONE
                            }
                        } }else {
                    Log.d("Not found", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Error ", "get failed with ", exception)
            }

        setUpRecyclerView()

        Approve.setOnClickListener {

            val intent = Intent(this, BorrowedDevice::class.java).apply {
            }
            startActivity(intent)
        }
    }

    override fun onClick(view: View) {
        //initiating the qr code scan
        qrScan!!.initiateScan()
    }
    //For activity results QR scan
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            //if qrcode has nothing in it
            if (result.contents == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show()
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    val obj = JSONObject(result.contents)
                    //setting values to textviews
                    textViewName!!.text = obj.getString("name")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    //Scan device details
                    Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
                    val intent = Intent(this@MainActivity, DeviceDetails::class.java)
                    intent.putExtra("ID", result.contents)
                    intent.putExtra("Allow", "Yes")
                    intent.putExtra("username", Username)
                    startActivity(intent)
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private val db = FirebaseFirestore.getInstance()
    private val DevicesRef = db.collection("Devices")
    private val UsersRef= db.collection("Users")
    private var adapter: ListAdapter? = null

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_history -> {
                Toast.makeText(this, "History clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_home -> {
                Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_update -> {
                Toast.makeText(this, "Update clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "Sign out clicked", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun setUpRecyclerView() {
        val query = DevicesRef.orderBy("Availability", Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<MyList>()
            .setQuery(query, MyList::class.java)
            .build()

        adapter = ListAdapter(options)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.setAdapter(adapter)
        adapter!!.setOnItemClickListener(object : ListAdapter.OnItemClickListener {
            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {
                val list = documentSnapshot.toObject(MyList::class.java)
                val id = documentSnapshot.getId()
                Toast.makeText(
                    this@MainActivity,
                    "Position: $position ID: $id", Toast.LENGTH_SHORT
                ).show()
                var docRef=UsersRef.document(Username)
                docRef.get().addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(MyListUser::class.java)
                val UType = user?.Type
                val intent = Intent(this@MainActivity, DeviceDetails::class.java)
                //Get user type


                intent.putExtra("ID", id)
                intent.putExtra("Type", UType) //To tell if user is admin

                startActivity(intent)
                }
            }
        })
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
    var Availability: Int= 0
    var Windows: String = ""
    var Department: String =""
    var AdminID: String = ""
    var Condition: String=""
    constructor() {}
    constructor(DName: String , DType: String, DModel: String, DAv: Int, DWindows: String,DDept: String, AID: String, DCondition: String) {
        this.Name = DName
        this.Type = DType
        this.Model= DModel
        this.Availability= DAv
        this.Windows= DWindows
        this.Department= DDept
        this.AdminID= AID
        this.Condition= DCondition
    }



}
class MyListUser {
    var User: String = ""
    var Department: String = ""
    var FName: String= ""
    var MName: String= ""
    var LName: String= ""
    var Password: String= ""
    var Type: String=""
    constructor() {}
    constructor(UName: String, UDepartment: String, UFName: String, UMName: String, ULName: String, UPassword: String, UType: String) {
        this.User = UName
        this.Department = UDepartment
        this.FName= UFName
        this.MName= UMName
        this.LName= ULName
        this.Password= UPassword
        this.Type=UType
    }
}
class ListAdapter(options: FirestoreRecyclerOptions<MyList>) :
    FirestoreRecyclerAdapter<MyList, ListAdapter.MyListHolder>(options) {
    private var listener: OnItemClickListener? = null
    override fun onBindViewHolder(holder: MyListHolder, position: Int, model: MyList) {
        holder.textDeviceName.setText(model.Name)
        holder.textDeviceType.setText(model.Type)
        holder.textDeviceModel.setText(model.Model)
        var check=" "
        if(model.Availability==1) {check="Yes"}else{check="No"}
        holder.textDeviceAvailability.setText(check)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.list_name_button,
            parent, false
        )
        return MyListHolder(v)
    }

    inner class MyListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textDeviceAvailability: TextView
        var textDeviceName: TextView
        var textDeviceType: TextView
        var textDeviceModel: TextView
        init {
            textDeviceType = itemView.findViewById(R.id.DeviceTypeView)
            textDeviceName = itemView.findViewById(R.id.DeviceNameView)
            textDeviceModel = itemView.findViewById(R.id.DeviceModelView)
            textDeviceAvailability = itemView.findViewById(R.id.DeviceAvailabilityView)
            itemView.setOnClickListener {
                val position = getAdapterPosition()
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener!!.onItemClick(getSnapshots().getSnapshot(position), position)


                }
            }
        }


    }
    interface OnItemClickListener {
        fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

