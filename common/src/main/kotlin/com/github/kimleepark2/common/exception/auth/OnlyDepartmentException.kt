package com.github.kimleepark2.common.exception.auth

import com.github.kimleepark2.common.exception.UnauthorizedException

class OnlyDepartmentException : UnauthorizedException("부서 관리자만 가능합니다.")