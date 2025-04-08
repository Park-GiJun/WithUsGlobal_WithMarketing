package com.gijun.withusglobal.blogger.repository

import com.gijun.withusglobal.blogger.domain.Blogger
import com.gijun.withusglobal.common.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BloggerRepository : JpaRepository<Blogger, Long> {
    fun findByUser(user: User): Optional<Blogger>
    fun existsByBlogUrl(blogUrl: String): Boolean
}
