package arquitectura.software.demo.bl

import arquitectura.software.demo.dao.Currency
import arquitectura.software.demo.dao.Repository.ChangeRepository
import arquitectura.software.demo.dao.Repository.CurrencyRepository
import arquitectura.software.demo.dto.CurrencyDto
import arquitectura.software.demo.dto.RequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class CurrencyBl @Autowired constructor(private val currencyRepository: CurrencyRepository, private val changeRepository: ChangeRepository) {

    companion object {
        val objectMapper = jacksonObjectMapper()
        val LOGGER: Logger = LoggerFactory.getLogger(CurrencyBl::class.java.name)
    }

    @Value("\${currency.url}")
    private val url: String? = null

    @Value("\${currency.api_key}")
    private val apiKey: String? = null

    fun obtain(from: String, to: String, amount: BigDecimal): CurrencyDto {
        LOGGER.info("Iniciando la logica para convertir divisas")
        if (amount <= BigDecimal.ZERO) {
            LOGGER.error("El monto debe ser mayor a 0 y no puede ser negativo")
            throw IllegalArgumentException("The change amount must be greater than 0 and not equal to 0")
        }

        val client = OkHttpClient.Builder().build()
        val request = Request.Builder()
            .url("$url?from=$from&to=$to&amount=$amount")
            .addHeader("apikey", apiKey)
            .method("GET", null)
            .build()
        val response = client.newCall(request).execute()
        val result = response.body()!!.string()

        if(response.isSuccessful){
            LOGGER.info("La respuesta fue exitosa")
            val currencyDto = objectMapper.readValue(result, CurrencyDto::class.java)
            val currency: Currency = Currency()
            currency.currencyFrom = from
            currency.currencyTo = to
            currency.amount = amount
            currency.result = currencyDto.result!!
            currency.date = Date()
            currencyRepository.save(currency)
            LOGGER.info("Conversion result: ${result}")
        }

        val mapper = ObjectMapper()
        val currencyDto = mapper.readValue(result, CurrencyDto::class.java)
        return currencyDto
    }

    fun getListOfConvertions(page: Int, size: Int): Any {
        val currencies = changeRepository.findAll(PageRequest.of(page, size))
        return currencies
    }

}