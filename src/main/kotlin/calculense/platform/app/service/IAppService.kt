package calculense.platform.app.service

import calculense.platform.app.model.App

interface IAppService {
    fun getAppByName(name: String): App
    fun createApp(app: App): App
}