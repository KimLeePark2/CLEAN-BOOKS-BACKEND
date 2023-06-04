package com.github.kimleepark2.common.util

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EnumValidator : ConstraintValidator<EnumValidation, Enum<*>> {
    lateinit var annotation: EnumValidation
    override fun initialize(constraintAnnotation: EnumValidation) {
        annotation = constraintAnnotation
    }

    override fun isValid(value: Enum<*>, context: ConstraintValidatorContext): Boolean {
        var result = false
        val enumValues: Array<out Enum<*>>? = annotation.enumClass.java.enumConstants
        if (enumValues != null) {
            for (enumValue in enumValues) {
                if (value === enumValue) {
                    result = true
                    break
                }
            }
        }
        return result
    }
}