package arquitectura.software.demo.exceptions

import arquitectura.software.demo.dto.ResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerExceptions {

    @ExceptionHandler(IllegalArgumentException::class)
    fun zeroException(e: IllegalArgumentException): ResponseEntity<Any> {
        val responseDto = ResponseDto<Any>(0, false, e.message.toString())
        return ResponseEntity.badRequest().body(responseDto)
    }
}