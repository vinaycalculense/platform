package calculense.platform.auth.dao

import calculense.platform.auth.model.CreditLogs
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CreditLogsRepository:JpaRepository<CreditLogs,Long?> {
}