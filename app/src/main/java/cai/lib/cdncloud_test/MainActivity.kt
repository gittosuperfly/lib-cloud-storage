package cai.lib.cdncloud_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import cai.lib.cloudstorage.CloudStorage
import cai.lib.cloudstorage.AccountConfig
import cai.lib.cloudstorage.UpCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val config = AccountConfig(
            "AK:xxxxxxxxxxxx",
            "SK:xxxxxxxxxxxx",
            "scope:xxxxxxxxxx",
            "cdn:www.xxxx.com"
        )

        CloudStorage.init(config)


        findViewById<View>(R.id.test_up).setOnClickListener {
            CloudStorage.upload(
                "test/test.txt",
                "111111123222".toByteArray(),
                false,
                object : UpCallback {
                    override fun onCompleted(url: String) {
                        Log.d("CloudStorage", "onCompleted: $url")
                    }

                    override fun onFailed(errorCode: Int, errorInfo: String) {
                        Log.d("CloudStorage", "errorInfo: $errorInfo")
                    }
                })
        }

    }
}