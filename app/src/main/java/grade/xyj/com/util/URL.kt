package grade.xyj.com.util


object URL{
    const val SERVER_ADDRESS = "http://112.74.185.21:8080/myserver/wfh/"

    const val USERAGENT_ANDROIDSCHOOL = "Mozilla/5.0 (Linux; Android 8.0; HUAWEI VNS Build/HUAWEIVNS-TL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36 cpdaily/8.0.3 wisedu/8.0.3"
    const val USERAGENT_IPHONESCHOOL = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16D57  kdtUnion_399b944f792c0c5679 (4416925696) cpdaily/8.0.3  wisedu/8.0.3"
    const val USERAGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36"

    const val LOGIN_API = "https://auth.wtu.edu.cn/authserver/login?service=http%3A%2F%2Fehall.wtu.edu.cn%2Flogin%3Fservice%3Dhttp%3A%2F%2Fehall.wtu.edu.cn%2Fnew%2Findex.html"
    const val LOGIN_HOST = "https://auth.wtu.edu.cn"

    const val COOLMARKET_APK_URL = "https://www.coolapk.com/apk/192247"
    const val CARD_URL = "http://cardwx.wtu.edu.cn:7280/wechat-web/service/profile.html"
    const val SCHOOL_NET_URL = "http://ehall.wtu.edu.cn/appShow?appId=5290300048773319"
    const val GET_PERMISSION_URL = "http://ehall.wtu.edu.cn/appShow?appId=5271578965812781"
    const val XYHY_URL = "http://ehall.wtu.edu.cn/publicapp/sys/xyhy/mobile/xyhy/index.html"
    const val BOYA_URL = "https://wtu.cpdaily.com/wec-amp-boya/mobile/index.html"
    const val CET46_URL = "http://cet.neea.edu.cn/cet/"

    const val COURSE_API = "http://jwglxt.wtu.edu.cn/kbcx/xskbcx_cxXsKb.html?gnmkdm=N253508"

    val GRADES_API get() = "http://jwglxt.wtu.edu.cn/cjcx/cjcx_cxXsKccjList.html?gnmkdm=N305007&su=${Account.cno}"

    val CHECKLOGIN_URL get() = "http://ehall.wtu.edu.cn/jsonp/userDesktopInfo.json?amp_jsonp_callback=jQuery1&_=${System.currentTimeMillis()}"

    fun NEWS_API(id:String) = "http://ehall.wtu.edu.cn/wtu/api/queryBulletinListByConditional.do?columnId=$id&pageNum=1&pageSize=20"

    const val QQ_URL = "mqqwpa://im/chat?chat_type=wpa&uin=58296741&version=1"
}