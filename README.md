
### ImageCompress

一行代码实现图片压缩功能，支持单个及多个图片压缩，以File及Bitmap对象返回。




### 添加 `ImageCompress` 到项目

- 第一步： 添加 `JitPack` 到项目的根 `build.gradle` 中


```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- 第二步：添加库依赖


```gradle
dependencies {
    implementation 'com.github.huangziye:ImageCompress:${latest_version}'
}
```



```kotlin
 val config = ImageConfig.getDefaultConfig("/storage/emulated/0/DCIM/Camera/IMG_20190214_092516.jpg")
 Log.e("TAG", File(config.mImagePath).length().toString())
 ImageCompress.get().compress(this, config, object : ImageCompress.OnCompressImageCallback {
     override fun onStartCompress() {

     }

     override fun onCompressSuccess(file: File) {
         Log.e("TAG", file.length().toString())
     }

     override fun onCompressError(errorMsg: String) {
         Log.e("TAG", errorMsg)
     }
 })
```




<br />

### 关于我


- [简书](https://user-gold-cdn.xitu.io/2018/7/26/164d5709442f7342)

- [掘金](https://juejin.im/user/5ad93382518825671547306b)

- [Github](https://github.com/huangziye)

<br />

### License

```
Copyright 2018, huangziye

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```