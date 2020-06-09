package grade.xyj.com.bean;

import java.util.List;

public class GradesBean {

    public int currentPage;
    public int currentResult;
    public boolean entityOrField;
    public List<ItemsBean> items;

    public static class ItemsBean {
        public String jgpxzd;
        public String jxb_id;
        public String jxbmc;
        public String kch;
        public String kch_id;
        public String kcmc;
        public String kkbm_id;
        public String kkbmmc;
        public String listnav;
        public String localeKey;
        public boolean pageable;
        public QueryModelBean queryModel;
        public boolean rangeable;
        public String row_id;
        public String totalResult;
        public UserModelBean userModel;
        public String xf;
        public String xh_id;
        public String xmblmc;
        public String xmcj;
        public String xnm;
        public String xnmmc;
        public String xqm;
        public String xqmmc;

        public static class QueryModelBean {
            public int currentPage;
            public int currentResult;
            public boolean entityOrField;
            public int limit;
            public int offset;
            public int pageNo;
            public int pageSize;
            public int showCount;
            public int totalCount;
            public int totalPage;
            public int totalResult;
        }

        public static class UserModelBean {
        

            public boolean monitor;
            public int roleCount;
            public String roleKeys;
            public String roleValues;
            public int status;
            public boolean usable;
            
        }
    }
}
