package com.example.sport.util;

import static android.content.Context.POWER_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.baselibs.net.BaseResponse;
import com.example.baselibs.net.network.ApiService;
import com.example.baselibs.net.network.UploadUtil;
import com.example.sport.db.DbManger;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogUtils {

    private boolean hasIgnored = false;
    public void initDialog(Context mContext) {
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(mContext);
        alterDialog.setCancelable(false);
        alterDialog.setTitle(" 是否允许应用始终在后台运行？");
        alterDialog.setMessage("允许程序始终在后台运行可能会缩短电池的续航时间\n\n您以后可以在\"设置\">\"应用和通知\"中更改此设置");
        alterDialog.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, "请同意!!", Toast.LENGTH_SHORT).show();
            }
        });
        alterDialog.setPositiveButton("允许", new DialogInterface.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PowerManager powerManager = (PowerManager) mContext.getSystemService(POWER_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hasIgnored = powerManager.isIgnoringBatteryOptimizations(mContext.getPackageName());
                    //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
                    if (!hasIgnored) {
                        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                        mContext.startActivity(intent);
                    }
                    Log.d("TAg", "onClick: " + hasIgnored);
                }
            }
        });
        if (!hasIgnored) {
            alterDialog.show();
        }
    }

}
