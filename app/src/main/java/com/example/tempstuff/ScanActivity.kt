package com.example.tempstuff

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView


class ScanActivity : AppCompatActivity(), View.OnClickListener {

    //View Objects
    private var buttonScan: Button? = null
    private var textViewName: TextView? = null
    private var textViewAddress: TextView? = null

    //qr code scanner object
    private var qrScan: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        //View objects
        buttonScan = findViewById(R.id.buttonScan) as Button
        textViewName = findViewById(R.id.textViewID) as TextView

        //intializing scan object
        qrScan = IntentIntegrator(this)

        //attaching onclick listener
        buttonScan!!.setOnClickListener(this)
    }

    //Getting the scan results
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
                    Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()


                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onClick(view: View) {
        //initiating the qr code scan
        qrScan!!.initiateScan()
    }
}
