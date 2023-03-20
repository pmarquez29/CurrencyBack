package arquitectura.software.demo.dto

data class ResponseDto<T>(
    var data: T,
    var success: Boolean,
    var message: String
)