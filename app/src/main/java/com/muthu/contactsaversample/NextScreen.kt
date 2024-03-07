package com.muthu.contactsaversample

import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat

class NextScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_screen)

       MainActivity.value=15
        Log.e("NextScreen", "onCreate: ${MainActivity.value}", )

        val permission =android.Manifest.permission.CAMERA
        if (checkPermission(permission)) {
            // Permission already granted
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
            // Do something here
        } else {
            // Request permission
            requestPermission(permission)
        }

    }
    private val PERMISSION_REQUEST_CODE = 123

    // Request permission method
    private fun requestPermission(permission: String) {
        if (shouldShowRequestPermissionRationale(permission)) {
            // Explain why the permission is needed
            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed because...")
                .setPositiveButton("OK") { _, _ ->
                    // Request the permission
                    requestPermissions(arrayOf(permission), PERMISSION_REQUEST_CODE)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            // Request the permission
            requestPermissions(arrayOf(permission), PERMISSION_REQUEST_CODE)
        }
    }

    // Check permission method
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    // Handle permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                // Do something here
                Toast.makeText(this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied
                // Show an explanation or disable the feature
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}