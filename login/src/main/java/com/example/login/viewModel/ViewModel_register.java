package com.example.login.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.baselibs.net.BaseResponse;
import com.example.baselibs.net.network.UploadUtil;
import com.example.baselibs.net.network.bean.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModel_register extends ViewModel {

    private MutableLiveData<User> user = new MutableLiveData<>(new User());
    private final User mUser = user.getValue();

    private MutableLiveData<Boolean> finish = new MutableLiveData<>(false);
    private MutableLiveData<String> password2 = new MutableLiveData<>();

    public LiveData<Boolean> getFinish() {
        return finish;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<String> getPassword2() {
        return password2;
    }

    public boolean checkMessage() {
        return !("".equals(mUser.getAge()) || "".equals(mUser.getEmail())  || "".equals(mUser.getGender()) ||
                "".equals(mUser.getPhone()) || "".equals(mUser.getUserName()) || "".equals(mUser.getPassword()) || "".equals(password2.getValue()));
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

    public void setPassword2(String password2) {
        this.password2.setValue(password2);
    }

    public String getPassword() {
        return mUser.getPassword();
    }

    public void setHeadUrl(String headUrl) {
        mUser.setHeadUrl(headUrl);
        user.setValue(mUser);
    }

    public void addInfo(){
        HashMap<String,String> map = new HashMap<>();
        map.put("age",mUser.getAge());
        map.put("headUrl",mUser.getHeadUrl());
        map.put("password",mUser.getPassword());
        map.put("userName",mUser.getUserName());
        map.put("phone",mUser.getPhone());
        map.put("gender",mUser.getGender());
        map.put("email",mUser.getEmail());
        map.put("uid","");

       try{
           UploadUtil.loginPostService().login_postAddInfo("register/addInfo",map).enqueue(new Callback<BaseResponse<Boolean>>() {
               @Override
               public void onResponse(Call<BaseResponse<Boolean>> call, Response<BaseResponse<Boolean>> response) {
                   assert response.body() != null;
                   finish.setValue(response.body().getData());
               }

               @Override
               public void onFailure(Call<BaseResponse<Boolean>> call, Throwable t) {

               }
           });
       }catch (Exception ignored) {

       }
    }
}
