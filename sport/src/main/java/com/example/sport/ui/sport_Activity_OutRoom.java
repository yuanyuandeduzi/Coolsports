package com.example.sport.ui;

import static com.example.sport.util.TimeUtil.getCurrentTime;
import static com.example.sport.util.TimeUtil.stringToTime;
import static com.example.sport.util.TimeUtil.timeFormat;
import static com.example.sport.util.TimeUtil.timeToString;

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
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.example.sport.R;
import com.example.sport.db.DbManger;
import com.example.sport.db.DbRecord;
import com.example.sport.record.PathRecord;
import com.example.sport.util.PathSmoothTool;
import com.example.sport.view.MyProgressButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class sport_Activity_OutRoom extends AppCompatActivity implements AMap.OnMyLocationChangeListener, View.OnClickListener {

    protected static MapView mapView;
    private AMap aMap;
    private List<LatLng> sportLatLngs = new ArrayList<>();
    private PolylineOptions polylineOptions;
    private PathSmoothTool pathSmoothTool;  //轨迹平滑处理类

    //记录
    private PathRecord pathRecord;
    private double distance;
    private long recordTime = 0;
    private double lastUpdate;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_out_room);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

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
                initDialog();
            }

            @Override
            public void isCancel() {

            }
        });
    }


    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
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
        //aMap = mapView.getMap();
        pathRecord = new PathRecord();

        createAnimation();

        setUpMap();
        initPolyline();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    //监听位置变化
    @Override
    public void onMyLocationChange(Location location) {
        //绘制移动路线
        if (isStart) {
            updateLocation(location);
        }
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

    //点击监听
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_1:
                if (isStart) {
                    bt_start.setText("继续");
                    recordTime = SystemClock.elapsedRealtime() - ch_1.getBase();
                    ch_1.stop();
                    myProgressButton.setVisibility(View.VISIBLE);
                    isStart = false;
                } else {
                    bt_start.setText("暂停");
                    ch_1.setBase(SystemClock.elapsedRealtime() - recordTime);
                    ch_1.start();
                    myProgressButton.setVisibility(View.INVISIBLE);
                    isStart = true;
                }
                break;
        }
    }


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
            polylineOptions.add(sportLatLngs.get(sportLatLngs.size() - 1));
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
        }
        aMap.addPolyline(polylineOptions);
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
        pathSmoothTool.setIntensity(4);
    }

    //退出的dialog
    private void initDialog() {
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(this);
        alterDialog.setCancelable(false);
        alterDialog.setTitle("运动已完成，是否现在上传？");
        alterDialog.setNegativeButton("稍后上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DbRecord dbRecord = new DbRecord();
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String format = decimalFormat.format(pathRecord.getDistance());
                DecimalFormat decimalFormat1 = new DecimalFormat("0.0");
                String format1 = decimalFormat1.format(pathRecord.getDuration());

                dbRecord.setRunTime(format1);
                dbRecord.setRunWhen(getCurrentTime());
                dbRecord.setDistance(format);
                DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                finish();
            }
        });
        alterDialog.setPositiveButton("现在上传", new DialogInterface.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DbManger.getInstance(getApplicationContext()).getAll().subscribe(new Consumer<List<DbRecord>>() {
                    @Override
                    public void accept(List<DbRecord> list) throws Exception {
                        Log.d("TAG", "accept: " + list.get(0).getRunWhen());
                    }
                });
            }
        });
        alterDialog.show();
    }

    //初始化地图
    private void setUpMap() {
        aMap = mapView.getMap();
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeWidth(0); //精度圈大小
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.sport_point));
        //去除精度圈
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.showMyLocation(true);//设置是否显示蓝点
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        //位置监听
        aMap.setOnMyLocationChangeListener(this);

        //设置缩放比例
        aMap.setMinZoomLevel(16);
        aMap.setMaxZoomLevel(18);
        //隐藏Logo,高德logo不可隐藏
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
    }

    //申请权限
    private void applyForRight() {
        if (ContextCompat.checkSelfPermission(sport_Activity_OutRoom.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(sport_Activity_OutRoom.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(sport_Activity_OutRoom.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(sport_Activity_OutRoom.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
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
                            Toast.makeText(sport_Activity_OutRoom.this, "", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                }
                toLocate();
        }
    }
}