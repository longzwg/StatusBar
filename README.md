# StatusBar
沉浸式状态栏
How to use To get a Git project into your build:
Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
Step 2. Add the dependency
dependencies {
        implementation 'com.github.longzwg:StatusBar:${latest_version}'
}
Step 3. Use
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
