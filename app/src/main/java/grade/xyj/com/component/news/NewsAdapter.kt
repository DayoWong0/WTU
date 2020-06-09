package grade.xyj.com.component.news

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import grade.xyj.com.R
import grade.xyj.com.bean.NewsBean


//BulletinListBean
class NewsAdapter(da:List<NewsBean.BulletinListBean>?): BaseQuickAdapter<NewsBean.BulletinListBean, BaseViewHolder>(R.layout.item_news,da) {
    override fun convert(helper: BaseViewHolder, item: NewsBean.BulletinListBean) {
        helper.run {
            setText(R.id.news_title,item.TITLE)
            setText(R.id.news_from,item.PUBLISH_USER_DEPT_NAME)
            setText(R.id.news_time,item.PUBLISH_TIME)
        }
    }


}