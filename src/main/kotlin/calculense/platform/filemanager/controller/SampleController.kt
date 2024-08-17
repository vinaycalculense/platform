package calculense.platform.filemanager.controller

import calculense.platform.auth.annotation.RequiresRole
import calculense.platform.filemanager.model.Sample
import calculense.platform.filemanager.model.SampleFile
import calculense.platform.filemanager.service.IFileUploadService
import calculense.platform.filemanager.service.ISampleService
import calculense.platform.filemanager.service.SampleService
import calculense.platform.model.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("sample")
@CrossOrigin(origins = ["*"])
class SampleController {

    @Autowired
    lateinit var sampleService: ISampleService
    @PostMapping
    @RequiresRole(["admin"])
    fun saveSample(@RequestBody sample: Sample): ResponseEntity<Response<Sample>> {
        return ResponseEntity(
            Response(data=sampleService.saveSample(sample), message = "request processed", error = false),
            HttpStatus.CREATED)
    }

    @GetMapping
    @RequiresRole(["user","admin"])
    fun getSample(@RequestParam("userId") userId:Long,@RequestParam("category") category:String): ResponseEntity<Response<List<Sample>>> {
        return ResponseEntity(
            Response(data=sampleService.getSamples(userId,category), message = "request processed", error = false),
            HttpStatus.OK)
    }
}