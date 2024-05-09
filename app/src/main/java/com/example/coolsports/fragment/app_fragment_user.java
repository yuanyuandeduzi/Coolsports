package com.example.coolsports.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.BuildConfig;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.baselibs.net.network.UploadUtil;
import com.example.baselibs.net.network.bean.User;
import com.example.coolsports.databinding.AppFragmentUserBinding;
import com.example.coolsports.ui.App_MainActivity;
import com.example.coolsports.ui.User_Activity_Alter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@Route(path = "/user/user1")
public class app_fragment_user extends Fragment {

    private AppFragmentUserBinding viewBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = AppFragmentUserBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UploadUtil.isLogin.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    viewBinding.reLogin.setEnabled(false);
                    viewBinding.reLogin.setText("已登录");
                } else {
                    viewBinding.reLogin.setEnabled(true);
                }
            }
        });

        viewBinding.reLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/login/login1").navigation();
            }
        });

        initMenu(UploadUtil.user);

        viewBinding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Boolean.TRUE.equals(UploadUtil.isLogin.getValue())) {
                    ARouter.getInstance().build("/user/user2").navigation();
                }else {
                    App_MainActivity.sendToast("请您先登录！！");
                }
            }
        });

    }

    private void initMenu(User user) {
        Menu menu = viewBinding.myNavigation.getMenu();
        if(user != null) {
            menu.getItem(0).setTitle("用户：" + user.getUserName());
            menu.getItem(1).setTitle("手机号：" + user.getPhone());
            menu.getItem(2).setTitle("年龄：" + user.getAge());
            menu.getItem(3).setTitle("性别：" + user.getGender());
            menu.getItem(4).setTitle("邮箱：" + user.getEmail());
            menu.getItem(5).setTitle("介绍：宇宙超级无敌大帅锅！");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isDebug()) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(getActivity().getApplication());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }

}
