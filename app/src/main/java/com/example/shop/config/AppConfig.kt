package com.example.shop.config

object AppConfig {

    const val APP_NAME = "FoodExpress"

    const val PRIMARY_COLOR = "#FF5722"

    const val CURRENCY = "₹"

    const val SUPPORT_EMAIL =
        "support@foodexpress.com"

    const val PHONE =
        "+91 9999999999"

    val theme = AppThemeConfig(
        appName = APP_NAME,
        currency = CURRENCY,
        supportEmail = SUPPORT_EMAIL,
        supportPhone = PHONE
    )
}