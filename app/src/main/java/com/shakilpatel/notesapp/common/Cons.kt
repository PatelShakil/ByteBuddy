package com.shakilpatel.notesapp.common

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random


class Cons() {
    companion object {
        const val USERS = "users"
        fun isInternetConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

            // Check if there is a network connection
            return networkInfo != null && networkInfo.isConnected
        }

        fun getPermissions(): Array<String> {
            return if (Build.VERSION.SDK_INT >= 33) {
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } else {
                arrayOf(
                )
            }
        }

        fun requestPermissionsIfNecessary(activity: Activity) {
            val permissions = getPermissions()

            val notGrantedPermissions = permissions.filter {
                ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
            }.toTypedArray()

            if (notGrantedPermissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(activity, notGrantedPermissions, 1)
            }
        }

        fun gotoUrl(s: String, context: Context) {
            Log.d("URI", s)
            val uri = Uri.parse(s)
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        fun getCurrentVersionCode(context: Context): Int {
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", e.localizedMessage)
            }
            return packageInfo!!.versionCode
        }
        fun getCurrentVersionName(context: Context): String {
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", e.localizedMessage)
            }
            return packageInfo!!.versionName
        }

        fun encodeImage(bitmap: Bitmap): String {
            var pwidth = bitmap.width
            var pheight = bitmap.height
            var pbitmap = Bitmap.createScaledBitmap(bitmap, pwidth, pheight, false)
            var byteArrayOutputStream = ByteArrayOutputStream()
            pbitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
            var bytes = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }

        fun decodeImage(postimg: String): Bitmap {
            var bytes = Base64.decode(postimg, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        fun createBitmapFromUri(context: Context, uri: Uri): Bitmap? {
            var bitmap: Bitmap? = null
            var inputStream: InputStream? = null

            try {
                val contentResolver: ContentResolver = context.contentResolver
                inputStream = contentResolver.openInputStream(uri)
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                inputStream?.close()
            }

            return bitmap
        }

        fun generateRandomValue(char: Int): String {
            val characterSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            val random = Random(System.currentTimeMillis())
            val randomValue = StringBuilder()

            repeat(char) {
                val randomIndex = random.nextInt(characterSet.length)
                randomValue.append(characterSet[randomIndex])
            }

            return randomValue.toString()
        }

        fun convertLongToDate(timestamp: Long, format: String): String {
            val date = Date(timestamp)
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.format(date)
        }

        fun createBitmapFromDrawableResource(context: Context, drawableResId: Int): Bitmap? {
            return BitmapFactory.decodeResource(context.resources, drawableResId)
        }

        fun loadImageBitmap(uri: Uri, contentResolver: ContentResolver): ImageBitmap? {
            return try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                bitmap.asImageBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}