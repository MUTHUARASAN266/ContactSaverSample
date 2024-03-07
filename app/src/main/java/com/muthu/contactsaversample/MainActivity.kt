package com.muthu.contactsaversample

import android.content.ContentProviderOperation
import android.content.Intent
import android.content.OperationApplicationException
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.muthu.contactsaversample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var CONTACT_CODE = 200
    private lateinit var contactPermission: Array<String>
    private lateinit var contentProviderOperation: ArrayList<ContentProviderOperation>

    companion object {
        var value = 5
    }

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contactPermission = arrayOf(android.Manifest.permission.WRITE_CONTACTS)


        contentProviderOperation = ArrayList()
        Log.e("companion OBJ", "onCreate: $value")
        binding.textView.text = value.toString()

        /* binding.button.setOnClickListener {
             startActivity(Intent(this,NextScreen::class.java))
         }

         binding.apply {
             button.setOnClickListener {

             }
         }*/

        binding.button2.setOnClickListener {
            startActivity(Intent(this, NextScreen::class.java))
        }
        binding.saveBtn.setOnClickListener {

            if (binding.edName.text!!.isEmpty() && binding.edNumber.text!!.isEmpty()) {
                Toast.makeText(this, "isEmpty", Toast.LENGTH_SHORT).show()
            } else {
                if (contactPermissionEnable()) {
                    saveContact()
                    Toast.makeText(this, "permission already granted", Toast.LENGTH_SHORT).show()
                } else {
                    requestContactPermission()
                }
            }
        }


    }

    override fun onRestart() {
        super.onRestart()
        Log.e("companion OBJ ", "onRestart: $value")
    }


    private fun contactPermissionEnable(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestContactPermission() {
        ActivityCompat.requestPermissions(this, contactPermission, CONTACT_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty()) {
            if (requestCode == CONTACT_CODE) {
                val contactPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (contactPermission) {
                    saveContact()
                } else {
                    Toast.makeText(this, "PERMISSION_NOT_GRANTED", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun saveContact() {
        Toast.makeText(this, "saveContact", Toast.LENGTH_SHORT).show()

        contentProviderOperation.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.CONTACT_ID, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        //AddingName
        contentProviderOperation.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(
                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                    binding.edName.text.toString()
                )
                .build()
        )

        //AddingNumber
        contentProviderOperation.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    binding.edNumber.text.toString()
                )
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                )
                .build()
        )
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, contentProviderOperation)
        } catch (e: OperationApplicationException) {
            Log.e(TAG, "saveContact: $e")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAG", "onDestroy: ")
    }
}