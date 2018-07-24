package com.carlt.sesame.data.set;

public class ModifyCarInfo {

	private String brandid;// 车型ID

	private String optionid;// 车系ID

	private String carid;// 车款ID

	private String carname;// 车款名称

	private String carlogo;// 车款logo的URL

	private String summiles;// (float) 总里程(如果是整数，请在后面加上".0")

	private String buydate;// 购车时间,格式为年-月-日，如：2014-05-23
	
	private String year;// 车款年限


	public String getBrandid() {
		return brandid;
	}

	public void setBrandid(String brandid) {
		this.brandid = brandid;
	}

	public String getOptionid() {
		return optionid;
	}

	public void setOptionid(String optionid) {
		this.optionid = optionid;
	}

	public String getCarid() {
		return carid;
	}

	public void setCarid(String carid) {
		this.carid = carid;
	}

	public String getSummiles() {
		return summiles;
	}

	public void setSummiles(String summiles) {
		this.summiles = summiles;
	}

	public String getBuydate() {
		return buydate;
	}

	public void setBuydate(String buydate) {
		this.buydate = buydate;
	}

	public String getCarname() {
		return carname;
	}

	public void setCarname(String carname) {
		this.carname = carname;
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

}
