package com.example.sport.db;


import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DbManger {

    private static Context context;

    private DbManger() {
    }

    ;

    public static DbManger sInstance;

    public static DbManger getInstance(Context context) {
        DbManger.context = context;
        if (sInstance == null) {
            synchronized (DbManger.class) {
                if (sInstance == null) {
                    sInstance = new DbManger();
                }
            }
        }
        return sInstance;
    }

    //添加
    public Observable<Integer> insert(DbRecord dbRecord) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                AppDataBase.getInstance(context).getDao().insert(dbRecord);
                emitter.onNext(1);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //查询
    public Flowable<List<DbRecord>> getAll() {
        return AppDataBase.getInstance(context).getDao().loadAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //删除单个
    public Observable<Integer> delete(DbRecord dbRecord) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                AppDataBase.getInstance(context).getDao().delete(dbRecord);
                emitter.onNext(1);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //删除全部
    public Observable<Integer> deleteAll() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                AppDataBase.getInstance(context).getDao().deleteAll();
                emitter.onNext(1);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
