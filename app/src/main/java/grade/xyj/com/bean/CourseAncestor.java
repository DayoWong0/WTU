package grade.xyj.com.bean;

import androidx.room.Ignore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CourseAncestor implements Serializable {

    /** 行号 */
    @Ignore
    protected int row;
    /** 所占行数 */
    @Ignore
    protected int rowNum = 1;
    /** 列号 */
    @Ignore
    protected int col;

    /** 颜色 */
    @Ignore
    protected int color = -1;
    @Ignore
    protected int color2 = -1;
    /** 显示的内容 */
    @Ignore
    protected String text;

    /** 活跃状态 */
    @Ignore
    protected boolean activeStatus = true;

    /** 是否显示 */
    @Ignore
    protected boolean displayable = true;

    @Ignore
    protected List<Integer> showIndexes = new ArrayList<>();

    public CourseAncestor() {
    }

    public void init(int row, int col, int rowNum, int color) {
        this.row = row;
        this.rowNum = rowNum;
        this.col = col;
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public CourseAncestor setRow(int row) {
        this.row = row;
        return this;
    }

    public int getRowNum() {
        return rowNum;
    }

    public CourseAncestor setRowNum(int rowNum) {
        this.rowNum = rowNum;
        return this;
    }

    public int getCol() {
        return col;
    }

    public CourseAncestor setCol(int col) {
        this.col = col;
        return this;
    }

    public int getColor() {
        return color;
    }

    public CourseAncestor setColor(int color) {
        this.color = color;
        return this;
    }

    public String getText() {
        return text;
    }

    public CourseAncestor setText(String text) {
        this.text = text;
        return this;
    }

    public boolean getActiveStatus() {
        return activeStatus;
    }

    public CourseAncestor setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
        return this;
    }

    public boolean isDisplayable() {
        return displayable;
    }


    public List<Integer> getShowIndexes() {
        return showIndexes;
    }

    public CourseAncestor addIndex(int index) {
        if (!showIndexes.contains(index)) {
            showIndexes.add(index);
        }
        return this;
    }

    public boolean shouldShow(int index) {
        return showIndexes.contains(index);
    }

}
