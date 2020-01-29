package com.example.tempstuff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import androidmads.library.qrgenearator.QRGSaver
import com.google.zxing.WriterException
import android.R.attr.y
import android.R.attr.x
import android.content.Context
import android.view.Display
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGSaver.save
import android.widget.EditText
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar


class GenerateQR : AppCompatActivity() {
    var TAG = "GenerateQRCode"
    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_qr)


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        var DName= intent.getStringExtra("Name")
        var DModel=intent.getStringExtra("Model")
        var DID=intent.getStringExtra("ID")

        var NameModelText=findViewById<TextView>(R.id.Name_Model).apply {
            text= DName+" " +DModel}


        val qrImage = findViewById(R.id.QR_Image) as ImageView
        val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        val height = point.y
        var smallerDimension = if (width < height) width else height
        smallerDimension = smallerDimension * 3 / 4

        var qrgEncoder = QRGEncoder(
            DID, null,
            QRGContents.Type.TEXT,
            smallerDimension
        )
        try {
            var bitmap = qrgEncoder.encodeAsBitmap()
            qrImage.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            Log.v("Exception", e.toString())
        }

    }
}
