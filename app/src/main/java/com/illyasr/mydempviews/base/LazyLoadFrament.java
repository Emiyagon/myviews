package com.illyasr.mydempviews.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Administrator on 2018/11/29.
 * user: Administrator
 * date: 2018/11/29
 * time; 10:00
 * name: 懒加载Frament
 */
public abstract class LazyLoadFrament extends Fragment {
  protected boolean isInit = false;//视图是否已经初始化
  private boolean isLoad = false;//是否开始加载数据
  private boolean isRepeat = true;//是否允许重复加载当前frament页面数据
  private View view;
  //解决fragment奔溃时重叠问题
  private final String isFragmentHide = "STATE_SAVE_IS_HIDDEN";

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    init(inflater, container);
    isInit = true;
    /**初始化的时候去加载数据**/
    loadData();
    return view;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    //当发生保存时，额外保存当前的fragment的显示状态
    outState.putBoolean(isFragmentHide, isHidden());
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //当恢复时，让app崩溃时处于隐藏状态的fragment恢复隐藏，处于显示状态的继续显示
    if (savedInstanceState != null) {
      boolean isHidden = savedInstanceState.getBoolean(isFragmentHide);
      FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
      if (isHidden) {
        //如果已经隐藏,则就隐藏
        transaction.hide(this);
      } else {
        transaction.show(this);
      }
      transaction.commit();
    }
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
  }

  /**
   * 视图是否已经对用户可见，系统的方法
   */
  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if(isRepeat()){
      loadData();
    }
  }

  private void init(LayoutInflater inflater, ViewGroup container) {
    view = inflater.inflate(setLayoutId(), container, false);
    //如果使用了黄油刀可以在这里添加绑定事件
  }

  /**
   * 是否可以加载数据
   * 可以加载数据的条件：
   * 1.视图已经初始化
   * 2.视图对用户可见
   */
  private void loadData() {
    if (!isInit) {
      //视图是否初始化
      return;
    }
    if (getUserVisibleHint()) {
      //视图已经可见，进行数据加载
      lazyLoad();
      isLoad = true;//开始加载数据
    } else {
      //处于不可见状态，如果数据正在加载中，则进行中断网络请求之类的数据操作
      if (isLoad) {
        stopLoad();
      }
    }

  }
  public boolean isRepeat() {
    return isRepeat;
  }

  public void setRepeat(boolean repeat) {
    isRepeat = repeat;
  }
  /**
   * 设置Fragment要显示的布局
   *
   * @return 布局的layoutId
   */
//    protected abstract int setContentView();
  protected abstract int setLayoutId();
  /**
   * 当视图初始化并且对用户可见的时候去真正的加载数据
   */
  protected abstract void lazyLoad();

  /**
   * 当视图处于用户不可见状态且处于数据加载状态，则进行取消数据加载
   */
  protected abstract void stopLoad();

  /**
   * 控件初始化
   */
  protected abstract void initView( );
}
