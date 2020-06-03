package com.dream.mediastorektdemo

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()

        tv_access_file.setOnClickListener {
            val file = FileUtils.internalPersistentStorageForAccess("lvxingxingfile", this)
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
            val input = FileUtils.internalPersistentStorageForRead("lvxingxingfile", this)
            input.bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    tv_content.text = "$some\n$text"
                    "sad"
                }
            }
        }

        tv_file_list.setOnClickListener {
            for (s in FileUtils.internalPersistentStorageList(this)) {
                Log.e("xxx", "onCreate: " + s)
            }
        }

        tv_access_external_file.setOnClickListener {
            val file = FileUtils.externalPersistentStorageForAccess(
                "externalfile1",
                Environment.DIRECTORY_DCIM,
                this
            )
            val output = file.outputStream()
            output.use {
                it.write("this is the content for external file1".toByteArray())
            }
        }

        tv_access_external_content.setOnClickListener {
            val file = FileUtils.externalPersistentStorageForAccess(
                "externalfile1",
                Environment.DIRECTORY_DCIM,
                this
            )
            val inputStream = file.inputStream()
            inputStream.bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    tv_content.text = "$some\n$text"
                    "22"
                }
            }
        }



        tv_add_file_media.setOnClickListener {
            val newSongDetails = ContentValues().apply {
                put(MediaStore.Audio.Media.DISPLAY_NAME, "XX6Song.mp3")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MUSIC)
            }
            songuri = FileUtils.addMediaFile(
                newSongDetails,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                this
            )
            Log.e("xxx", "songuri = ${songuri}")
        }

        tv_write_to_file.setOnClickListener {
            songuri?.let { it1 ->
                contentResolver.openOutputStream(it1).use {
                    it?.write("hahahah".toByteArray())
                }
            }
        }

        tv_access_media_audio_names.setOnClickListener {
            thread {
                val cursor = FileUtils.mediaStorageForAccess(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    null,
                    this
                )
                cursor?.let {
                    Log.e("xxx", "count is ${cursor.count}")
                    while (it.moveToNext()) {
                        val id =
                            it.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                        Log.e("xxx", "id is $id ")
                    }
                }
            }
        }
    }

    fun requestPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), 0x22
        )
    }

    var songuri: Uri? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            0x22 -> {

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}