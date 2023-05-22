package com.github.kimleepark2.common.exception.entities.user

import com.github.kimleepark2.common.exception.BasicException

class UserAlreadyExistException : BasicException(409, "이미 회원가입된 아이디입니다.")
