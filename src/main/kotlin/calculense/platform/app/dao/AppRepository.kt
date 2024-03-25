package calculense.platform.app.dao

import calculense.platform.app.model.App
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface AppRepository:JpaRepository<App,Long?> {
    fun findAppByName(name:String):App
}