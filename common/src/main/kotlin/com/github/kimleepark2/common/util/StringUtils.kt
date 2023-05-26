package kr.co.jsol.core.util

/**
 * string pascal case to camel case
 * ex : PascalCase -> pascalCase
 */
fun String.pascalToCamel(): String {
    return if (isNotEmpty()) {
        this[0].lowercaseChar() + substring(1)
    } else {
        this
    }
}