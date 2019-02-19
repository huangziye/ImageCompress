package com.hzy.compress

import android.content.Context
import android.graphics.Bitmap
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.io.File


/**
 *
 * @author: ziye_huang
 * @date: 2019/2/18
 */
class ImageCompress {

    /**
     * 是否正在压缩
     */
    private var mIsCompressing = false

    private constructor()

    companion object {
        private var mImageCompress: ImageCompress? = null
        fun get(): ImageCompress {
            synchronized(ImageCompress::class.java) {
                if (null == mImageCompress) {
                    synchronized(ImageCompress::class.java) {
                        mImageCompress = ImageCompress()
                    }
                }
            }
            return mImageCompress!!
        }
    }

    /**
     * 压缩图片
     */
    fun compress(imageConfig: ImageConfig, callback: OnCompressBitmapCallback) {
        callback.onStartCompress()
        Observable.create(ObservableOnSubscribe<ImageConfig> { e -> e.onNext(imageConfig) })
            .map(Function<ImageConfig, Bitmap> { imageConfig -> CompressPicker.compress(imageConfig) })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<Bitmap> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(bitmap: Bitmap) {
                    if (bitmap != null && bitmap.height > 0 && bitmap.width > 0) {
                        callback.onCompressSuccess(bitmap)
                    } else {
                        callback.onCompressError("图片压缩失败")
                    }
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }
            })
    }

    fun compress(context: Context, imageConfig: ImageConfig, callback: OnCompressImageCallback) {
        callback.onStartCompress()
        Observable.create(ObservableOnSubscribe<ImageConfig> { e ->
            e.onNext(imageConfig)
            mIsCompressing = true
        }).map(Function<ImageConfig, File> { imageConfig ->
            val bitmap = CompressPicker.compress(imageConfig)
            bitmap?.let {
                CompressPicker.saveBitmapToFile(context, bitmap)
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<File> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(file: File) {
                    mIsCompressing = false
                    if (file != null) {
                        callback.onCompressSuccess(file)
                    } else {
                        callback.onCompressError("图片压缩失败")
                    }
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                    mIsCompressing = false
                }
            })
    }

    /**
     * 压缩图片集合
     */
    fun compress(context: Context, imageConfigList: List<ImageConfig>, callback: OnCompressImageListCallback) {
        if (imageConfigList.isEmpty()) return
        callback.onStartCompress()
        Observable.fromIterable(imageConfigList)
            .map(Function<ImageConfig, File> { imageConfig ->
                mIsCompressing = true
                CompressPicker.saveBitmapToFile(context, CompressPicker.compress(imageConfig)!!)
            })
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<File>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onSuccess(files: List<File>) {
                    mIsCompressing = false
                    if (files.isNotEmpty()) {
                        callback.onCompressSuccess(files)
                    } else {
                        callback.onCompressError("图片压缩失败")
                    }
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    /**
     * 当前是否正在压缩
     */
    fun isCompressing(): Boolean {
        return mIsCompressing
    }

    interface OnCompressImageListCallback {
        fun onStartCompress()
        fun onCompressSuccess(fileList: List<File>)
        fun onCompressError(errorMsg: String)
    }

    interface OnCompressImageCallback {
        fun onStartCompress()
        fun onCompressSuccess(file: File)
        fun onCompressError(errorMsg: String)
    }

    interface OnCompressBitmapCallback {
        fun onStartCompress()
        fun onCompressSuccess(bitmap: Bitmap)
        fun onCompressError(errorMsg: String)
    }

}