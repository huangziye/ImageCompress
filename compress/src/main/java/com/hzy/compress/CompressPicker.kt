package com.hzy.compress

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 *
 * @author: ziye_huang
 * @date: 2019/2/18
 */
class CompressPicker {
    companion object {
        /**
         * 压缩图片最大容量
         */
        val COMPRESS_SIZE = 150
        val BYTE_MONAD = 1024
        private var mImageConfig: ImageConfig? = null

        /**
         * 压缩图片
         */
        fun compress(imageConfig: ImageConfig): Bitmap? {
            mImageConfig = imageConfig
            val options = BitmapFactory.Options()
            options.inPreferredConfig = imageConfig.mImageQuality
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imageConfig.mImagePath, options)
            options.inSampleSize =
                (((options.outWidth * 1.0f) / (imageConfig.mDefaultCompressWidth * 1.0f) + (options.outHeight * 1.0f) / (imageConfig.mDefaultCompressHeight * 1.0f)) / 2).toInt()
            options.inJustDecodeBounds = false
            options.inScaled = false
            options.inMutable = true
            var bitmap = BitmapFactory.decodeFile(imageConfig.mImagePath, options)
            try {
                val exif = ExifInterface(imageConfig.mImagePath)
                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
                val matrix = Matrix()
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                return bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        /**
         * 保存bitmap到文件
         */
        fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {
            if (null == mImageConfig) {
                mImageConfig = ImageConfig()
            }
            var fos: FileOutputStream? = null
            val bos = ByteArrayOutputStream()
            var quality = 100
            bitmap.compress(mImageConfig!!.mImageFormat, quality, bos)
            while (bos.toByteArray().size / CompressPicker.BYTE_MONAD > mImageConfig!!.mCompressSize) {
                bos.reset()
                quality -= 5
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos)
            }
            var file = File(mImageConfig!!.mCacheImageDir)
            file = FileUtil.createDirectory(context, file.absolutePath)
            val imageFile = File(file.absoluteFile, mImageConfig!!.mCacheImageName)
            try {
                fos = FileOutputStream(imageFile)
                fos.write(bos.toByteArray(), 0, bos.toByteArray().size)
                fos.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    bos.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    fos?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return imageFile
        }
    }

}