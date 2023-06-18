package com.github.kimleepark2.domain.entity.file

import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository : JpaRepository<File, Long>{
    fun findByPath(path: String): File?
}