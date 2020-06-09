package grade.xyj.com.bean;

import java.util.ArrayList;
import java.util.List;

public class NGradeBean {
    public String name;
    public String type;
    public String cj;
    public String xq;
    public double xf;

    public GradesBean.ItemsBean itemsBean;

    public List<NGradeBean> list;

    public NGradeBean(GradesBean.ItemsBean bean){
        itemsBean = bean;
        name = bean.kcmc;
        type = bean.xmblmc;
        cj = bean.xmcj;
        xq = bean.xqmmc;
        xf = Double.parseDouble(bean.xf);

        if(type.equals("总评")) list = new ArrayList<>();
    }

}
