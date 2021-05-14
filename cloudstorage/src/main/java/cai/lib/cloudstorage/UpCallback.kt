package cai.lib.cloudstorage

/**
 * 上传回调类
 */
interface UpCallback {
    /**
     * 完成时
     */
    fun onCompleted(url: String)

    /**
     * 失败时
     */
    fun onFailed(errorCode: Int, errorInfo: String)

    /**
     * 进度追踪
     */
    fun onProgress(percent: Double) {
        //非必须, 默认空实现
    }

    /**
     * 是否中断上传
     *
     * 当返回值为true时，上传终止
     */
    fun isCancel(): Boolean {
        //默认不中断
        return false
    }
}