package com.example.sport.ui;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class LocationService extends Service {

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new myBind();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG1", "onCreate: ");
    }

    private final LocationService.myBind locationService = new myBind();

    class myBind extends Binder {
        public void start(AMapLocationListener listener) {
            startLocation();
            mLocationClient.setLocationListener(listener);
            //开始定位
            mLocationClient.startLocation();
        }

        public void pause(AMapLocationListener listener) {
            //停止定位
            if (null != mLocationClient) {
                mLocationClient.stopLocation();
                mLocationClient.unRegisterLocationListener(listener);
                mLocationClient.onDestroy();
                mLocationClient = null;
            }
        }
    }


    private void startLocation() {
        if (mLocationClient == null) {
            try {
                mLocationClient = new AMapLocationClient(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //设置定位属性
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
            mLocationOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
            mLocationOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            mLocationOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
            mLocationOption.setNeedAddress(false);//可选，设置是否返回逆地理地址信息。默认是true
            mLocationOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
            mLocationOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
            AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
            mLocationOption.setSensorEnable(true);//可选，设置是否使用传感器。默认是false
            mLocationOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
            mLocationOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
            mLocationOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.ZH);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
            mLocationOption.setMockEnable(false);
            mLocationClient.setLocationOption(mLocationOption);
        }
    }

}