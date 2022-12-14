package com.example.acssample

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.azure.android.communication.common.CommunicationTokenCredential
import com.azure.android.communication.common.CommunicationTokenRefreshOptions
import com.azure.android.communication.ui.calling.CallComposite
import com.azure.android.communication.ui.calling.CallCompositeBuilder
import com.azure.android.communication.ui.calling.models.CallCompositeGroupCallLocator
import com.azure.android.communication.ui.calling.models.CallCompositeJoinLocator
import com.azure.android.communication.ui.calling.models.CallCompositeRemoteOptions
import com.example.acssample.ui.theme.ACSSampleTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ACSSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JoiningAcsScreen()
                }
            }
        }
    }
}

class MyAppState {

    fun join(context: Context) {
        val refreshOptions = CommunicationTokenRefreshOptions({ "accesstoken" }, true)
        val credential = CommunicationTokenCredential(refreshOptions)
        val locator: CallCompositeJoinLocator = CallCompositeGroupCallLocator(UUID.fromString("meeting guid"))
        val remoteOptions = CallCompositeRemoteOptions(locator, credential)

        val callComposite: CallComposite = CallCompositeBuilder().build()
        callComposite.launch(context, remoteOptions)
        callComposite.addOnErrorEventHandler {
            println(it.errorCode)
        }
    }
}

@Composable
fun JoiningAcsScreen() {
    val state by remember {
        mutableStateOf(MyAppState())
    }
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            state.join(context)
        }) {
            Text(text = "会議に参加")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ACSSampleTheme {
        JoiningAcsScreen()
    }
}