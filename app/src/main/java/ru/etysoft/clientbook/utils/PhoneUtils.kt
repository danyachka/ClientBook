package ru.etysoft.clientbook.utils

object PhoneUtils {

    fun formatPhoneNumberStringJ(s: String): String = formatPhoneNumberString(s)
}

fun formatPhoneNumberString(s: String): String {
    if (s.length < 10 || s.length > 12) return s

    val countryCode = s.substring(0, s.length - 10)
    val number = s.substring(s.length - 10)

    return "${if (countryCode.isNotEmpty()) "$countryCode " else ""}${number.substring(0, 3)} ${number.substring(3, 6)}-${number.substring(6, 8)}-${number.substring(8)}"
}