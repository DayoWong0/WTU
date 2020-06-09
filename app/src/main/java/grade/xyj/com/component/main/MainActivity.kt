package grade.xyj.com.component.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.jaeger.library.StatusBarUtil
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import grade.xyj.com.R
import grade.xyj.com.component.AboutActivity
import grade.xyj.com.component.WebActivity
import grade.xyj.com.component.course.CourseFragment
import grade.xyj.com.component.grade.GradeBaseFragment
import grade.xyj.com.component.login.LoginActivity
import grade.xyj.com.component.login.LoginWebActivity
import grade.xyj.com.component.news.NewsMainBaseFragment
import grade.xyj.com.component.qbank.WorkActivity
import grade.xyj.com.component.setting.SettingActivity
import grade.xyj.com.login.LoginStatus
import grade.xyj.com.update.UpdateBaseActivity
import grade.xyj.com.util.*
import grade.xyj.com.util.URL.BOYA_URL
import grade.xyj.com.util.URL.CARD_URL
import grade.xyj.com.util.URL.CET46_URL
import grade.xyj.com.util.URL.GET_PERMISSION_URL
import grade.xyj.com.util.URL.SCHOOL_NET_URL
import grade.xyj.com.util.URL.XYHY_URL
import grade.xyj.com.util.extend.*
import org.jetbrains.anko.*

class MainActivity : UpdateBaseActivity() {

    sealed class FragmentType {
        object COURSE : FragmentType()
        object GRADE : FragmentType()
        object NEWS : FragmentType()

        fun getFragment(): Fragment = when (this) {
            COURSE -> CourseFragment.getInstance()
            GRADE -> GradeBaseFragment.getInstance()
            NEWS -> NewsMainBaseFragment.getInstance()
        }
    }

    private lateinit var drawer: Drawer
    private var currentFragmentType: FragmentType? = null
    private lateinit var autoLoginViewModel: MainActivityViewModel

    private val courseFirst = Settings.courseFirst

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        autoStatusBarModel()
        window.setBackgroundDrawable(null)
        super.onCreate(savedInstanceState)
        if (Account.cno.isBlank() && !Account.useWeb) {
            startLogin()
        } else {
            MainActivityUI().setContentView(this)
            initDrawer()
            showFragment(if (courseFirst) FragmentType.COURSE else FragmentType.GRADE)
            StatusBarUtil.setColor(this, getColorByCompat(R.color.light_text_color), 0)
            initViewModel()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 888) {
            if (resultCode == 888) {
                toastSuccess("登陆成功")
            } else {
                toastError("网页登陆失败")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /*******************************************控件初始化******************************************/
    private fun initDrawer() {
        val profile = ProfileDrawerItem()
            .withName(Account.name)
            .withEmail(Account.department)
        val accountHeader = AccountHeaderBuilder()
            .withActivity(this)
            .withTranslucentStatusBar(true)
            .withHeaderBackground(R.color.light_text_color)
            .withTextColorRes(R.color.dark_text_color)
            .addProfiles(profile)
            .withOnAccountHeaderListener {
                showLogoutDialog()
            }
            .build()

        drawer = DrawerBuilder()
            .apply { setDrawerItems(this, context = this@MainActivity) }
            .withDrawerGravity(Gravity.START)
            .withSliderBackgroundColor(getColorByCompat(R.color.light_text_color))
            .withActivity(this)
            .withDrawerWidthDp(300)
            .withAccountHeader(accountHeader)
            .withOnDrawerItemClickListener {
                when (it) {
                    10 -> showFragment(FragmentType.COURSE)
                    20 -> showFragment(FragmentType.GRADE)
                    21 -> toWeb("教务系统", GET_PERMISSION_URL)
                    30 -> toWeb("校园卡", CARD_URL)
                    31 -> toWeb("黄页", "http://debugtbs.qq.com")
                    32 -> showFragment(FragmentType.NEWS)
                    36 -> toWeb("校园网", SCHOOL_NET_URL)
                    40 -> startActivityDelayed<AboutActivity>(200)
                    41 -> showLogoutDialog()
                    42 -> startActivityDelayed<WorkActivity>(200)
                    43 -> startActivityDelayed<SettingActivity>(200)
                    44 -> toWeb("百事通", BOYA_URL)
                    45 -> toWeb("46级查询", CET46_URL)
                    else -> return@withOnDrawerItemClickListener
                }
                drawer.closeDrawer()
            }
            .build()
    }

    @SuppressLint("ResourceType")
    private fun initViewModel() {
        val loginPopWindow = AutoLoginPopWindow(this)
        loginPopWindow.setText("尝试自动登陆")
        delayed(500) {
            loginPopWindow.show()
        }
        autoLoginViewModel = getViewModel()
        autoLoginViewModel.loginStatus.observe(this, Observer {
            when (it) {
                LoginStatus.NEED_OPEN_WEB -> {
                    delayed(1_000){
                        loginPopWindow.dismiss()
                    }
                    toastInfo("需要重新登陆")
                    startActivityForResult<LoginWebActivity>(888)
                }
                LoginStatus.SUCCESS -> {
                    loginPopWindow.setText("登陆成功")
                    delayed(1000) {
                        loginPopWindow.dismiss()
                    }
                }
                LoginStatus.USER_OR_PWD_ERROR -> {
                    loginPopWindow.dismiss()
                    MaterialDialog(this).show {
                        title(text = "登陆错误")
                        message(text = "账号或密码错误.\n1.请尝试重新登陆\n2.请去教务处查看能否登陆\n3.加群反馈:315170003")
                        positiveButton(text = "我知道了") {

                        }
                        negativeButton(text = "重新登陆") {
                            showLogoutDialog()
                        }
                    }
                }
                LoginStatus.NET_ERROR -> {
                    loginPopWindow.dismiss()
                    MaterialDialog(this).show {
                        title(text = "登陆错误")
                        message(text = "网络出错啦.\n1.请尝试重新登陆\n2.请去教务处查看能否登陆\n3.加群反馈:315170003")
                        positiveButton(text = "我知道了") {

                        }
                        negativeButton(text = "重新登陆") {
                            autoLoginViewModel.autoLogin()
                            loginPopWindow.setText("尝试自动登陆")
                            loginPopWindow.show()
                        }
                    }
                }
            }
        })
        autoLoginViewModel.captchaLiveData.observe(this, Observer { captchaData ->
            MaterialDialog(this).show {
                var et: EditText? = null
                val view = UI {
                    linearLayout {
                        et = editText {
                            id = 1
                            onTextChange {
                                setActionButtonEnabled(
                                    WhichButton.POSITIVE,
                                    it?.trim()?.length == 4
                                )
                            }
                        }.lparams(dip(0)) {
                            weight = 3f
                        }
                        imageView {
                            imageBitmap = BitmapFactory.decodeByteArray(captchaData.bytes,
                                0,
                                captchaData.bytes.size,
                                BitmapFactory.Options().apply {
                                    inPreferredConfig = Bitmap.Config.RGB_565
                                })
                            id = 2
                        }.lparams(dip(0), matchParent) {
                            weight = 1f
                        }
                    }
                }.view
                title(text = "请输入验证码")
                customView(view = view)
                positiveButton {
                    val code = et!!.text.trim().toString()
                    autoLoginViewModel.submitCaptcha(code, captchaData.channel)
                }
                negativeButton {
                    loginPopWindow.dismiss()
                    autoLoginViewModel.cancel()
                    toastInfo("取消了自动登陆")
                }
                cancelOnTouchOutside(false)
                cancelable(false)
            }
        })
        autoLoginViewModel.autoLogin()
    }

    private fun startLogin() {
        startActivity<LoginActivity>()
        finish()
    }

    fun openDrawer() = drawer.openDrawer()

    override fun onBackPressed() {
        if (drawer.isDrawerOpen)
            drawer.closeDrawer()
        else {
            when (currentFragmentType) {
                FragmentType.COURSE -> {
                    if (courseFirst) {
                        finish()
                    } else {
                        showFragment(FragmentType.GRADE)
                    }
                }
                FragmentType.GRADE -> {
                    if (courseFirst) {
                        showFragment(FragmentType.COURSE)
                    } else {
                        finish()
                    }
                }
                FragmentType.NEWS -> showFragment(if (courseFirst) FragmentType.COURSE else FragmentType.GRADE)
            }
        }
    }

    private fun toWeb(title: String, url: String) = delayed(200) {
        startActivity<WebActivity>("title" to title, "url" to url)
    }

    @SuppressLint("ResourceType")
    private fun showFragment(fragmentType: FragmentType) {
        if (currentFragmentType == fragmentType) {
            return
        }
        val fragment = fragmentType.getFragment()

        supportFragmentManager.beginTransaction().run {
            when {
                !fragment.isAdded -> {
                    if (currentFragmentType != null) {
                        hide(currentFragmentType!!.getFragment())
                    }
                    add(123456, fragment)
                }
                else -> {
                    hide(currentFragmentType!!.getFragment()).show(fragment)
                }
            }
            commit()
        }
        currentFragmentType = fragmentType
    }

    private fun showLogoutDialog() {
        MaterialDialog(this).show {
            lifecycleOwner(this@MainActivity)
            title(text = "提示")
            message(text = "您确定要退出登陆吗？这不会清空你的课表数据库。")
            positiveButton {
                Http.clear()
                Account.clear()
                startLogin()
            }
            negativeButton {

            }
        }
    }
}