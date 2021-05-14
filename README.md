# lib-cloud-storage

对七牛云对象储存SDK进行简单封装，方便Android端使用

## 引入

[![](https://jitpack.io/v/gittosuperfly/lib-cloud-storage.svg)](https://jitpack.io/#gittosuperfly/lib-cloud-storage)

**Step 1**. 添加JitPack repository到你项目的build.gradle文件

```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2**. 添加库依赖
```groovy
	dependencies {
	    implementation 'com.github.gittosuperfly:lib-cloud-storage:Version'
	}
```


## 使用

**Step 1**. 设置账户信息

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = AccountConfig(
            "AK:xxxxxxxxxxxx",
            "SK:xxxxxxxxxxxx",
            "scope:xxxxxxxxxx",
            "cdn:www.xxxx.com"
        )
        
        CloudStorage.init(config)

    }
}
```

**Step 2**. 在代码中使用上传功能

```kotlin
fun testBtnClick(){
    //参数分别为上传后路径、文件/文件地址/Byte数组、是否分片上传、上传回调
    CloudStorage.upload("test/test.gif", ImageFileUtils.gitTestGifFile(), false, object : UpCallback {
            override fun onCompleted(url: String) {
                Log.d("CloudStorage", "onCompleted: $url")
            }
    
            override fun onFailed(errorCode: Int, errorInfo: String) {
                Log.d("CloudStorage", "errorInfo: $errorInfo")
            }
        })
}
```