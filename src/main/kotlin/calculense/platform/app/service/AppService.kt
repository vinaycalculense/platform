package calculense.platform.app.service

import calculense.platform.app.dao.AppRepository
import calculense.platform.app.model.App
import calculense.platform.exception.CalculenseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AppService:IAppService {
    @Autowired
    lateinit var appRepository: AppRepository
    override fun getAppByName(name:String):App{
        val app = appRepository.findAppByName(name)
        return app
    }

    override fun createApp(app: App): App {
        return appRepository.save(app)
    }
}