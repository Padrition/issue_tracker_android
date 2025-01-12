package cz.mendelu.projek.utils

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import java.util.Locale

object LocaleManager {
    fun updateLocale(context: Context, locale: Locale): Context {
        val config = Configuration(context.resources.configuration)
        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        config.setLocales(localeList)

        return context.createConfigurationContext(config)
    }
}
