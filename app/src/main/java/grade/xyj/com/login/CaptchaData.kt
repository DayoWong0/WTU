package grade.xyj.com.login

import kotlinx.coroutines.channels.Channel

class CaptchaData(
    val bytes: ByteArray,
    val channel: Channel<String>
)