package com.example.login.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.baselibs.net.BaseResponse;
import com.example.baselibs.net.network.UploadUtil;
import com.example.baselibs.net.network.bean.User;
import com.example.login.ui.Login_MainActivity;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModel_MainActivity extends ViewModel {

    private final MutableLiveData<person> people = new MutableLiveData<>(new person());
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<String> msg = new MutableLiveData<>("");

    public LiveData<String> getMsg() {
        return msg;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<person> getPeople() {
        return people;
    }

    public void setPhone(String phone) {
        person people = this.people.getValue();
        assert people != null;
        people.phone = phone;
        this.people.setValue(people);
    }

    public void setCode(String code) {
        person people = this.people.getValue();
        assert people != null;
        people.code = code;
        this.people.setValue(people);
    }

    //验证验证码
    public void checkCode() {
        if (people.getValue() == null) {
            return;
        }
        try {
            UploadUtil.loginPostService().login_postCheckCode("login/byCode", people.getValue().phone, people.getValue().code).enqueue(new Callback<BaseResponse<User>>() {
                @Override
                public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                    assert response.body() != null;
                    msg.setValue(response.body().getMsg());
                    User data = response.body().getData();
                    if (data != null) {
                        UploadUtil.uid = data.getUid();
                        data.setHeadUrl("");
//                        UploadUtil.user = data;
                        EventBus.getDefault().postSticky(data);
                        Log.d("TAG", "onResponse11: " + data);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                    user.setValue(null);
                }
            });
        } catch (Exception ignored) {

        }
    }

    //获取验证码
    public void getCode() {
        if (people.getValue() == null) {
            return;
        }
        try {
            UploadUtil.loginPostService().login_postGetCode("register/codeSend", people.getValue().phone).enqueue(new Callback<BaseResponse<Boolean>>() {
                @Override
                public void onResponse(Call<BaseResponse<Boolean>> call, Response<BaseResponse<Boolean>> response) {
                    assert response.body() != null;
                    Boolean data = response.body().getData();
                    Log.d("TAG", "onResponse: " + data);
                }

                @Override
                public void onFailure(Call<BaseResponse<Boolean>> call, Throwable t) {
                }
            });
        } catch (Exception ignored) {

        }

    }

    public static class person {
        private String phone = "";
        private String code = "";

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public boolean isStart() {
            if (!"".equals(phone) && !"".equals(code)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
