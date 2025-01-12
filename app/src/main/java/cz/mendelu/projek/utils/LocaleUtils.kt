package cz.mendelu.projek.utils

import android.content.Context
import android.content.res.Configuration
import cz.mendelu.projek.constants.Languages
import java.util.Locale

fun Context.setLocale(language: Languages): Context{
    val locale = when(language){
        Languages.CZ -> Locale("cs", "CZ")
        Languages.EN -> Locale.ENGLISH
    }
    Locale.setDefault(locale)
    val config = Configuration()
    config.setLocale(locale)
    return createConfigurationContext(config)
}