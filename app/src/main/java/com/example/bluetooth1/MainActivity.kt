package com.example.bluetooth1

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.bluetooth1.ui.theme.Bluetooth1Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class MainActivity : ComponentActivity() {
    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var takePermission:ActivityResultLauncher<String>
    lateinit var takeResultLauncher: ActivityResultLauncher<Intent>
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothManager=getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter=bluetoothManager.adapter
        takePermission=registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it)
            {
                val intent =Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                takeResultLauncher.launch(intent)

            }else
            {
                Toast.makeText(applicationContext,"Bluetooth Permission is not Granted",Toast.LENGTH_SHORT).show()
            }
        }
        takeResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                    result->
                if (result.resultCode== RESULT_OK){
                    Toast.makeText(applicationContext,"Bluetooth ON",Toast.LENGTH_SHORT).show()
                } else
                {
                    Toast.makeText(applicationContext,"Bluetooth OFF",Toast.LENGTH_SHORT).show()
                }
            })
        setContent { Bluetooth1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting()
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun Greeting() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Bluetooth ON/OFF Application",fontSize=20.sp,
                fontWeight = FontWeight.Bold,textAlign=TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(onClick = {
                takePermission.launch(Manifest.permission.BLUETOOTH_CONNECT)

            }) {
                Text("Bluetooth ON",fontSize=30.sp,
                    fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(onClick = {
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    bluetoothAdapter.disable()
                    Toast.makeText(applicationContext,"Bluetooth Disabled",Toast.LENGTH_SHORT).show()
                }

            }) {
                Text("Bluetooth OFF",fontSize=30.sp,
                    fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(onClick = {
                val data:StringBuffer= StringBuffer()
                val pairedDevices =bluetoothAdapter.bondedDevices
                for (device in pairedDevices)
                {
                    data.append("Device Name="+device.name+"\nDevice Address"+device.address)
                }
                if (data.isEmpty())
                {
                    Toast.makeText(applicationContext,"No Bluetooth Paired Device Found",Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(applicationContext,data,Toast.LENGTH_SHORT).show()
                }

            }) {
                Text("Display Bluetooth Paired Devices",fontSize=30.sp,
                    fontWeight = FontWeight.Bold)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Bluetooth1Theme {
            Greeting()
        }
    }
}