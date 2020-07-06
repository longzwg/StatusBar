package com.zwg.immersionstatusbar

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

enum class StatusBarType {
    TYPE_MIUI,
    TYPE_FLYME,
    TYPE_M
}

/**
 * Created by longz
 * On 2019/11/21
 * Description 设置手机沉浸式状态栏
 */
object StatusBarUtil {
    val TYPE_MIUI = 0
    val TYPE_FLYME = 1
    val TYPE_M = 3//6.0

    /**
     * 设置沉浸式
     * @param activity 上下文
     * @param isAddPadding 是否添加状态栏
     * @param isTextColor 状态栏字体颜色切换(true为浅色,false为深色)
     * @param color 状态栏背景色设置(默认白色)
     */
    fun setStatusBar(activity: Activity, isAddPadding: Boolean, isTextColor: Boolean, color: Any?) {
        //这里注意下 因为在评论区发现有网友调用setRootViewFitsSystemWindows 里面 winContent.getChildCount()=0 导致代码无法继续
        //是因为你需要在setContentView之后才可以调用 setRootViewFitsSystemWindows
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        setRootViewFitsSystemWindows(activity, isAddPadding)
        //设置状态栏字体颜色深浅
        setStatusBarDarkTheme(activity, isTextColor)
        //设置状态栏背景颜色
        when (color) {
            is Int -> {
                setStatusBarColor(activity,color)
            }
            is String -> {
                setStatusBarColor(activity,Color.parseColor(color))
            }
            else -> {
                setStatusBarColor(activity,Color.parseColor("#FFFFFF"))
            }
        }
    }

    /**
     * 图片背景设置沉浸式
     * @param activity    上下文
     * @param isTextColor 状态栏字体颜色切换(false为浅色,true为深色)
     */
    fun setImageStatusBar(activity: Activity){
        //这里注意下 因为在评论区发现有网友调用setRootViewFitsSystemWindows 里面 winContent.getChildCount()=0 导致代码无法继续
        //是因为你需要在setContentView之后才可以调用 setRootViewFitsSystemWindows
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        setRootViewFitsSystemWindows(activity, false)
        //设置状态栏字体颜色深浅
//        setStatusBarDarkTheme(activity, isTextColor)
        //设置状态栏透明
        setTranslucentStatus(activity)
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param colorId 颜色
     */
    private fun setStatusBarColor(activity: Activity, colorId: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.statusBarColor = colorId
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTintManager,需要先将状态栏设置为透明
            setTranslucentStatus(activity)
            val systemBarTintManager = SystemBarTintManager(activity)
            systemBarTintManager.isStatusBarTintEnabled = true//显示状态栏
            systemBarTintManager.setStatusBarTintColor(colorId)//设置状态栏颜色
        }
    }

    /**
     * 设置状态栏透明
     */
    @TargetApi(19)
    private fun setTranslucentStatus(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            val window = activity.window
            val decorView = window.decorView
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            //导航栏颜色也可以正常设置
            //window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            val attributes = window.attributes
            val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            attributes.flags = attributes.flags or flagTranslucentStatus
            //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            //attributes.flags |= flagTranslucentNavigation;
            window.attributes = attributes
        }
    }


    /**
     * 代码实现android:fitsSystemWindows
     *
     * @param activity
     */
    private fun setRootViewFitsSystemWindows(activity: Activity, fitSystemWindows: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val winContent = activity.findViewById<View>(android.R.id.content) as ViewGroup
            if (winContent.childCount > 0) {
                val rootView = winContent.getChildAt(0) as ViewGroup
                if (rootView != null) {
                    rootView.fitsSystemWindows = fitSystemWindows
                }
            }
        }

    }


    /**
     * 设置状态栏深色浅色切换
     */
    private fun setStatusBarDarkTheme(activity: Activity, dark: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setStatusBarFontIconDark(activity, StatusBarType.TYPE_M, dark)
            } else if (OSUtil.isMiui()) {
                setStatusBarFontIconDark(activity, StatusBarType.TYPE_MIUI, dark)
            } else if (OSUtil.isFlyme()) {
                setStatusBarFontIconDark(activity, StatusBarType.TYPE_FLYME, dark)
            } else {//其他情况
                return false
            }

            return true
        }
        return false
    }

    /**
     * 设置 状态栏深色浅色切换
     */
    private fun setStatusBarFontIconDark(activity: Activity, type: StatusBarType, dark: Boolean): Boolean {
        when (type) {
            StatusBarType.TYPE_MIUI -> return setMiuiUI(activity, dark)
            StatusBarType.TYPE_FLYME -> return setFlymeUI(activity, dark)
            StatusBarType.TYPE_M -> return setCommonUI(activity, dark)
            else -> return setCommonUI(activity, dark)
        }
    }

    //设置6.0 状态栏深色浅色切换
    private fun setCommonUI(activity: Activity, dark: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = activity.window.decorView
            if (decorView != null) {
                var vis = decorView.systemUiVisibility
                if (dark) {
                    vis = vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    vis = vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
                if (decorView.systemUiVisibility !== vis) {
                    decorView.systemUiVisibility = vis
                }
                return true
            }
        }
        return false

    }

    //设置Flyme 状态栏深色浅色切换
    private fun setFlymeUI(activity: Activity, dark: Boolean): Boolean {
        try {
            val window = activity.window
            val lp = window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            if (dark) {
                value = value or bit
            } else {
                value = value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            window.attributes = lp
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    //设置MIUI 状态栏深色浅色切换
    private fun setMiuiUI(activity: Activity, dark: Boolean): Boolean {
        try {
            val window = activity.window
            val clazz = activity.window.javaClass
            @SuppressLint("PrivateApi") val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField =
                clazz.getDeclaredMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            extraFlagField.isAccessible = true
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    //获取状态栏高度
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.getResources().getIdentifier(
            "status_bar_height", "dimen", "android"
        )
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun test(context: Activity) {
      /*  //这里注意下 因为在评论区发现有网友调用setRootViewFitsSystemWindows 里面 winContent.getChildCount()=0 导致代码无法继续
        //是因为你需要在setContentView之后才可以调用 setRootViewFitsSystemWindows
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(context, true)
        //StatusBarUtil.setRootViewFitsSystemWindows(context,false)   想要图片沉浸, 必须设置fitsSystemWindows=false, 以去掉padding效果
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(context)
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(context, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(context, 0x55000000)
        }*/
        //设置通用界面
        StatusBarUtil.setStatusBar(context, isAddPadding = true, isTextColor = true, color = "#2AFC04")
        //设置图片界面
        StatusBarUtil.setImageStatusBar(context)
    }
}
