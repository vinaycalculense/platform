package calculense.platform.auth.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Paid(val creditAmount:Int, val app:String)
