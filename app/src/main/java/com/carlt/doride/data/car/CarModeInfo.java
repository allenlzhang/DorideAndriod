
package com.carlt.doride.data.car;

public class CarModeInfo {
    private String id;

    private String pid;

    private String title;

    private String title_py;

    private String carlogo;

    private String year;// 车款年限

    private boolean isTitleSub;// 是否是小标题

    private int type;// 列表类型

    private String  factory_year;

    public final static int TYPE_FIRST = 1;

    public final static int TYPE_SECOND = 2;

    public final static int TYPE_THIRD = 3;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_py() {
        return title_py;
    }

    public void setTitle_py(String title_py) {
        this.title_py = title_py;
    }

    public String getCarlogo() {
        return carlogo;
    }

    public void setCarlogo(String carlogo) {
        this.carlogo = carlogo;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isTitleSub() {
        return isTitleSub;
    }

    public void setTitleSub(boolean isTitleSub) {
        this.isTitleSub = isTitleSub;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFactory_year() {
        return factory_year;
    }

    public void setFactory_year(String factory_year) {
        this.factory_year = factory_year;
    }
}
