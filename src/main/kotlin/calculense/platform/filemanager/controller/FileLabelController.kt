package calculense.platform.filemanager.controller

import calculense.platform.auth.annotation.RequiresRole
import calculense.platform.filemanager.model.FileLabel
import calculense.platform.filemanager.service.IFileLabelService
import calculense.platform.model.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("filelabel")
@CrossOrigin(origins = ["*"])
class FileLabelController {

    @Autowired
    lateinit var fileLabelService: IFileLabelService

    @PostMapping
    @RequiresRole(["admin"])
    fun createFileLabel(@RequestBody fileLabel: FileLabel):ResponseEntity<Response<FileLabel>>{
        return ResponseEntity(Response(data=fileLabelService.createFileLabel(fileLabel), message = "File label created.", error = false),HttpStatus.CREATED)
    }
}