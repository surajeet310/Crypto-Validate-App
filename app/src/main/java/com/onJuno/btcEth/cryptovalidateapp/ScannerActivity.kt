package com.onJuno.btcEth.cryptovalidateapp

/**
 * Used a opensource library 'Code scanner' in order to facilitate a QR code scanner
 * Reference Link : https://github.com/yuriy-budiyev/code-scanner
 */

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback

class ScannerActivity : AppCompatActivity() {
    private lateinit var codeScannerView: CodeScannerView
    private lateinit var codeScanner:CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        codeScannerView = findViewById(R.id.scanner_view)
        codeScanner = CodeScanner(this,codeScannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        /**
         * The DecodeCallback provides the data obtained after successful scanning of the QR code.
         * Retrieved data is sent back to the MainActivity.
         */
        codeScanner.decodeCallback = DecodeCallback {
            val intent = Intent().apply {
                putExtra("data",it.text.toString())
            }
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this,"Error occurred in opening camera",Toast.LENGTH_SHORT).show()
            }
        }

        codeScannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }
}