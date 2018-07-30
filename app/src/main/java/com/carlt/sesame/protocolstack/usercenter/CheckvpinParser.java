package com.carlt.sesame.protocolstack.usercenter;

import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONObject;

/**
 * 检验VPIN码解析
 * @author Administrator
 *
 */
public class CheckvpinParser extends BaseParser {
	private String vpinType ;

	public String getReturn() {
		return vpinType;
	}

	@Override
	protected void parser() {
		JSONObject mJSON_data = mJson.optJSONObject("data");
		vpinType = mJSON_data.optString("is_before");
		if(vpinType.equals("1")){
		    //前装
		    SesameLoginInfo.setInstallorder(true);
		}else if(vpinType.equals("0")){
		    //后装
		    SesameLoginInfo.setInstallorder(false);
		}
		
		String deviceIdString= SesameLoginInfo.getDeviceidstring();
		
		if(deviceIdString!=null&&!deviceIdString.equals("")){
		    //当前用户已绑定设备，故无论用户是前装、还是后装，都改为前装
		    SesameLoginInfo.setIsJumptoBind(SesameLoginInfo.noJumptoBind);
		    //同时将当前用户绑定设备的状态从已绑定改为未绑定
		    //LoginInfo.setDeviceidstring("");
		}else{
		  //当前用户未绑定设备
		    if(vpinType.equals("1")){
	            //前装
	            SesameLoginInfo.setIsJumptoBind(SesameLoginInfo.noJumptoBind);
	        }else if(vpinType.equals("0")){
	            //后装
	            SesameLoginInfo.setIsJumptoBind(SesameLoginInfo.needJumptoBind);
	        }
		}
	}


}
