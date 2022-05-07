package com.onJuno.btcEth.cryptovalidateapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var scanBtcBtn:Button
    private lateinit var scanEthBtn:Button
    private lateinit var validateBtn:Button
    private lateinit var shareBtn:Button
    private lateinit var addressText:TextView
    private lateinit var resultCardView:MaterialCardView
    private var btcAddress:String = "None"
    private var ethAddress:String = "None"
    private val CAMERA_REQUEST_CODE = 100
    private var isPermitted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scanBtcBtn = findViewById(R.id.scan_btc_btn)
        scanEthBtn = findViewById(R.id.scan_eth_btn)
        addressText = findViewById(R.id.qr_code_data)
        resultCardView = findViewById(R.id.result_card_view)
        validateBtn = findViewById(R.id.validate_btn)
        shareBtn = findViewById(R.id.share_btn)
        /**
         * In order to differentiate which address(BTC/ETH) is going to be scanned, two ActivityResultLauncher variables viz.
         * 'startScannerActivityForBtc' and 'startScannerActivityForEth' are initialized, which are used to launch the QR code scanning activity.
         * The 'startScannerActivityForBtc' sets the data received from the scanner activity to the 'btcAddress'. Similarly,
         * 'startScannerActivityForEth' sets the data received from the scanner activity to the 'ethAddress'.
         */
        val startScannerActivityForBtc = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){
                if ((it.data != null)&&(it.data!!.getStringExtra("data").toString().isNotEmpty())){
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
                if ((it.data != null)&&(it.data!!.getStringExtra("data").toString().isNotEmpty())){
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
        /**
         *'ethAddress' is the string which holds the ETH address.
         * When the 'BTC' button is clicked, the 'ethAddress' is set to None in order to make sure that there is only one available address(BTC)
         * to validate and share, when the 'Validate' and 'Share' button is clicked.
         */
        scanBtcBtn.setOnClickListener {
            ethAddress = "None"
            /**
             * 'isPermitted' denotes whether the user has granted the permission or not.
             */
            if (isPermitted){
                startScannerActivityForBtc.launch(Intent(this,ScannerActivity::class.java))
            }
            else{
                Snackbar.make(it,"Permission for Camera is required to scan QR",Snackbar.LENGTH_SHORT).show()
            }
        }
        /**
         *'btcAddress' is the string which holds the BTC address.
         * When the 'ETH' button is clicked, the 'btcAddress' is set to None in order to make sure that there is only one available address(ETH)
         * to validate and share, when the 'Validate' and 'Share' button is clicked.
         */
        scanEthBtn.setOnClickListener {
            btcAddress = "None"
            if (isPermitted){
                startScannerActivityForEth.launch(Intent(this,ScannerActivity::class.java))
            }
            else{
                Snackbar.make(it,"Permission for Camera is required to scan QR",Snackbar.LENGTH_SHORT).show()
            }
        }

        validateBtn.setOnClickListener {
            /**
             * Checks if both the addresses are empty
             */
            if ((btcAddress == "None")&&(ethAddress == "None")){
                Snackbar.make(it,"Cannot validate empty address",Snackbar.LENGTH_SHORT).show()
            }

            /**
             * Checks which one of the addresses(BTC/ETH) is non empty. The non empty address undergoes validation.
             * Both of two addresses cannot be non empty at the same time, because 'btcAddress' is set to None in 'ETH' button's Click listener event and vice versa.
             */

            if (btcAddress != "None"){
                if (Validators.validateBTCAddress(btcAddress.trim())){
                    Snackbar.make(it,"The Scanned BTC Address is valid",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    Snackbar.make(it,"The Scanned BTC Address is invalid",Snackbar.LENGTH_SHORT).show()
                }
            }

            if (ethAddress != "None"){
                if (Validators.validateETHAddress(ethAddress.trim())){
                    Snackbar.make(it,"The Scanned ETH Address is valid",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    Snackbar.make(it,"The Scanned ETH Address is invalid",Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        shareBtn.setOnClickListener {
            /**
             * Checks if both the addresses are empty
             */
            if ((btcAddress == "None")&&(ethAddress == "None")){
                Snackbar.make(it,"Cannot share empty address",Snackbar.LENGTH_SHORT).show()
            }

            /**
             * Checks which one of the addresses(BTC/ETH) is non empty. The non empty address will be shared.
             * Both of two addresses cannot be non empty at the same time, because 'btcAddress' is set to None in 'ETH' button's Click listener event and vice versa.
             */

            if (btcAddress != "None"){
                /**
                 * Checks if the address is valid for sharing.
                 */
                if (Validators.validateBTCAddress(btcAddress.trim())){
                    val messageBody = "Here is a BTC Address I scanned using the Crypto Validate App : $btcAddress"
                    val btcAddressShareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT,messageBody)
                        type = "text/plain"
                    }
                    startActivity(btcAddressShareIntent)
                }
                else{
                    Snackbar.make(it,"Please scan a valid BTC address to be able to share",Snackbar.LENGTH_SHORT).show()
                }
            }
            if (ethAddress != "None"){
                if (Validators.validateETHAddress(ethAddress.trim())){
                    val messageBody = "Here is a ETH Address I scanned using the Crypto Validate App : $ethAddress"
                    val ethAddressShareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT,messageBody)
                        type = "text/plain"
                    }
                    startActivity(ethAddressShareIntent)
                }
                else{
                    Snackbar.make(it,"Please scan a valid ETH address to be able to share",Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Checks for necessary camera permission. If not permitted, then prompts the user, else sets the 'isPermitted' value to be true.
     */
    private fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
        }
        else{
            isPermitted = true
        }
    }

    /**
     * If the user grants permission, the variable 'isPermitted' is set to true else false
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_REQUEST_CODE){
            isPermitted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }
}