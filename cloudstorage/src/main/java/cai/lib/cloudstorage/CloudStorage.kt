package cai.lib.cloudstorage

import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UpCancellationSignal
import com.qiniu.android.storage.UpCompletionHandler
import com.qiniu.android.storage.UploadManager
import com.qiniu.android.storage.UploadOptions
import java.io.File

object CloudStorage {

    private lateinit var config: AccountConfig
    private val uploadManager: UploadManager by lazy { UploadManager() }
    private val segmentUploadManager: UploadManager by lazy { UploadManager(segmentConfig) }
    private var segmentConfig: Configuration = Configuration.Builder()
        .resumeUploadVersion(Configuration.RESUME_UPLOAD_VERSION_V2)
        .useConcurrentResumeUpload(true)
        .concurrentTaskCount(3)
        .build()

    fun init(config: AccountConfig) {
        this.config = config
    }

    fun upload(
        key: String,
        value: File,
        isSegment: Boolean,
        callback: UpCallback
    ) {
        val manager = if (isSegment) segmentUploadManager else uploadManager
        manager.put(
            value, key, TokenUtils.getToken(config), handlerResult(callback),
            handlerRuntime(callback)
        )
    }

    fun upload(
        key: String,
        value: ByteArray,
        isSegment: Boolean,
        callback: UpCallback
    ) {
        val manager = if (isSegment) segmentUploadManager else uploadManager
        manager.put(
            value, key, TokenUtils.getToken(config), handlerResult(callback),
            handlerRuntime(callback)
        )
    }

    fun upload(
        key: String,
        value: String,
        isSegment: Boolean,
        callback: UpCallback
    ) {
        val manager = if (isSegment) segmentUploadManager else uploadManager
        manager.put(
            value, key, TokenUtils.getToken(config), handlerResult(callback),
            handlerRuntime(callback)
        )
    }

    //处理结果
    private fun handlerResult(callback: UpCallback): UpCompletionHandler {
        return UpCompletionHandler { key, info, _ ->
            if (info.isOK) {
                callback.onCompleted("http://" + config.cdnHost + "/" + key)
            } else {
                callback.onFailed(info.statusCode, info.error)
            }
        }
    }

    //处理传输过程中
    private fun handlerRuntime(callback: UpCallback): UploadOptions {
        return UploadOptions(null, null, false,
            { _, percent ->
                callback.onProgress(percent)
            }, UpCancellationSignal {
                return@UpCancellationSignal callback.isCancel()
            })
    }


}