package com.github.kimleepark2.domain.entity.user

// fun User.isJsolRole(): Boolean {
//    return this.role == UserRoleType.ROLE_JSOL
// }
//
// fun User.isNotJsolRole(): Boolean {
//    return isJsolRole().not()
// }
//
// fun User.isLaundryRole(): Boolean {
//    return this.role == UserRoleType.ROLE_LAUNDRY
// }
//
// fun User.isNotLaundryRole(): Boolean {
//    return isLaundryRole().not() && isNotJsolRole()
// }
//
// fun User.isClientRole(): Boolean {
//    return this.role == UserRoleType.ROLE_CLIENT
// }
//
// fun User.isNotClientRole(): Boolean {
//    return isClientRole().not() && isNotJsolRole()
// }
//
// fun User.isDepartmentRole(): Boolean {
//    return this.role == UserRoleType.ROLE_DEPARTMENT
// }
//
// fun User.isNotDepartmentRole(): Boolean {
//    return isDepartmentRole().not() && isNotJsolRole()
// }
//
// fun User.isUserRole(): Boolean {
//    return this.role == UserRoleType.ROLE_USER
// }
//
// fun User.isNotUserRole(): Boolean {
//    return isUserRole().not() && isNotJsolRole()
// }
//
// fun User.isClientManagerRole(): Boolean {
//    return isClientRole() || isDepartmentRole()
// }
//
// fun User.isNotClientManagerRole(): Boolean {
//    return isClientManagerRole().not() && isNotJsolRole()
// }