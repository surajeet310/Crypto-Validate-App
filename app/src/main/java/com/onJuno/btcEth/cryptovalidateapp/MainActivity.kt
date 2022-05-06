package com.onJuno.btcEth.cryptovalidateapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var scanBtcBtn:Button
    private lateinit var scanEthBtn:Button
    private lateinit var validateBtn:Button
    private lateinit var shareBtn:Button
    private lateinit var addressText:TextView
    private lateinit var resultCardView:MaterialCardView
    private var btcAddress:String = ""
    private var ethAddress:String = ""
    private val CAMERA_REQUEST_CODE = 100
    private var isPermitted = MutableLiveData<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scanBtcBtn = findViewById(R.id.scan_btc_btn)
        scanEthBtn = findViewById(R.id.scan_eth_btn)
        addressText = findViewById(R.id.qr_code_data)
        resultCardView = findViewById(R.id.result_card_view)
        validateBtn = findViewById(R.id.validate_btn)
        val startScannerActivityForBtc = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){
                if (it.data != null){
                    btcAddress = it.data!!.getStringExtra("data").toString()
                }
                else{
                    btcAddress = "None"
                }
                resultCardView.visibility = View.VISIBLE
                addressText.text = "Scanned BTC Address: "+btcAddress
            }
        }
        val startScannerActivityForEth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){
                if (it.data != null){
                    ethAddress = it.data!!.getStringExtra("data").toString()
                }
                else{
                    ethAddress = "None"
                }
                resultCardView.visibility = View.VISIBLE
                addressText.text = "Scanned ETH Address: "+ethAddress
            }
        }
        checkPermissions()
        scanBtcBtn.setOnClickListener {
            ethAddress = ""
            isPermitted.observe(this, Observer { status->
                if (status){
                    startScannerActivityForBtc.launch(Intent(this,ScannerActivity::class.java))
                }
                else{
                    Snackbar.make(it,"Permission for Camera is required to scan QR",Snackbar.LENGTH_SHORT).show()
                }
            })
        }
        scanEthBtn.setOnClickListener {
            btcAddress = ""
            isPermitted.observe(this, Observer { status->
                if (status){
                    startScannerActivityForEth.launch(Intent(this,ScannerActivity::class.java))
                }
                else{
                    Snackbar.make(it,"Permission for Camera is required to scan QR",Snackbar.LENGTH_SHORT).show()
                }
            })
        }

        validateBtn.setOnClickListener {
            if ((btcAddress != "")&&(btcAddress != "None")){
                if (Validators.validateBTCAddress(btcAddress.trim())){
                    Snackbar.make(it,"The Scanned BTC Address is valid",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    Snackbar.make(it,"The Scanned BTC Address is invalid",Snackbar.LENGTH_SHORT).show()
                }
            }

            if ((ethAddress != "")&&(ethAddress != "None")){
                if (Validators.validateETHAddress(ethAddress.trim())){
                    Snackbar.make(it,"The Scanned ETH Address is valid",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    Snackbar.make(it,"The Scanned ETH Address is invalid",Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
        }
        else{
            isPermitted.postValue(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                isPermitted.postValue(true)
            }
            else{
                isPermitted.postValue(false)
            }
        }
    }
}