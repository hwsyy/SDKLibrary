import android.util.Log;

import com.dataeye.DCAccount;
import com.dataeye.DCAgent;
import com.dataeye.DCEvent;
import com.dataeye.DCReportMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import SdkInterface.LogBase;
import SdkInterface.SDKInterfaceDefine;
import SdkInterface.SdkInterface;

/**
 * Created by GaiKai on 2018/4/10.
 */

public class DataEye extends LogBase
{
    @Override
    public void Init(JSONObject json)
    {
        SDKName = "DataEye";
        Log.d("Unity","DataEye Init "+ json.toString());
        try {

            DCAgent.setReportMode(DCReportMode.DC_AFTER_LOGIN);
            DCAgent.initConfig(SdkInterface.GetContext()
                    ,GetProperties().getProperty("AppID")
                    ,GetProperties().getProperty("ChannelID"));

        } catch (Exception e) {
            SendError("DataEye IOException",e);
        }
    }

    @Override
    public void Login(JSONObject json)
    {
        Log.d("Unity","DataEye Login " + json.toString());
        try {
            String accountId = json.getString(SDKInterfaceDefine.ParameterName_FunctionName);
            DCAccount.login(accountId);

        } catch (Exception e) {
           SendError("DataEye Login " + e.toString(),e);
        }
    }

    @Override
    public void LoginOut(JSONObject json) {
        Log.d("Unity","DataEye LoginOut "+ json.toString());
        DCAccount.logout();
    }

    @Override
    public void OnEvent(JSONObject json)
    {
        Log.d("Unity","DataEye OnEvent "+ json.toString());

        try {
            String eventID = json.getString(SDKInterfaceDefine.Log_ParameterName_EventID);

            if(json.has(SDKInterfaceDefine.Log_ParameterName_EventLabel))
            {
                String eventLabel = json.getString(SDKInterfaceDefine.Log_ParameterName_EventLabel);
                DCEvent.onEvent(eventID,eventLabel);
            }

            if(json.has(SDKInterfaceDefine.Log_ParameterName_EventMap))
            {
                Map map  = jsonToMap(json.getJSONObject(SDKInterfaceDefine.Log_ParameterName_EventMap));
                DCEvent.onEvent(eventID,map);
            }
        } catch (JSONException e) {
            SendError("DataEye OnEvent " + e.toString(),e);
        }

    }

    public static Map<Object, Object> jsonToMap(JSONObject json) {
        Map<Object, Object> map = (Map)json;
        return map;
    }
}
