package com.zwg.statusbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zwg.immersionstatusbar.StatusBarUtil

/**
 * <pre>
 * author : zwg
 * e-mail : longzwg133@163.com
 * time : 2020/07/04
 * desc : baseActivity基类
 * version: 1.0
 * </pre>
 */
open abstract class BaseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initLayout())
        setStatusBar()
        initData()
    }

    /**
     * 设置通用沉浸式状态栏
     * @param activity 上下文
     * @param isAddPadding 是否预留状态栏高度
     * @param isTextColor 状态栏字体颜色是否加深
     * @param color 状态栏颜色
     */
    fun setStatusBar() {
        StatusBarUtil.setStatusBar(this, isAddPadding = true, isTextColor = true, color = null)
    }

    /**
     * 设置通用沉浸式状态栏
     * @param activity 上下文
     * @param isAddPadding 是否预留状态栏高度
     * @param isTextColor 状态栏字体颜色是否加深
     * @param color 状态栏颜色
     */
    fun setStatusBar(isAddPadding:Boolean,isTextColor:Boolean,color:Any?) {
        StatusBarUtil.setStatusBar(this, isAddPadding = isAddPadding, isTextColor = isTextColor, color = color)
    }

    /**
     * 设置图片状态栏
     */
    fun setImageStatusBar() {
        StatusBarUtil.setImageStatusBar(this)
    }

    /**
     * 初始化布局
     * @return 布局id
     */
    protected abstract fun initLayout(): Int

    protected abstract fun initData()
}