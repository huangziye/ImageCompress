package com.hzy.imagecompress

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hzy.compress.ImageCompress
import com.hzy.compress.ImageConfig
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val config = ImageConfig.getDefaultConfig("/storage/emulated/0/DCIM/Camera/IMG_20190214_092516.jpg")
        Log.e("TAG", File(config.mImagePath).length().toString())
        ImageCompress.get().compress(this, config, object : ImageCompress.OnCompressImageCallback {
            override fun onStartCompress() {

            }

            override fun onCompressSuccess(file: File) {
                Log.e("TAG", file.length().toString())
            }

            override fun onCompressError(errorMsg: String) {
                Log.e("TAG", errorMsg)
            }
        })
    }
}
