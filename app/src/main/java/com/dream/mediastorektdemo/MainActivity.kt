package com.dream.mediastorektdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tv_access_file.setOnClickListener {
            val file = FileUtils.internalPersistentStorageForAccess("lvxingxingfile",this)
            Log.e("xxx", "onCreate: file:length = " + file.length())
        }

        tv_add_internal_dir.setOnClickListener {
            Log.i("xxx", "tv_add_internal_dir")
            val output = FileUtils.internalPersistentStorageForWrite("lvxingxingfile", this)
            val content = "this is content 87723"
            output.use {
                it.write(content.toByteArray())
            }
        }

        tv_read_internal_content.setOnClickListener {
            Log.i("xxx", "tv_read_internal_content")
            val input = FileUtils.internalPersistentStorageForRead("lvxingxingfile",this)
            input.bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                   tv_content.text = "$some\n$text"
                    "sad"
                }
            }
        }

        tv_file_list.setOnClickListener {
            for (s in FileUtils.internalPersistentStorageList(this)){
                Log.e("xxx", "onCreate: " + s)
            }

        }
    }
}