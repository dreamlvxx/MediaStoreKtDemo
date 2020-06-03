package com.dream.mediastorektdemo

import android.content.*
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.os.storage.StorageManager.ACTION_MANAGE_STORAGE
import android.provider.MediaStore
import android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY
import android.util.Size
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getExternalFilesDirs
import java.io.*
import java.util.*

/**
 * 总述：
 * 大致分为三块：
 * APP-SEPCIAL STORAGE
 * SHARED STORAGE
 * OTHER DOCUMENT FILE
 */
class FileUtils {


    /**
     *  这个文件存储应用私有文件，其他app无法访问，具有很高的安全性。
     *  注意：这个地方不能存储太大的东西。
     *  如果想让别的应用获取这个地方的文件，得用FileProvider
     */
    companion object {

        //************************************************************************************************************************
        //*********************************************APP-SPECIAL STORAGE********************************************************

        /**
         * 根据filename获取到一个文件
         */
        fun internalPersistentStorageForAccess(filename: String, context: Context): File {
            return File(context.filesDir, filename)
        }

        /**
         * 根据filename写文件
         */
        fun internalPersistentStorageForWrite(filename: String, context: Context): OutputStream {
            return context.openFileOutput(filename, Context.MODE_PRIVATE)
        }

        /**
         * 根据filename读文件
         */
        fun internalPersistentStorageForRead(filename: String, context: Context): InputStream {
            return context.openFileInput(filename)
        }

        /**
         * 获取所有文件的name
         */
        fun internalPersistentStorageList(context: Context): Array<String> {
            return context.fileList()
        }

        /**
         * 内部存储创建临时文件
         */
        fun internalTempStorageForCreate(filename: String, context: Context) {
            File.createTempFile(filename, null, context.cacheDir)
        }

        /**
         * 根据filename获取一个file
         */
        fun internalTempStorageForAccess(filename: String, context: Context): File {
            return File(context.cacheDir, filename)
        }

        /**
         * 根据filename删除一个file
         */
        fun interenalTempStorageForDel(filename: String, context: Context) {
            context.deleteFile(filename)
        }

        /**
         * 检查外部存储是否可 读 写
         */
        fun isExternalStorageWritable(): Boolean {
            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        }

        /**
         * 检查外部存储是否可 读
         */
        fun isExternalStorageReadable(): Boolean {
            return Environment.getExternalStorageState() in
                    setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
        }

        /**
         * 选择一个主要得外部存储卷 (一般默认第一个是主要的)
         */
        fun externalStoragePhysicalLocaltion(context: Context): File {
            val externalStorageVolumes: Array<out File> =
                ContextCompat.getExternalFilesDirs(context, null)
            return externalStorageVolumes[0]
        }


        /**
         *type 可选项目如下
         *        {@link android.os.Environment#DIRECTORY_MUSIC},
         *        {@link android.os.Environment#DIRECTORY_PODCASTS},
         *        {@link android.os.Environment#DIRECTORY_RINGTONES},
         *        {@link android.os.Environment#DIRECTORY_ALARMS},
         *        {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
         *        {@link android.os.Environment#DIRECTORY_PICTURES},
         *        {@link android.os.Environment#DIRECTORY_MOVIES}.
         *        {@link android.os.Environment#DIRECTORY_DOWNLOADS}.
         *        {@link android.os.Environment#DIRECTORY_DCIM}.
         *        {@link android.os.Environment#DIRECTORY_DOCUMENTS}.
         *        {@link android.os.Environment#DIRECTORY_SCREENSHOTS}.
         *        {@link android.os.Environment#DIRECTORY_AUDIOBOOKS}.
         *
         *
         *  可以获取到一个filename的文件
         *  如果没有会自动创建
         *
         */
        fun externalPersistentStorageForAccess(
            filename: String,
            type: String,
            context: Context
        ): File {
            return File(context.getExternalFilesDir(type), filename)
        }


        /**
         * 获取一个external缓存file
         * 没有会自动创建
         */
        fun externalCacheStorageForAccess(filename: String, type: String, context: Context): File {
            return File(context.externalCacheDir, filename)
        }


        /**
         * 判断内存可用情况(未补充)
         */
//        fun getM(context: Context, filename: String) {
//            // App needs 10 MB within internal storage.
//            val NUM_BYTES_NEEDED_FOR_MY_APP = 1024 * 1024 * 10L
//
//            val storageManager = context.applicationContext.getSystemService<StorageManager>("")!!
//            val appSpecificInternalDirUuid: UUID = storageManager.getUuidForPath(filesDir)
//            val availableBytes: Long =
//                storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
//            if (availableBytes >= NUM_BYTES_NEEDED_FOR_MY_APP) {
//                storageManager.allocateBytes(
//                    appSpecificInternalDirUuid, NUM_BYTES_NEEDED_FOR_MY_APP
//                )
//            } else {
//                val storageIntent = Intent().apply {
//                    action = ACTION_MANAGE_STORAGE
//                }
//                // Display prompt to user, requesting that they choose files to remove.
//            }
//        }


        //**************************************************************************************************************************
        //*************************************************SHARED STORAGE***********************************************************


        /**
         * 查找某个媒体库的文件
         * 可用媒体库参考如下(每个媒体库分为三种类型：Internal External 可移动存储)：
         *
         * AUDIO:
         * MediaStore.Audio.Media.INTERNAL_CONTENT_URI   ==>content://media/internal/audio/media
         * MediaStore.Audio.Media.EXTERNAL_CONTENT_URI   ==>content://media/external/audio/media
         * MediaStore.Audio.Media.getContentUri()        ==>content://media//audio/media
         *
         * VIDEO:
         * MediaStore.Video.Media.INTERNAL_CONTENT_URI   ==>content://media/internal/video/media
         * MediaStore.Video.Media.EXTERNAL_CONTENT_URI   ==>content://media/external/video/media
         * MediaStore.Video.Media.getContentUri()        ==>content://media//video/media
         *
         * IMAGE:
         * MediaStore.Images.Media.INTERNAL_CONTENT_URI   ==>content://media/internal/images/media
         * MediaStore.Images.Media.EXTERNAL_CONTENT_URI   ==>content://media/external/images/media
         * MediaStore.Images.Media.getContentUri()        ==>content://media//images/media
         *
         * DOWNLOADS:
         * MediaStore.Downloads.INTERNAL_CONTENT_URI   ==>content://media/internal/downloads
         * MediaStore.Downloads.EXTERNAL_CONTENT_URI   ==>content://media/external/downloads
         * MediaStore.Downloads.getContentUri()        ==>content://media//downloads
         *
         * FILE:
         * MediaStore.Files.Media.getContentUri()  ==>content://media//file
         *
         */
        fun mediaStorageForAccess(
            mediaTypeUri: Uri,
            projection: Array<String>?,
            selection: String?,
            selectionArgs: Array<String>?,
            sortOrder: String?,
            context: Context
        ): Cursor? {
            val resolver = context.contentResolver
            return resolver.query(mediaTypeUri, projection, selection, selectionArgs, sortOrder)
        }

        /**
         * 添加一个文件到媒体库
         */
        fun addMediaFile(values: ContentValues, mediaStorageUri: Uri, context: Context): Uri? {
            val resolver = context.contentResolver
            return resolver.insert(mediaStorageUri, values)
        }

        fun updateMediaFile(values : ContentValues){

        }

        /**
         * 获取外部存储卷的名称
         * 主要的卷名称为：VOLUME_EXTERNAL_PRIMARY
         */
        @RequiresApi(Build.VERSION_CODES.Q)
        fun selectExternalVolumeList(context: Context): Set<String> {
            return MediaStore.getExternalVolumeNames(context)
        }

        /**
         * 根据一个uri来打开缩略图信息
         */
        @RequiresApi(Build.VERSION_CODES.Q)
        fun loadThumbnails(uri: Uri, context: Context) {
            val thumb: Bitmap = context.contentResolver.loadThumbnail(uri, Size(640, 480), null)
        }

        /**
         * 通过PFD打开一个媒体文件
         */
        fun openMediaFileByPfd(uri: Uri, context: Context) {
            val readOnlyMode = "r"
            context.contentResolver.openFileDescriptor(uri, readOnlyMode).use { pfd ->
                {

                }
            }
        }

        /**
         * 通过inputstream打开一个文件
         */
        fun openMediaFileByStream(uri: Uri, context: Context) {
            context.contentResolver.openInputStream(uri).use { stream ->
                {

                }
            }
        }

        /**
         * 获取相册中的图片
         */
        fun accessPictureForMediaStore(context: Context) {
            val resolver = context.contentResolver
            val cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                "${MediaStore.MediaColumns.DATE_ADDED} desc"
            )
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    val uri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    println("image uri is $uri")
                }
                cursor.close()
            }
        }

        /**
         * 将一个uri对应的图片设置到ImageView
         */
        fun setPictureforImageView(context: Context, uri: Uri, imageView: ImageView) {
            val resolver = context.contentResolver
            val fd = resolver.openFileDescriptor(uri, "r")
            if (fd != null) {
                val bitmap = BitmapFactory.decodeFileDescriptor(fd.fileDescriptor)
                fd.close()
                imageView.setImageBitmap(bitmap)
            }
        }

        /**
         * 添加图片到相册
         */
        fun addBitmapToAlbum(
            context: Context,
            bitmap: Bitmap,
            displayName: String,
            mimeType: String,
            compressFormat: Bitmap.CompressFormat
        ) {
            val resolver = context.contentResolver
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            } else {
                values.put(
                    MediaStore.MediaColumns.DATA,
                    "${Environment.getExternalStorageDirectory().path}/${Environment.DIRECTORY_DCIM}/$displayName"
                )
            }
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                val outputStream = resolver.openOutputStream(uri)
                if (outputStream != null) {
                    bitmap.compress(compressFormat, 100, outputStream)
                    outputStream.close()
                }
            }
        }

        /**
         * 将一张网络图片或者私有目录的图片通过获取流然后写入
         */
        fun writeInputStreamToAlbum(
            context: Context,
            inputStream: InputStream,
            displayName: String,
            mimeType: String
        ) {
            val resolver = context.contentResolver
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            } else {
                values.put(
                    MediaStore.MediaColumns.DATA,
                    "${Environment.getExternalStorageDirectory().path}/${Environment.DIRECTORY_DCIM}/$displayName"
                )
            }
            val bis = BufferedInputStream(inputStream)
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                val outputStream = resolver.openOutputStream(uri)
                if (outputStream != null) {
                    val bos = BufferedOutputStream(outputStream)
                    val buffer = ByteArray(1024)
                    var bytes = bis.read(buffer)
                    while (bytes >= 0) {
                        bos.write(buffer, 0, bytes)
                        bos.flush()
                        bytes = bis.read(buffer)
                    }
                    bos.close()
                }
            }
            bis.close()
        }

    }


    /**
     * 当你访问的文件可能具有耗时操作的时候，
     * 那么可以设置标志IS_PENDING为1，此时只有你能访问这个文件。直到这个标志变成0
     */
    fun accessFileForOnly(context: Context) {
        // Add a media item that other apps shouldn't see until the item is
// fully written to the media store.
        val resolver = context.contentResolver

// Find all audio files on the primary external storage device.
// On API <= 28, use VOLUME_EXTERNAL instead.
        val audioCollection = MediaStore.Audio.Media
            .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val songDetails = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, "My Workout Playlist.mp3")
            put(MediaStore.Audio.Media.IS_PENDING, 1)
        }

        val songContentUri = resolver.insert(audioCollection, songDetails)
        songContentUri?.let {
            resolver.openFileDescriptor(songContentUri, "w", null).use { pfd ->
                // Write data into the pending audio file.
            }

// Now that we're finished, release the "pending" status, and allow other apps
// to play the audio track.
            songDetails.clear()
            songDetails.put(MediaStore.Audio.Media.IS_PENDING, 0)
            resolver.update(songContentUri, songDetails, null, null)
        }

    }

}