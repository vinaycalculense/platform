package calculense.platform.handler.exception

import calculense.platform.exception.CalculenseException
import calculense.platform.model.Response
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<Response<Nothing>> {
        val errorMessage =ex.bindingResult.allErrors[0].defaultMessage
        val errorResponse = Response(data = null, message = errorMessage!!, error = true)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<Response<Nothing>> {
        val errorMessage =ex.message
        val errorResponse = Response(data = null, message = errorMessage!!, error = true)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }
    @ExceptionHandler(CalculenseException::class)
    fun handleCalculenseException(ex: CalculenseException): ResponseEntity<Response<Nothing>> {
        val errorMessage =ex.errorMessage
        val errorResponse = Response(data = null, message = errorMessage, error = true)
        return ResponseEntity.status(HttpStatusCode.valueOf(ex.errorCode)).body(errorResponse)
    }
}