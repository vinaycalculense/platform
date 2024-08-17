package calculense.platform.contact.controller

import calculense.platform.contact.model.ContactRequest
import calculense.platform.contact.service.IContactRequestService
import calculense.platform.model.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("contactrequest")
@CrossOrigin(origins = ["*"])
class ContactRequestController {

    @Autowired
    private lateinit var contactRequestService: IContactRequestService
    @PostMapping
    fun createApp(@RequestBody contactRequest: ContactRequest): ResponseEntity<Response<ContactRequest>> {
        return ResponseEntity(
            Response(data =contactRequestService.saveContactRequest(contactRequest =contactRequest ), message = "Contact Request Submitted.", error = false),
            HttpStatus.CREATED)
    }
}