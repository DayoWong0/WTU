package grade.xyj.com.component.login

import android.animation.IntEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.KeyboardUtils
import com.bumptech.glide.Glide
import grade.xyj.com.R
import grade.xyj.com.component.main.MainActivity
import grade.xyj.com.login.LoginStatus
import grade.xyj.com.update.UpdateBaseActivity
import grade.xyj.com.util.Account
import grade.xyj.com.util.extend.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult

class LoginActivity : UpdateBaseActivity() {
    private var loginBtnOldWidth = 0
    private var loginBtnOldHeight = 0
    private var loginBtnOldRadius = 0f
    private var currentStatus = 0
    private var channel: Channel<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setWindow()
        autoStatusBarModel()
        super.onCreate(savedInstanceState)

        val mFont = Typeface.createFromAsset(assets, "fonts/iconfont.ttf")
        val viewModel = getViewModel<LoginViewModel>()
        setContentView(R.layout.activity_login)
        //网页登陆
        web_login_button.setOnClickListener {
            startActivityForResult<LoginWebActivity>(888)
        }
        //设置密码隐藏
        cb_check_pwd.runNoResult {
            typeface = mFont
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    cb_check_pwd.text = "\uE6E8"
                    et_pwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    et_pwd.setSelection(et_pwd.text.length)
                } else {
                    cb_check_pwd.text = "\uE6A9"
                    et_pwd.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    et_pwd.setSelection(et_pwd.text.length)
                }
            }
        }

        cv_login.setOnClickListener {
            val shake = AnimationUtils.loadAnimation(this, R.anim.edittext_shake)
            if (currentStatus == 0) {
                when {
                    et_id.text.isEmpty() -> et_id.startAnimation(shake)
                    et_pwd.text.isEmpty() -> et_pwd.startAnimation(shake)
                    else -> {
                        cardRe2C()
                        viewModel.login(et_id.text.toString(), et_pwd.text.toString())
                    }
                }
            } else {
                val code = et_captcha.text.toString().trim()
                if (code.length != 4) {
                    et_captcha.startAnimation(shake)
                } else if (channel != null) {
                    cardRe2C()
                    viewModel.submitCaptcha(code, channel!!)
                }
            }
        }
        viewModel.loginStatus.observe(this, Observer {
            when (it) {
                LoginStatus.NET_ERROR -> {
                    currentStatus = 0
                    ll_captcha.gone()
                    cardC2Re("网络错误")
                }
                LoginStatus.USER_OR_PWD_ERROR -> {
                    currentStatus = 0
                    ll_captcha.gone()
                    cardC2Re("账号或密码错误")
                }
                LoginStatus.SUCCESS -> {
                    showSuccess()
                    Account.cno = viewModel.userName
                    Account.passWord = viewModel.passWord
                    startActivity<MainActivity>()
                    finish()
                }
                LoginStatus.WF_NET_ERROR -> {
                    currentStatus = 0
                    ll_captcha.gone()
                    MaterialDialog(this).show {
                        title(text = "错误")
                        message(text = "密码加密错误,请等待作者修复\n或加群反馈:315170003")
                        positiveButton {

                        }
                    }
                }
            }
        })
        viewModel.captchaLiveData.observe(this, Observer { captchaData ->
            et_captcha.setText("")
            KeyboardUtils.showSoftInput(et_captcha)
            cardC2Re("请输入验证码")
            ll_captcha.visible()
            Glide.with(this)
                .asDrawable()
                .load(captchaData.bytes)
                .into(iv_captcha)

            currentStatus = 1
            channel = captchaData.channel
        })


    }

    private fun fadeAnimation(target: View, start: Float, end: Float, duration: Long, delay: Long) {
        ObjectAnimator.ofFloat(target, "alpha", start, end).run {
            this.duration = duration
            startDelay = delay
            start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 888) {
            if (resultCode == 888) {
                Account.useWeb = true
                startActivity<MainActivity>()
                finish()
            } else {
                toastError("网页登陆失败")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun cardRe2C() {
        loginBtnOldWidth = cv_login.width
        loginBtnOldHeight = cv_login.height
        loginBtnOldRadius = cv_login.radius
        widthAnimation(cv_login, loginBtnOldWidth, cv_login.height, 300, 0)
        fadeAnimation(btn_text, 1f, 0f, 200, 0)
        fadeAnimation(cpb, 0f, 1f, 100, 0)
        cv_login.isClickable = false
    }

    private fun widthAnimation(target: View, start: Int, end: Int, duration: Int, delay: Long) {
        val valueAnimator = ValueAnimator.ofInt(1, 100)
        valueAnimator.addUpdateListener { animation ->
            val intEvaluator = IntEvaluator()
            val fraction = animation.animatedFraction
            target.layoutParams.width = intEvaluator.evaluate(fraction, start, end)!!
            target.requestLayout()
        }
        valueAnimator.startDelay = delay
        valueAnimator.setDuration(duration.toLong()).start()
    }

    private fun cardC2Re(msg: String) {
        cv_login.isClickable = false
        widthAnimation(cv_login, cv_login.height, loginBtnOldWidth, 300, 0)
        fadeAnimation(cpb, 1f, 0f, 100, 0)
        btn_text.text = msg
        fadeAnimation(btn_text, 0f, 1f, 200, 0)
        launchWithLifecycle {
            delay(1000)
            btn_text.text = "登录"
            cv_login.isClickable = true
        }
    }

    private fun showSuccess() {
        cv_login.isClickable = false
        widthAnimation(cv_login, cv_login.height, loginBtnOldWidth, 300, 0)
        fadeAnimation(cpb, 1f, 0f, 100, 0)
        btn_text.text = "登陆成功"
        fadeAnimation(btn_text, 0f, 1f, 200, 0)
    }
}