package com.ionicframework.lovescanning967442.wxapi;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.openapi.SendAuth;
import com.wordsbaking.cordova.wechat.WeChat;

import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

/*
    Cordova WeChat Plugin
    https://github.com/vilic/cordova-plugin-wechat

    by VILIC VANE
    https://github.com/vilic

    MIT License
*/

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeChat.api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WeChat.api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        // not implemented
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp instanceof SendAuth.Resp) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("code", ((SendAuth.Resp)resp).token);
                        object.put("state", ((SendAuth.Resp)resp).state);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PluginResult res = new PluginResult(PluginResult.Status.OK, object);
                    WeChat.currentCallbackContext.sendPluginResult(res);
                }
                else
                    WeChat.currentCallbackContext.success();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                WeChat.currentCallbackContext.error(WeChat.ERR_USER_CANCEL);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                WeChat.currentCallbackContext.error(WeChat.ERR_AUTH_DENIED);
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                WeChat.currentCallbackContext.error(WeChat.ERR_SENT_FAILED);
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                WeChat.currentCallbackContext.error(WeChat.ERR_UNSUPPORT);
                break;
            case BaseResp.ErrCode.ERR_COMM:
                WeChat.currentCallbackContext.error(WeChat.ERR_COMM);
                break;
            default:
                WeChat.currentCallbackContext.error(WeChat.ERR_UNKNOWN);
                break;
        }
        finish();
    }

}