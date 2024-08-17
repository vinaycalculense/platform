package calculense.platform.app.controller

import calculense.platform.app.model.App
import calculense.platform.app.service.IAppService
import calculense.platform.auth.annotation.RequiresRole
import calculense.platform.model.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("app")
@CrossOrigin(origins = ["*"])
class AppController {
    @Autowired
    private lateinit var appService:IAppService
    @PostMapping
    @RequiresRole(type = ["admin"])
    fun createApp(@RequestBody app: App):ResponseEntity<Response<App>>{
        return ResponseEntity(Response(data = appService.createApp(app = app), message = "App Registered.", error = false),HttpStatus.CREATED)
    }
}