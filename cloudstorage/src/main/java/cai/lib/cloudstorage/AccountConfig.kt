package cai.lib.cloudstorage

data class AccountConfig(
    val accessKey: String,
    val secretKey: String,
    val scope: String,
    val cdnHost:String
)