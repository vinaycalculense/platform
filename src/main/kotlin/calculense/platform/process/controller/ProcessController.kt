package calculense.platform.process.controller

import calculense.platform.auth.annotation.Paid
import calculense.platform.auth.annotation.RequiresRole
import calculense.platform.model.Response
import calculense.platform.process.service.ProcessExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("process")
class ProcessController {

    @Autowired
    lateinit var processExecutor: ProcessExecutor
    @PostMapping
    @RequiresRole(type = ["user","admin"])
    @Paid
    fun processRequest(@RequestParam("request_id") requestId:String ): ResponseEntity<Response<String>> {
        processExecutor.process(requestId)
        return ResponseEntity(Response(data = requestId, message = "Request Submitted.", error = false),
            HttpStatus.CREATED)

    }
}