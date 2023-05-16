package com.github.kimleepark2.common.exception.entities.user

import com.github.kimleepark2.common.exception.BasicException

class UserLaundryInfoNotExist : BasicException(400, "사용자의 세탁소 정보가 지정되어있지 않습니다.")