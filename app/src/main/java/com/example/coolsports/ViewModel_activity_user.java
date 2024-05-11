package com.example.coolsports;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.baselibs.net.BaseResponse;
import com.example.baselibs.net.network.UploadUtil;
import com.example.baselibs.net.network.bean.User;
import com.example.baselibs.room.baseroom.AppDataBase;
import com.example.baselibs.room.baseroom.UserDataBase;
import com.example.login.viewModel.ViewModel_register;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModel_activity_user extends ViewModel {

    private MutableLiveData<User> user = new MutableLiveData<>();
    private User mUser = null;

    private MutableLiveData<String> finish = new MutableLiveData<>("");
    private MutableLiveData<String> password = new MutableLiveData<>("");

    public LiveData<String> getFinish() {
        return finish;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void setUser(User user) {
        user.setPassword("");
        mUser = user;
        this.user.setValue(mUser);
    }

    public void setPassword2(String password) {
        this.password.setValue(password);
    }

    public LiveData<String> getPassword2() {
        return password;
    }

    public boolean checkMessage() {
        return !("".equals(mUser.getAge()) || "".equals(mUser.getEmail()) || "".equals(mUser.getGender()) ||
                "".equals(mUser.getPhone()) || "".equals(mUser.getUserName()) || "".equals(mUser.getPassword())
                || "".equals(password.getValue()));
    }

    public void setName(String name) {
        mUser.setUserName(name);
        user.setValue(mUser);
    }

    public void setPhone(String phone) {
        mUser.setPhone(phone);
        user.setValue(mUser);
    }

    public void setAge(String age) {
        mUser.setAge(age);
        user.setValue(mUser);
    }

    public void setGender(String gender) {
        mUser.setGender(gender);
        user.setValue(mUser);
    }


    public void setEmail(String email) {
        mUser.setEmail(email);
        user.setValue(mUser);
    }

    public void setPassword(String password) {
        mUser.setPassword(password);
        user.setValue(mUser);
    }

    public String getPassword() {
        return mUser.getPassword();
    }

    public void setHeadUrl(String headUrl) {
        mUser.setHeadUrl(headUrl);
        user.setValue(mUser);
    }


    public void updateInfo(Context context) {
        EventBus.getDefault().postSticky(mUser);
        UserDataBase.getInstance(context).getUserDao().update(mUser);
       /* HashMap<String, String> map = new HashMap<>();
        map.put("age", mUser.getAge());
        map.put("headUrl", mUser.getHeadUrl());
        map.put("password", mUser.getPassword());
        map.put("userName", mUser.getUserName());
        map.put("phone", mUser.getPhone());
        map.put("gender", mUser.getGender());
        map.put("email", mUser.getEmail());
        map.put("uid", mUser.getUid());

        try {

            UploadUtil.sentPostService().login_postAddInfo("user/updateInfo", map).enqueue(new Callback<BaseResponse<Boolean>>() {
                @Override
                public void onResponse(Call<BaseResponse<Boolean>> call, Response<BaseResponse<Boolean>> response) {
                    Log.d("TAG", "onResponse: " + response.body().toString());
                    Log.d("TAG", "onResponse: " + mUser.toString());
                    if (response.body() != null) {
                        finish.setValue(response.body().getMsg());
                        EventBus.getDefault().postSticky(mUser);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<Boolean>> call, Throwable t) {
                    Log.d("TAG", "onFailure: " + t.toString());
                }
            });

        } catch (Exception ignored) {

        }*/

    }
}
