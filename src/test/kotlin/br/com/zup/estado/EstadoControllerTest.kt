package br.com.zup.estado

import br.com.zup.pais.Pais
import br.com.zup.pais.PaisRepository
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.TransactionMode
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.inject.Inject

@MicronautTest(transactionMode = TransactionMode.SINGLE_TRANSACTION, transactional = false, rollback = true)
internal class EstadoControllerTest {

    private lateinit var pais: Pais

    @field:Inject
    lateinit var estadoRepository: EstadoRepository

    @field:Inject
    lateinit var paisRepository: PaisRepository

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @BeforeEach
    internal fun setUp() {
        pais = Pais("Brasil")
        paisRepository.save(pais)
    }

    @AfterEach
    internal fun tearDown() {
        estadoRepository.deleteAll()
        paisRepository.deleteAll()
    }

    /**
     * 1 - happy path - estado com pais existente
     * 2 - nao deve cadastrar estado com pais inexistente
     * 3 - nao deve cadastrar estado duplicado no mesmo pais
     * 4 - deve cadastrar estado com nome igual mas pais diferente
     */

    @Test
    fun `deve cadastrar estado com pais existente`() {
        val estadoRequest = EstadoRequest("Recife", pais.id!!)

        val request = HttpRequest.POST("/estados", estadoRequest)
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
    }

    @Test
    fun `nao deve cadastrar estado com pais inexistente`() {
        val estadoRequest = EstadoRequest("Recife", 2L)
        val request = HttpRequest.POST("/estados", estadoRequest)

        assertThrows<HttpClientResponseException>{
            client.toBlocking().exchange(request, Any::class.java)
        }.let { error ->
            assertEquals(HttpStatus.BAD_REQUEST, error.status)
        }
    }

    @Test
    fun `nao deve cadastrar estado duplicado no mesmo pais`() {
        val estatoRequest = EstadoRequest("Recife", pais.id!!)
        val request1 = HttpRequest.POST("/estados", EstadoRequest("Recife", pais.id!!))
        client.toBlocking().exchange(request1, Any::class.java)

        val request2 = HttpRequest.POST("/estados", estatoRequest)

        assertThrows<HttpClientResponseException>{
            client.toBlocking().exchange(request2, Any::class.java)
        }.let { error ->
            assertEquals(HttpStatus.BAD_REQUEST, error.status)
        }
    }

    @Test
    fun `deve cadastrar estado com nome igual mas pais diferente`() {
        val request1 = HttpRequest.POST("/estados", EstadoRequest("Recife", 1L))
        client.toBlocking().exchange(request1, Any::class.java)

        val novoPais = Pais("Japao")
        paisRepository.save(novoPais)
        val estatoRequest = EstadoRequest("Recife", novoPais.id!!)

        val request2 = HttpRequest.POST("/estados", estatoRequest)
        val response = client.toBlocking().exchange(request2, Any::class.java)
        assertEquals(HttpStatus.CREATED, response.status)
    }
}