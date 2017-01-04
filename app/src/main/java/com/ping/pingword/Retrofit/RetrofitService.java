package com.ping.pingword.Retrofit;


import java.util.HashMap;
import java.util.Map;

import ping.Lib.RetrofitProgress.okhttp.OKHttpFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * ClassName RetrofitService
 * description 静态内部类的单例模式
 * author  wenhuaping
 * date createTime：2016/03/11
 * version
 */
public class RetrofitService {
    private ApiService apiService;

    private static final class LazyHolder {
        private static final RetrofitService retrofitService = new RetrofitService();
    }

    public static RetrofitService getInstance() {
        return LazyHolder.retrofitService;
    }

    public RetrofitService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://fanyi.youdao.com/").addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).client(OKHttpFactory.getInstance().getOkHttpClient()).build();
        apiService = retrofit.create(ApiService.class);
    }

    public interface ApiService {
        @GET("openapi.do")
        Call<RequestWord> getYoDaoWord(@QueryMap Map<String, String> map);

    }

    //获取翻译
    public void getYoDaoWord(String word, final ResultListener resultListener) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("keyfrom","dancibei");
        map.put("key","1619746159");
        map.put("type","data");
        map.put("doctype","json");
        map.put("version","1.1");
        map.put("q",word);
        apiService.getYoDaoWord(map).enqueue(new Callback<RequestWord>() {
            @Override
            public void onResponse(Call<RequestWord> call, Response<RequestWord> response) {
                RequestWord word = response.body();
                if (word != null && word.getErrorCode() == 0) {
                    resultListener.onSucess(word);
                } else if (word != null && word.getErrorCode() != 0) {
                    resultListener.onFailre(word.getErrorCode());
                } else {
                    resultListener.onServerError("翻译有误");
                }
            }

            @Override
            public void onFailure(Call<RequestWord> call, Throwable t) {
                resultListener.onFailre(100);
            }
        });
    }
}