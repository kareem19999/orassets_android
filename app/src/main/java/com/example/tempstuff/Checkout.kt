package com.example.tempstuff


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class Checkout : AppCompatActivity() {
    //For scanning

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        //setSupportActionBar(findViewById(R.id.toolbar))
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //To get the name and department
        val db = FirebaseFirestore.getInstance()
        val LogRef = db.collection("Log")
        //Get only unapproved devices
        val docRef = db.collection("Log").whereEqualTo("Approval",0)//To be changed to login
        var theUser: MyListUser?
        setUpRecyclerView()
    }

    private val db = FirebaseFirestore.getInstance()
    private val LogRef = db.collection("Log")
    private var adapter: CheckoutAdapter? = null


    private fun setUpRecyclerView() {
        val query = LogRef.orderBy("Borrow", Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<MyCheckout>()
            .setQuery(query, MyCheckout::class.java)
            .build()

        adapter = CheckoutAdapter(options)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.setAdapter(adapter)
        adapter!!.setOnItemClickListener(object : CheckoutAdapter.OnItemClickListener {
            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {
                val list = documentSnapshot.toObject(MyCheckout::class.java)
                val id = documentSnapshot.getId()
                Toast.makeText(this@Checkout, "Position: $position ID: $id", Toast.LENGTH_SHORT).show()
                var docRefgetLog=LogRef.document(id)
                docRefgetLog.get().addOnSuccessListener { documentSnapshot ->
                    val Log = documentSnapshot.toObject(MyListUser::class.java)
                    Toast.makeText(
                        this@Checkout,
                        "Position: $position ID: $id", Toast.LENGTH_SHORT
                    ).show()
                    //Change the status here?
                    val db = FirebaseFirestore.getInstance()
                    val docRef = db.collection("Log").document(id)//To be changed to login
                    var theUser: MyListUser?
                    docRef.update("Approval", 1, "Borrow", 2)
                        .addOnSuccessListener {
                            android.util.Log.d(

                                "Changed Availability",
                                "DocumentSnapshot successfully written!"
                            )
                            Toast.makeText(
                                this@Checkout,
                                "Device Freed", Toast.LENGTH_SHORT
                            ).show()
                            //Change the availability of device
                            docRef.get().addOnSuccessListener { documentSnapshot ->
                                var theLog= documentSnapshot.toObject(MyCheckout::class.java)
                                var deviceID=theLog?.DeviceID
                                docRef.get().addOnSuccessListener { documentSnapshot ->
                                    var theLog= documentSnapshot.toObject(com.example.tempstuff.MyCheckout::class.java)
                                    var deviceID=theLog?.DeviceID
                                    val docRefDevice = db.collection("Devices").document(deviceID.toString())//To be changed t
                                    docRefDevice.update("Availability", 1)

                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            android.util.Log.w(
                                "Error Changing",
                                "Error writing document",
                                e
                            )
                            Toast.makeText(
                                this@Checkout,
                                "Failed To Approve", Toast.LENGTH_SHORT
                            ).show()
                        }
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

class MyCheckout{
    var Approval: Int=0
    var Borrow: Int=0
    var DeviceID: String=""
    var Username: String=""
    constructor(){}
    constructor(DA: Int, DB: Int, DID: String, DUser: String)
    {
        Approval=DA
        Borrow=DB
        DeviceID=DID
        Username=DUser
    }
}

class CheckoutAdapter(options: FirestoreRecyclerOptions<MyCheckout>) :
    FirestoreRecyclerAdapter<MyCheckout, CheckoutAdapter.MyListHolder>(options) {
    private var listener: OnItemClickListener? = null
    override fun onBindViewHolder(holder: MyListHolder, position: Int, model: MyCheckout) {
        //Query here to get name and device to be pushed to string
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Users").document(model.Username)//To be changed to login
        var theUser: MyListUser?
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("Found", "DocumentSnapshot data: ${document.data}")
                    docRef.get() //Display Data
                        .addOnSuccessListener { documentSnapshot ->
                            theUser= documentSnapshot.toObject(MyListUser::class.java)
                            var firstChar=theUser?.MName
                            var UserText= theUser?.FName + " " + firstChar.toString().get(0) + ". " + theUser?.LName
                            holder.textBorrowerName.setText(UserText)

                        } }else {
                    Log.d("Not found", "No such document")
                }
            }
        val docRefDev = db.collection("Devices").document(model.DeviceID)//To be changed to login
        var theDevice: MyList?
        docRefDev.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("Found", "DocumentSnapshot data: ${document.data}")
                    docRefDev.get() //Display Data
                        .addOnSuccessListener { documentSnapshot ->
                            theDevice= documentSnapshot.toObject(MyList::class.java)
                            var DeviceText= theDevice?.Name + " " + theDevice?.Model
                            holder.textDeviceName.setText(DeviceText)
                        } }else {
                    Log.d("Not found", "No such document")
                }
            }
        var ApprovalText=""
        if(model.Borrow==0)
        {
            ApprovalText="Waiting"
        }else if(model.Borrow==1){
            ApprovalText="Borrowed"
        }else{
            ApprovalText="Available"
        }
        holder.textStatusName.setText(ApprovalText)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.borrower_layout,
            parent, false
        )
        return MyListHolder(v)
    }

    inner class MyListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textDeviceName: TextView
        var textBorrowerName: TextView
        var textStatusName: TextView
        init {
            textDeviceName = itemView.findViewById(R.id.DeviceNameView)
            textBorrowerName = itemView.findViewById(R.id.BorrowerNameView)
            textStatusName = itemView.findViewById(R.id.BorrowerStatusView)
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


