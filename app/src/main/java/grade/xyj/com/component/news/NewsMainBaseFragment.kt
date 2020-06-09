package grade.xyj.com.component.news

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.bumptech.glide.Glide
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import grade.xyj.com.R
import grade.xyj.com.base.NewBaseFragment
import grade.xyj.com.util.extend.getColorByCompat
import grade.xyj.com.util.extend.getViewModel
import grade.xyj.com.util.extend.runNoResult
import kotlinx.android.synthetic.main.fragment_news.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

class NewsMainBaseFragment :NewBaseFragment(){


    companion object {

        private lateinit var instance: NewsMainBaseFragment

        fun getInstance(): NewsMainBaseFragment {
            if (!this::instance.isInitialized) {
                instance = NewsMainBaseFragment()
            }
            return instance
        }
    }

    override fun setTitle(): String = "校内新闻"

    val titles = listOf("通知公告","人事信息","学工信息","教务信息","科研动态")

    var times = 0

    lateinit var viewModel: NewChildViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LayoutInflater.from(context).inflate(R.layout.fragment_news,mContentView)
        viewModel = getViewModel()

        initIndicator()
        initBanner()

        viewModel.getBannerImages()
    }

    private fun initBanner(){
        viewModel.images.observe(viewLifecycleOwner, Observer {
            it?.runNoResult {
                banner.setImages(this)
            }
            banner.start()
        })
        banner.run {
            setDelayTime(3000)
            setBannerAnimation(Transformer.CubeOut)
            setIndicatorGravity(BannerConfig.RIGHT)
            setImageLoader(object :ImageLoader(){
                override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                    Glide.with(context)
                            .asBitmap()
                            .load(path.toString())
                            .thumbnail(0.5f)
                            .into(imageView)
                }
            })
        }
    }

    private fun initIndicator(){

        val commonNavigator = CommonNavigator(context).apply {
            scrollPivotX = 0.25f
            adapter = object : CommonNavigatorAdapter(){
                override fun getTitleView(p0: Context, p1: Int): IPagerTitleView = SimplePagerTitleView(p0).apply {
                    text = titles[p1]
                    textSize = 12f
                    normalColor = Color.GRAY
                    selectedColor = context.getColorByCompat(R.color.dark_text_color)
                    setOnClickListener { viewpager.currentItem = p1 }
                }

                override fun getCount(): Int = titles.size

                override fun getIndicator(p0: Context): IPagerIndicator?  = LinePagerIndicator(p0).apply {
                    mode = LinePagerIndicator.MODE_EXACTLY
                    yOffset = ConvertUtils.dp2px(3f).toFloat()
                    setColors(context.getColorByCompat(R.color.color_san_pressed))
                }
            }
        }

        ViewPagerHelper.bind(
                magicIndicator.apply {
                    //setBackgroundColor(Color.WHITE)
                    navigator = commonNavigator
                },
                viewpager.apply { adapter = NewsPageAdapter(childFragmentManager, listOf(
                        //通知公告
                        NewsChildFragment.newInstance("1994a3b58bef4ee887e1efc19881decd", 2),
                        //人事信息
                        NewsChildFragment.newInstance("982564a6cdfd4307804a07e27e6322bc", 4),
                        //学工信息
                        NewsChildFragment.newInstance("f2ba8cfe5d6f43ddab67ea20db1f6952", 6),
                        //教务信息
                        NewsChildFragment.newInstance("36d47fcd3e774f289adfef1d93138a9d", 5),
                        //科研动态
                        NewsChildFragment.newInstance("48e8abfb983b4e4486b69feacad1dc1b", 3)
                ))
                    offscreenPageLimit = 4
                }
        )
    }

    override fun onStart() {
        super.onStart()
        banner.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        banner.stopAutoPlay()
    }
}