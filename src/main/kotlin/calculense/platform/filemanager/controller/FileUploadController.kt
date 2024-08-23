package calculense.platform.filemanager.controller

import calculense.platform.auth.annotation.RequiresRole
import calculense.platform.filemanager.model.CopyRequest
import calculense.platform.filemanager.model.FileUploadRequestDTO
import calculense.platform.filemanager.model.FileUploadResponseDTO
import calculense.platform.filemanager.service.IFileUploadService
import calculense.platform.model.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("fileupload")
@CrossOrigin(origins = ["*"])
class FileUploadController {
    @Autowired
    lateinit var fileUploadService: IFileUploadService
    @PostMapping
    @RequiresRole(["user","admin"])
    fun processUpload(@RequestBody fileUploadRequestDTO: FileUploadRequestDTO): ResponseEntity<Response<FileUploadResponseDTO>> {
        return ResponseEntity(Response(data=fileUploadService.processUpload(fileUploadRequestDTO), message = "request processed", error = false),HttpStatus.CREATED)
    }

    @PostMapping("/copy")
    @RequiresRole(["user","admin"])
    fun getFile(@RequestBody copyRequest: CopyRequest): ResponseEntity<Response<String>> {
        fileUploadService.copyImage(copyRequest.getUrl,copyRequest.putUrl)
        return ResponseEntity(Response(data="File Copied", message = "request processed", error = false),HttpStatus.CREATED)
    }


}