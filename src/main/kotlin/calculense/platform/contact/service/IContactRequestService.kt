package calculense.platform.contact.service

import calculense.platform.contact.model.ContactRequest

interface IContactRequestService {

    fun saveContactRequest(contactRequest: ContactRequest): ContactRequest
}