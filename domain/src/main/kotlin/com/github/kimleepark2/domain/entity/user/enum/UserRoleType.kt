package com.github.kimleepark2.domain.entity.user.enum

enum class UserRoleType {
    ROLE_ADMIN, // 루트(서버 상태 관리자)
    ROLE_MANAGER, // 매니저(서비스 운영 관리자)
    ROLE_USER, // 일반 사용자
}
