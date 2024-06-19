package com.example.sport.ui;

import static com.example.baselibs.TimeUtil.getCurrentTime;
import static com.example.baselibs.TimeUtil.stringToTime;
import static com.example.baselibs.TimeUtil.timeFormat;
import static com.example.baselibs.TimeUtil.timeToString;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.example.baselibs.net.network.bean.DbRecord;
import com.example.baselibs.room.baseroom.AppDataBase;
import com.example.sport.R;
import com.example.sport.db.DbManger;
import com.example.baselibs.net.network.UploadUtil;
import com.example.sport.record.PathRecord;
import com.example.sport.util.PathSmoothTool;
import com.example.sport.view.MyProgressButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//AMap.OnMyLocationChangeListener,
public class Sport_Activity_OutRoom extends AppCompatActivity implements View.OnClickListener {

    //地图种的类
    protected static MapView mapView;
    private AMap aMap;
    private List<LatLng> sportLatLngs = new ArrayList<>();
    private PolylineOptions polylineOptions;
    private PathSmoothTool pathSmoothTool;  //轨迹平滑处理类
    private LocationSource.OnLocationChangedListener mListener = null;
    private AMapLocationClient mLocationClient;

    //记录
    private PathRecord pathRecord;
    private double distance;
    private long recordTime = 0;
    private double lastUpdate;
    private DbRecord dbRecord;

    //判断
    private boolean isStart = false;

    //控件
    private TextView distance_tv_1;
    private TextView distribution_tv_2;
    private Chronometer ch_1;
    private Button bt_start;
    private FrameLayout frameLayout;
    private TextView animation_tv;
    private MyProgressButton myProgressButton;

    private LocationService.myBind locationBinder;
    private Context mContext;


    @SuppressLint({"ShortAlarm", "InvalidWakeLockTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_out_room);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //地图显示
        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this, true);

        //获取地图实例
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        //初始化控件
        initControl();
        ch_1.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                ch_1.setText(timeToString(chronometer));
            }
        });
        applyForRight();
        myProgressButton.setListener(new MyProgressButton.MyProgressButtonFinishCallback() {
            @Override
            public void isFinish() {
                pathRecord.setDuration(stringToTime(ch_1.getText().toString()));

                dbRecord = new DbRecord();
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String format = decimalFormat.format(pathRecord.getDistance());
                DecimalFormat decimalFormat1 = new DecimalFormat("0.0");
                String format1 = decimalFormat1.format(pathRecord.getDuration());

                dbRecord.setRunTime(format1);
                dbRecord.setRunWhen(getCurrentTime());
                dbRecord.setDistance(format);
                dbRecord.setPhone(UploadUtil.user.getPhone());

                initDialog();
            }

            @Override
            public void isCancel() {

            }
        });

        Intent bindIntent = new Intent(this, LocationService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);

        //创建Alarm并启动
        Intent intent = new Intent("LOCATION_CLOCK");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 每十五秒唤醒一次
        long second = 15 * 1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), second, pendingIntent);
        mContext = this;
    }


    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        if (locationBinder != null) {
            unbindService(connection);
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    //开启定位服务
    private void toLocate() {

        if (aMap == null) {
            aMap = mapView.getMap();
        }
        pathRecord = new PathRecord();

        createAnimation();

        setUpMap();
        initPolyline();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    //初始化控件
    private void initControl() {
        ch_1 = findViewById(R.id.ch_sport);
        distance_tv_1 = findViewById(R.id.tv_sport_out);
        distribution_tv_2 = findViewById(R.id.tv_2);
        animation_tv = findViewById(R.id.tv_animation);
        frameLayout = findViewById(R.id.animation_layout);
        myProgressButton = findViewById(R.id.myProgress);

        bt_start = findViewById(R.id.bt_1);
        bt_start.setOnClickListener(this);
    }

    //起始动画
    private void createAnimation() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                animation_tv.setText("3");
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(animation_tv, "scaleX", 70f, 20f);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(animation_tv, "scaleY", 70f, 20f);
                objectAnimator1.setDuration(1000);
                objectAnimator2.setDuration(1000);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(objectAnimator1).with(objectAnimator2);
                animatorSet.start();
                AnimatorSet clone1 = animatorSet.clone();
                AnimatorSet clone2 = animatorSet.clone();
                AnimatorSet clone3 = animatorSet.clone();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animation_tv.setText("2");
                        clone1.start();
                    }
                });
                clone1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animation_tv.setText("1");
                        clone2.start();
                    }
                });
                clone2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animation_tv.setText("Go");
                        clone3.start();
                    }
                });
                clone3.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        frameLayout.setVisibility(View.GONE);
                        isStart = true;
                        bt_start.setVisibility(View.VISIBLE);
                        ch_1.setBase(SystemClock.elapsedRealtime());
                        lastUpdate = stringToTime(ch_1.getText().toString());
                        ch_1.start();
                    }
                });
            }
        });
    }

    //点击监听
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_1) {
            if (isStart) {
                bt_start.setText("继续");
                recordTime = SystemClock.elapsedRealtime() - ch_1.getBase();
                ch_1.stop();
                pauseLocation();
                myProgressButton.setVisibility(View.VISIBLE);
                isStart = false;
            } else {
                bt_start.setText("暂停");
                ch_1.setBase(SystemClock.elapsedRealtime() - recordTime);
                ch_1.start();
                startUpLocation();
                myProgressButton.setVisibility(View.INVISIBLE);
                isStart = true;
            }
        }
    }


    //计算总距离
    private double getDistance(List<LatLng> list) {
        double distance = 0;
        if (list == null || list.size() == 0) {
            return distance;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            LatLng firstLatLng = list.get(i);
            LatLng secondLatLng = list.get(i + 1);
            double betweenDis = AMapUtils.calculateLineDistance(firstLatLng, secondLatLng);
            distance = distance + betweenDis;
        }
        return distance;
    }

    //初始化路线绘制对象
    private void initPolyline() {
        polylineOptions = new PolylineOptions();
        polylineOptions.color(getResources().getColor(R.color.sport_point_write));
        polylineOptions.width(10f);
        polylineOptions.useGradient(true);

        pathSmoothTool = new PathSmoothTool();
        pathSmoothTool.setIntensity(2);
    }

    //退出的dialog
    private void initDialog() {
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(this);
        alterDialog.setCancelable(false);
        alterDialog.setTitle("运动已完成，是否现在上传？");
        alterDialog.setNegativeButton("稍后上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                finish();
            }
        });
        alterDialog.setPositiveButton("现在上传", new DialogInterface.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                List<Long> result = AppDataBase.getInstance(mContext).getDbRecordDao().insert(dbRecord);
                if(result != null) {
                    Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
                }

                /*Map<String, String> map = new HashMap<>();
                map.put("runTime", dbRecord.getRunTime());
                map.put("runWhen", dbRecord.getRunWhen());
                map.put("distance", dbRecord.getDistance());
                //uid
                map.put("uid", UploadUtil.uid);
                ApiService postService = UploadUtil.sentPostService();
                postService.sport_postCall("run/addRunRecord", map).enqueue(new Callback<BaseResponse<String>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                        if (response.body() == null) {
                            DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                            Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().isSuccess()) {
                            Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                        } else {
                            DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                        DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                        Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });*/
                finish();
            }
        });
        alterDialog.show();
    }


    private void startUpLocation() {
        locationBinder.start(aMapLocationListener);
    }

    private void pauseLocation() {

        locationBinder.pause(aMapLocationListener);
    }

    //初始化地图
    private void setUpMap() {

        //添加定位监听
        aMap.setLocationSource(locationSource);

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.strokeWidth(0); //精度圈大小
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.sport_point));
        //去除精度圈
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.showMyLocation(true);//设置是否显示蓝点
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        //设置缩放比例
        aMap.setMinZoomLevel(16);
        aMap.setMaxZoomLevel(18);
        //隐藏Logo,高德logo不可隐藏
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
    }

    private final LocationSource locationSource = new LocationSource() {
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            mListener = onLocationChangedListener;
        }

        @Override
        public void deactivate() {
            mListener = null;
            if (mLocationClient != null) {
                mLocationClient.stopLocation();
                mLocationClient.onDestroy();
            }
            mLocationClient = null;
        }
    };

    /**
     * 定位结果回调
     *
     * @param aMapLocation 位置信息类
     */
    private final AMapLocationListener aMapLocationListener = aMapLocation -> {
        if (null == aMapLocation)
            return;
        if (aMapLocation.getErrorCode() == 0) {
            //定位成功
            updateLocation(aMapLocation);
        }
    };


    //更新路径
    @SuppressLint("SetTextI18n")
    private void updateLocation(Location location) {
        if (location.getLatitude() == 0 && location.getLongitude() == 0) {
            return;
        }
        pathRecord.addLaLng(new LatLng(location.getLatitude(), location.getLongitude()));
        distance = getDistance(pathRecord.getPathLinePoints());
        pathRecord.setDistance((float) distance / 1000f);
        //计算配速
        float sportMile = (float) distance / 1000f;
        float useTime = stringToTime(ch_1.getText().toString());
        if (sportMile > 0.005 && useTime > 0.1) {
            if (useTime - lastUpdate > 0.05) {
                distribution_tv_2.setText(timeFormat(useTime / sportMile));
                lastUpdate = useTime;
            }

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String format = decimalFormat.format(sportMile);
            distance_tv_1.setText(format);
        }
        sportLatLngs.clear();
        //轨迹平滑处理
        sportLatLngs = new ArrayList<>(pathSmoothTool.pathOptimize(pathRecord.getPathLinePoints()));

        if (!sportLatLngs.isEmpty()) {
            if (mListener != null)
                mListener.onLocationChanged(location);// 显示系统小蓝点
            polylineOptions.add(sportLatLngs.get(sportLatLngs.size() - 1));
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
        }
        aMap.addPolyline(polylineOptions);
    }


    //服务
    private ServiceConnection connection = new ServiceConnection() {    //创建一个ServiceConnection匿名类对象，重写其两个方法，一个为建里链接时执行，一个为接触链接时执行
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            locationBinder = (LocationService.myBind) iBinder;
            locationBinder.start(aMapLocationListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    //申请权限
    private void applyForRight() {
        if (ContextCompat.checkSelfPermission(Sport_Activity_OutRoom.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(Sport_Activity_OutRoom.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(Sport_Activity_OutRoom.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(Sport_Activity_OutRoom.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE
            }, 1);
        } else {
            toLocate();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(Sport_Activity_OutRoom.this, "开启权限使用完整功能", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                }
                toLocate();
        }
    }
}