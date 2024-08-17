package calculense.platform.contact.dao

import calculense.platform.contact.model.ContactRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ContactRequestRepository:JpaRepository<ContactRequest,Long> {
}