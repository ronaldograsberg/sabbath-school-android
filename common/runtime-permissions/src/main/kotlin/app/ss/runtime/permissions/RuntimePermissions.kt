/*
 * Copyright (c) 2022. Adventech <info@adventech.io>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package app.ss.runtime.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import app.ss.runtime.permissions.RuntimePermissions.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface RuntimePermissions {
    fun isGranted(permission: String): Boolean
    fun isGranted(activity: ComponentActivity, permission: String): Result
    fun request(activity: ComponentActivity, permission: String, listener: Listener)

    interface Listener {
        fun onPermissionGranted()
        fun onPermissionDenied()
    }

    enum class Result {
        GRANTED, SHOW_RATIONALE, DENIED
    }
}

internal class RuntimePermissionsImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : RuntimePermissions {

    override fun isGranted(permission: String): Boolean = ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED

    override fun isGranted(
        activity: ComponentActivity,
        permission: String
    ): Result = when {
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED -> Result.GRANTED
        activity.shouldShowRequestPermissionRationale(permission) -> Result.SHOW_RATIONALE
        else -> Result.DENIED
    }

    override fun request(
        activity: ComponentActivity,
        permission: String,
        listener: RuntimePermissions.Listener
    ) {
        val requestPermissionLauncher =
            activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    listener.onPermissionGranted()
                } else {
                    listener.onPermissionDenied()
                }
            }
        requestPermissionLauncher.launch(permission)
    }
}
