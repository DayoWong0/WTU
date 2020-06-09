package grade.xyj.com.bean;

import java.util.List;

public class NewsBean {

    public int totalRows;
    public String status;
    public int pageSize;
    public int totalPage;
    public int pageNum;
    public List<BulletinListBean> bulletinList;

    public static class BulletinListBean {
        public String PUBLISH_USER_DEPT_NAME;
        public String WID;
        public String TITLE;

        public String PUBLISH_TIME;
        public String CREATE_TIME;
      
        public int NUM;
    }
}
