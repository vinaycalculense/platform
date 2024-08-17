package calculense.platform.process.service

interface ProcessExecutor {
    fun process(requestId:String, requestName:String)
}