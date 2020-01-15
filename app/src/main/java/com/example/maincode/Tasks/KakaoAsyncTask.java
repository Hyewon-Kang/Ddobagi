package com.example.maincode.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.maincode.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class KakaoAsyncTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private Handler mHandler;
    private final static int TIME_OUT = 10000;
    // https://developers.kakao.com/docs/restapi/local#%ED%82%A4%EC%9B%8C%EB%93%9C-%EA%B2%80%EC%83%89 참조
    private final static String KAKAO_KEYWORD_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/keyword.json?";
    private final static String KAKAO_CATEGORY_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/category.json?";

//    public KakaoAsyncTask() {
//    }

    public KakaoAsyncTask(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder buffer = new StringBuilder();

        String latitude = params[0];// 위도
        String longitude = params[1]; // 경도

        Log.d("ssb10300", "KakaoAsyncTask :: doInBackground");
        HashMap<String, String> hashMapParams = new HashMap<>();

        hashMapParams.put("y", latitude);
        hashMapParams.put("x", longitude);
        //hashMapParams.put("radius", "1000");
        hashMapParams.put("radius", "2000");
        hashMapParams.put("query", "파출소");
        //hashMapParams.put("query", "지하철역");
        String requestURL = KAKAO_KEYWORD_SEARCH_URL;
        Log.d("ssb10300", "KakaoAsyncTask requestURL : " + requestURL);

        String errorCode;
        HttpsURLConnection httpUrlConnection = null;
        InputStream connInputStream = null;

        try {
            String urlStr = makeUrl(requestURL, hashMapParams);
            Log.d("ssb10300", "KakaoAsyncTask urlStr : " + urlStr);

            URL url = new URL(urlStr);

            httpUrlConnection = (HttpsURLConnection) url.openConnection();

            httpUrlConnection.setConnectTimeout(TIME_OUT);
            httpUrlConnection.setReadTimeout(TIME_OUT);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.setRequestProperty("Authorization", "KakaoAK " + mContext.getString(R.string.daum_map_rest_api_key));

            Log.d("ssb10300", "KakaoAsyncTask httpUrlConnection.getRequestMethod : [ " + httpUrlConnection.getRequestMethod() + " ] ");

            int resCode = httpUrlConnection.getResponseCode();
            Log.d("ssb10300", "KakaoAsyncTask response Code : " + resCode);
            if (resCode == HttpURLConnection.HTTP_OK) {
                connInputStream = httpUrlConnection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(connInputStream));
                int c;
                while ((c = in.read()) != -1) {
                    buffer.append((char) c);
                }
                Log.d("ssb10300", "KakaoAsyncTask Connection Result : " + buffer.toString());

            } else {
                errorCode = String.valueOf(resCode);
                Log.d("ssb10300", "KakaoAsyncTask Connection Result : error : " + errorCode);
            }
        } catch (UnsupportedEncodingException e) {
            Log.d("ssb10300", "KakaoAsyncTask UnsupportedEncodingException");
        } catch (ProtocolException e) {
            Log.d("ssb10300", "KakaoAsyncTask ProtocolException");
        } catch (MalformedURLException e) {
            Log.d("ssb10300", "KakaoAsyncTask MalformedURLException");
        } catch (IOException e) {
            Log.d("ssb10300", "KakaoAsyncTask IOException");
        } catch (Exception e) {
            Log.d("ssb10300", "KakaoAsyncTask Exception : " + e.getMessage());
        }

        return buffer.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onCancelled(String result) {
        super.onCancelled(result);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("ssb10300", "KakaoAsyncTask onPostExecute() result : " + result);
        if (mHandler != null && !TextUtils.isEmpty(result)) {
            Message msg = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("kakaoResult", result);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
    }

    private String makeUrl(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        String result = null;
        String builderStr;

        if (url != null && params != null) {

            Set<Entry<String, String>> set = params.entrySet();
            Iterator<Entry<String, String>> it = set.iterator();
            Entry<String, String> e;
            while (it.hasNext()) {
                e = it.next();
                String value = URLEncoder.encode(e.getValue(), "UTF-8");
                builder.append(e.getKey()).append("=").append(value);
                builder.append("&");
            }

            if (builder.lastIndexOf("&") >= builder.length() - 1) {
                builderStr = builder.substring(0, builder.length() - 1);
            } else {
                builderStr = builder.toString();
            }
            result = url + builderStr;

            Log.d("ssb10300", "KakaoAsyncTask makeUrl :: url : " + result);
        }

        return result;
    }
}
