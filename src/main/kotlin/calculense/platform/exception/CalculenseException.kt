package calculense.platform.exception

class CalculenseException(val errorMessage: String, val errorCode: Int):Exception() {
}