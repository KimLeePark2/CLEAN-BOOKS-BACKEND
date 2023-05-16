package com.github.kimleepark2.common.exception.entities.user

import com.github.kimleepark2.common.exception.BasicException

class UserDisableException : BasicException(400, "사용 불가능한 계정 정보입니다.")
