package com.zwg.immersionstatusbar

import android.os.Build
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * <pre>
 * author : zwg
 * e-mail : longzwg133@163.com
 * time : 2020/07/04
 * desc : 判断系手机统工具类
 * version: 1.0
 * </pre>
 */
object OSUtil {

    val ROM_MIUI = "MIUI"
    val ROM_EMUI = "EMUI"
    val ROM_FLYME = "FLYME"
    val ROM_OPPO = "OPPO"
    val ROM_SMARTISAN = "SMARTISAN"
    val ROM_VIVO = "VIVO"
    val ROM_QIKU = "QIKU"

    private val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
    private val KEY_VERSION_EMUI = "ro.build.version.emui"
    private val KEY_VERSION_OPPO = "ro.build.version.opporom"
    private val KEY_VERSION_SMARTISAN = "ro.smartisan.version"
    private val KEY_VERSION_VIVO = "ro.vivo.os.version"

    private var romName: String? = null
    private var romVersion: String? = null

    /**
     * 检测系统产商型号
     */
    private fun checkRom(rom: String): Boolean {
        romName?.let {
            return it == rom
        }
        when {
            romVersion == getProperty(KEY_VERSION_MIUI) -> {
                romName = ROM_MIUI
            }
            romVersion == getProperty(KEY_VERSION_EMUI) -> {
                romName = ROM_EMUI
            }
            romVersion == getProperty(KEY_VERSION_OPPO) -> {
                romName = ROM_OPPO
            }
            romVersion == getProperty(KEY_VERSION_VIVO) -> {
                romName = ROM_VIVO
            }
            romVersion == getProperty(KEY_VERSION_SMARTISAN) -> {
                romName = ROM_SMARTISAN
            }
            else -> {
                romVersion = Build.DISPLAY
                if (romVersion?.toUpperCase()?.contains(ROM_FLYME) == true) {
                    romName = ROM_FLYME
                } else {
                    romVersion = Build.UNKNOWN
                    romName = Build.MANUFACTURER.toUpperCase()
                }
            }
        }
        return romName == rom
    }

    /**
     * 获取系统版本名称
     * @param name
     */
    private fun getProperty(name: String): String? {
        var line: String? = null
        var bufferReader: BufferedReader? = null
        try {
            var process = Runtime.getRuntime().exec("getprop $name")
            bufferReader = BufferedReader(InputStreamReader(process.inputStream), 1024)
            line = bufferReader.readLine()
            bufferReader.close()
        } catch (e: Exception) {
            return null
        } finally {
            try {
                bufferReader?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return line
    }

    /**
     * 是否是Emui
     */
    fun isEmui(): Boolean {
        return checkRom(ROM_MIUI)
    }

    /**
     * 是否是miui
     */
    fun isMiui(): Boolean {
        return checkRom(ROM_MIUI)
    }


    /**
     * 是否是vivo
     */
    fun isVivo(): Boolean {
        return checkRom(ROM_VIVO)
    }

    /**
     * 是否是oppo
     */
    fun isOppo(): Boolean {
        return checkRom(ROM_OPPO)
    }


    /**
     * 是否是oppo
     */
    fun isFlyme(): Boolean {
        return checkRom(ROM_FLYME)
    }

    /**
     * 是否是360
     */
    fun is360(): Boolean {
        return checkRom("360")
    }

    /**
     * 是否是smartisan
     */
    fun isSmartisan(): Boolean {
        return checkRom(ROM_SMARTISAN)
    }


    fun getRomName(): String? {
        romName?.let {
            checkRom("")
        }
        return romName
    }

    fun getRomVersion(): String? {
        romVersion?.let {
            checkRom("")
        }
        return romVersion
    }
}