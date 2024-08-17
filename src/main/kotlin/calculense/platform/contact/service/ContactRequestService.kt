package calculense.platform.contact.service

import calculense.platform.contact.dao.ContactRequestRepository
import calculense.platform.contact.model.ContactRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ContactRequestService:IContactRequestService {

    @Autowired
    lateinit var contactRequestRepository: ContactRequestRepository

    override fun saveContactRequest(contactRequest: ContactRequest):ContactRequest {
        return contactRequestRepository.save(contactRequest)
    }
}