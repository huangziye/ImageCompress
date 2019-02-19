package com.hzy.compress

import android.graphics.Bitmap
import java.util.*

/**
 * 图片的配置信息类
 * @author: ziye_huang
 * @date: 2019/2/18
 */
class ImageConfig {
    /**
     * 压缩默认宽为720px
     */
    var mDefaultCompressWidth = 720
    /**
     * 压缩默认高为1280px
     */
    var mDefaultCompressHeight = 1280
    var mImagePath: String? = null

    /**
     * 图片格式
     */
    var mImageFormat = Bitmap.CompressFormat.JPEG
    /**
     * 图片质量
     */
    var mImageQuality = Bitmap.Config.ARGB_8888
    /**
     * 默认缓存的目录
     */
    var mCacheImageDir = "cache"
    /**
     * 默认的缓存图片名字
     */
    var mCacheImageName = "${System.currentTimeMillis()}${UUID.randomUUID()}".replace("-", "").trim() + ".jpg"
    /**
     * 调用默认压缩的图片配置属性
     */
    var mCompressSize = CompressPicker.COMPRESS_SIZE

    companion object {
        fun getDefaultConfig(filePath: String): ImageConfig {
            return ImageConfig(filePath)
        }
    }

    constructor()
    private constructor(filePath: String) {
        mImagePath = filePath
    }

}