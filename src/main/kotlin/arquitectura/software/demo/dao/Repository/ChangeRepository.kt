package arquitectura.software.demo.dao.Repository

import arquitectura.software.demo.dao.Currency
import org.springframework.data.repository.PagingAndSortingRepository

interface ChangeRepository: PagingAndSortingRepository<Currency, Long> {}