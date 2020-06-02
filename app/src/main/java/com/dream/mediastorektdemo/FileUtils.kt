package com.dream.mediastorektdemo

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getExternalFilesDirs
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class FileUtils {


    /**
     * type 可选项目如下
     *            {@link android.os.Environment#DIRECTORY_MUSIC},
     *            {@link android.os.Environment#DIRECTORY_PODCASTS},
     *            {@link android.os.Environment#DIRECTORY_RINGTONES},
     *            {@link android.os.Environment#DIRECTORY_ALARMS},
     *            {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *            {@link android.os.Environment#DIRECTORY_PICTURES},
     *            {@link android.os.Environment#DIRECTORY_MOVIES}.
     *
     *  这个文件存储应用私有文件，其他app无法访问，具有很高的安全性。
     *  注意：这个地方不能存储太大的东西。
     *  如果想让别的应用获取这个地方的文件，得用FileProvider
     */
    companion object{

        /**
         * 根据filename获取到一个文件
         */
        fun internalPersistentStorageForAccess(filename : String,context : Context) : File{
            return File(context.filesDir,filename)
        }

        /**
         * 根据filename写文件
         */
        fun internalPersistentStorageForWrite(filename : String,context : Context) : OutputStream{
            return context.openFileOutput(filename, Context.MODE_PRIVATE)
        }

        /**
         * 根据filename读文件
         */
        fun internalPersistentStorageForRead(filename : String,context : Context) : InputStream{
            return context.openFileInput(filename)
        }

        /**
         * 获取所有文件的name
         */
        fun internalPersistentStorageList(context : Context) : Array<String>{
            return context.fileList()
        }


    }


}