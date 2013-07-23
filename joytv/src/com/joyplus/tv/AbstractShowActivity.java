package com.joyplus.tv;

import java.util.List;

import org.json.JSONObject;

import com.androidquery.callback.AjaxStatus;
import com.joyplus.tv.entity.MovieItemData;
import com.joyplus.tv.utils.BangDanConstant;
import com.joyplus.tv.utils.JieMianConstant;
import com.joyplus.tv.utils.MyKeyEventKey;

import android.app.Activity;
import android.view.View;

public abstract class AbstractShowActivity extends Activity implements
		View.OnKeyListener, MyKeyEventKey, BangDanConstant, JieMianConstant,
		View.OnClickListener, View.OnFocusChangeListener {

	protected void initActivity() {

		initView();
		initViewListener();
		initViewState();
		initLists();
		clearLists();
	}

	protected abstract void initView();// 初始化界面基本组件

	protected abstract void initViewListener();// 初始化界面一些需要添加监听的组件

	protected abstract void initViewState();// 初始化一些组件的状态

	protected abstract void clearLists();// 清除Lists

	protected abstract void initLists();// 初始化Lists数组

	protected abstract void notifyAdapter(List<MovieItemData> list);// 刷新adapter中数据，并初始化基本状态

	protected abstract void filterVideoSource(String[] choice);// 通过选择条件，获取相应视频资源

	protected abstract void getQuan10Data(String url);// 获取推荐的10部影片

	protected abstract void getQuanbuData(String url);// 获取全部分类的影片

	protected abstract void getUnQuanbuData(String url);// 获取非全部分类的影片,过滤的除外

	protected abstract void getFilterData(String url);// 获取过滤的影片

	protected abstract void getServiceData(String url, String interfaceName);// 通过不同名字调用服务

	public abstract void initQuan10ServiceData(String url, JSONObject json,
			AjaxStatus status);// 推荐的10部影片服务

	public abstract void initQuanbuServiceData(String url, JSONObject json,
			AjaxStatus status);// 全部分类的影片服务

	public abstract void initUnQuanbuServiceData(String url, JSONObject json,
			AjaxStatus status);// 非全部分类的影片,过滤的除外服务

	public abstract void initFilerServiceData(String url, JSONObject json,
			AjaxStatus status);// 非全部分类的影片,过滤的除外服务
	
	protected abstract void refreshAdpter(List<MovieItemData> list);
	
	public abstract void initMoreFilerServiceData(String url, JSONObject json,
			AjaxStatus status);
	
	protected abstract void getMoreFilterData(String url);
	
	public abstract void initMoreBangDanServiceData(String url, JSONObject json,
			AjaxStatus status);
	
	protected abstract void getMoreBangDanData(String url);
	
	protected abstract void cachePlay(int index, int pageNum);
	
	protected abstract void filterPopWindowShow();
	
	protected abstract void resetGvActive();
	
	protected abstract void initFirstFloatView(int position,View view);

}
