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
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
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
import com.amap.api.location.AMapLocationClientOption;
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
import com.example.baselibs.net.BaseResponse;
import com.example.sport.R;
import com.example.sport.db.DbManger;
import com.example.sport.db.DbRecord;
import com.example.baselibs.net.network.ApiService;
import com.example.baselibs.net.network.UploadUtil;
import com.example.sport.record.PathRecord;
import com.example.sport.util.PathSmoothTool;
import com.example.sport.view.MyProgressButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//AMap.OnMyLocationChangeListener,
public class Sport_Activity_OutRoom extends AppCompatActivity implements  View.OnClickListener {

    //???????????????
    protected static MapView mapView;
    private AMap aMap;
    private List<LatLng> sportLatLngs = new ArrayList<>();
    private PolylineOptions polylineOptions;
    private PathSmoothTool pathSmoothTool;  //?????????????????????
    private LocationSource.OnLocationChangedListener mListener = null;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    //??????
    private PathRecord pathRecord;
    private double distance;
    private long recordTime = 0;
    private double lastUpdate;
    private DbRecord dbRecord;

    //??????
    private boolean isStart = false;

    //??????
    private TextView distance_tv_1;
    private TextView distribution_tv_2;
    private Chronometer ch_1;
    private Button bt_start;
    private FrameLayout frameLayout;
    private TextView animation_tv;
    private MyProgressButton myProgressButton;

    private LocationService.myBind locationBinder;

    @SuppressLint({"ShortAlarm", "InvalidWakeLockTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_out_room);

        //???????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //???????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        //????????????
        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this, true);

        //??????????????????
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        //???????????????
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

                initDialog();
            }

            @Override
            public void isCancel() {

            }
        });

        Intent bindIntent = new Intent(this,LocationService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);

        //??????Alarm?????????
        Intent intent = new Intent("LOCATION_CLOCK");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
// ????????????????????????
        long second = 15 * 1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), second, pendingIntent);

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
        if(locationBinder != null) {
            unbindService(connection);
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //???activity??????onSaveInstanceState?????????mMapView.onSaveInstanceState (outState)??????????????????????????????
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    //??????????????????
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

    //???????????????
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

    //????????????
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

    //????????????
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_1) {
            if (isStart) {
                bt_start.setText("??????");
                recordTime = SystemClock.elapsedRealtime() - ch_1.getBase();
                ch_1.stop();
                pauseLocation();
                myProgressButton.setVisibility(View.VISIBLE);
                isStart = false;
            } else {
                bt_start.setText("??????");
                ch_1.setBase(SystemClock.elapsedRealtime() - recordTime);
                ch_1.start();
                startUpLocation();
                myProgressButton.setVisibility(View.INVISIBLE);
                isStart = true;
            }
        }
    }


    //???????????????
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

    //???????????????????????????
    private void initPolyline() {
        polylineOptions = new PolylineOptions();
        polylineOptions.color(getResources().getColor(R.color.sport_point_write));
        polylineOptions.width(10f);
        polylineOptions.useGradient(true);

        pathSmoothTool = new PathSmoothTool();
        pathSmoothTool.setIntensity(2);
    }

    //?????????dialog
    private void initDialog() {
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(this);
        alterDialog.setCancelable(false);
        alterDialog.setTitle("???????????????????????????????????????");
        alterDialog.setNegativeButton("????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                finish();
            }
        });
        alterDialog.setPositiveButton("????????????", new DialogInterface.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Map<String, String> map = new HashMap<>();
                map.put("runTime", dbRecord.getRunTime());
                map.put("runWhen", dbRecord.getRunWhen());
                map.put("distance", dbRecord.getDistance());
                map.put("uid", "1");
                UploadUtil util = new UploadUtil();
                ApiService postService = util.getPostService();
                postService.sport_postCall("run/addRunRecord", map).enqueue(new Callback<BaseResponse<String>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                        if (response.body() == null) {
                            DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                            Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().isSuccess()) {
                            Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                        } else {
                            DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                        DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                        Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                    }
                });
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

    //???????????????
    private void setUpMap() {

        aMap.setLocationSource(locationSource);


        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//??????????????????????????????myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????1???1???????????????????????????myLocationType????????????????????????????????????
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.strokeWidth(0); //???????????????
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.sport_point));
        //???????????????
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// ???????????????????????????
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// ???????????????????????????
        myLocationStyle.showMyLocation(true);//????????????????????????
        aMap.setMyLocationStyle(myLocationStyle);//?????????????????????Style
        aMap.setMyLocationEnabled(true);// ?????????true?????????????????????????????????false??????????????????????????????????????????????????????false???

        //????????????
        //aMap.setOnMyLocationChangeListener(this);

        //??????????????????
        aMap.setMinZoomLevel(16);
        aMap.setMaxZoomLevel(18);
        //??????Logo,??????logo????????????
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
    }

    private LocationSource locationSource = new LocationSource() {
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
     * ??????????????????
     *
     * @param aMapLocation ???????????????
     */
    private final AMapLocationListener aMapLocationListener = aMapLocation -> {
        if (null == aMapLocation)
            return;
        if (aMapLocation.getErrorCode() == 0) {
            //????????????
            updateLocation(aMapLocation);
        }
    };


    //????????????
    @SuppressLint("SetTextI18n")
    private void updateLocation(Location location) {
        if (location.getLatitude() == 0 && location.getLongitude() == 0) {
            return;
        }
        pathRecord.addLaLng(new LatLng(location.getLatitude(), location.getLongitude()));
        distance = getDistance(pathRecord.getPathLinePoints());
        pathRecord.setDistance((float) distance / 1000f);
        //????????????
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
        //??????????????????
        sportLatLngs = new ArrayList<>(pathSmoothTool.pathOptimize(pathRecord.getPathLinePoints()));

        if (!sportLatLngs.isEmpty()) {
            if (mListener != null)
                mListener.onLocationChanged(location);// ?????????????????????
            polylineOptions.add(sportLatLngs.get(sportLatLngs.size() - 1));
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
        }
        aMap.addPolyline(polylineOptions);
    }


    //??????
    private ServiceConnection connection = new ServiceConnection() {	//????????????ServiceConnection?????????????????????????????????????????????????????????????????????????????????????????????????????????
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            locationBinder = (LocationService.myBind) iBinder;
            locationBinder.start(aMapLocationListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    //????????????
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
                            Toast.makeText(Sport_Activity_OutRoom.this, "", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                }
                toLocate();
        }
    }
}