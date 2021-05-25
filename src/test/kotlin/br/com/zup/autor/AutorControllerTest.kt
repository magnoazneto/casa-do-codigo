package br.com.zup.autor

import br.com.zup.endereco.Endereco
import br.com.zup.servicosexternos.CepClient
import br.com.zup.servicosexternos.CepResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import javax.inject.Inject

/**
 * Métodos testados: GET e POST. Eram onde haviam branches geradas pela aplicação
 * PUT e DELETE no estado atual seria testar a própria JPA.
 */
@MicronautTest
internal class AutorControllerTest {

    lateinit var autor: Autor

    @field:Inject
    lateinit var cepClient: CepClient

    @field:Inject
    lateinit var autorRepository: AutorRepository

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @BeforeEach
    internal fun setUp() {
        val endereco = Endereco(
            CepResponse(
                "rua x",
                "complemento y",
                "ilhotas",
                "localidade z",
                "pi",
                "86"
            ))
        autor = Autor(
            nome = "Magno",
            email = "magno@gmail.com",
            descricao = "descricao aq",
            endereco = endereco
        )

        autorRepository.save(autor)
    }

    @AfterEach
    internal fun tearDown() {
        autorRepository.deleteAll()
    }

    @Test // @Get - path 1/3 - queryValue presente/autor cadastrado
    fun `Deve retornar os detalhes de um autor com email cadastrado`() {

        val response = client
            .toBlocking()
            .exchange("autores?email=${autor.email}", AutorResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(autor.nome, response.body()!!.nome)
        assertEquals(autor.email, response.body()!!.email)
        assertEquals(autor.descricao, response.body()!!.descricao)
    }

    @Test // @Get - path 2/3 - queryValue presente/autor nao cadastrado
    fun `Deve retornar 404 para um email nao cadastrado`() {
        assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(
                "autores?email=test02@gmail.com", AutorResponse::class.java)
        }.let { exception ->
            assertEquals(HttpStatus.NOT_FOUND, exception.status)
        }
    }

    @Test // @Get - path 3/3 - queryValue vazio/autores cadastrados
    fun `Deve retornar uma lista de autores quando param nao for passado`() {
        val autorRequest = AutorRequest("Magno", "test@gmail.com", "desc aq", "64014047")
        val cepResponse = CepResponse(
            "logradouro x",
            "complemento z",
            "bairro",
            "localidade",
            "pi",
            "86"
        )

        Mockito.`when`(cepClient.consulta(autorRequest.cep)).thenReturn(HttpResponse.ok(cepResponse))

        val request = HttpRequest.POST("/autores", autorRequest)
        client.toBlocking().exchange(request, Any::class.java)

        client.toBlocking().exchange(
            "autores", List::class.java
        ).let { response ->
            assertEquals(2, response.body().size)
        }
    }

    @Test // @Post - path 1/2 - email já cadastrado
    fun `Nao deve aceitar email duplicado para novo autor`() {
        val autorRequest = AutorRequest("Magno", autor.email, "desc aq", "64014047")
        val cepResponse = CepResponse(
            "logradouro x",
            "complemento z",
            "bairro",
            "localidade",
            "pi",
            "86"
        )

        Mockito.`when`(cepClient.consulta(autorRequest.cep)).thenReturn(HttpResponse.ok(cepResponse))
        val request = HttpRequest.POST("/autores", autorRequest)

        assertThrows<HttpClientResponseException>{
            client.toBlocking().exchange(request, Any::class.java)
        }.let { responseError ->
            assertEquals(HttpStatus.BAD_REQUEST, responseError.status)
        }
    }

    @Test // @Post - path 2/2 - email não cadastrado
    fun `Deve cadastrar um novo autor valido`() {
        val autorRequest = AutorRequest("Magno", "test@gmail.com", "desc aq", "64014047")
        val cepResponse = CepResponse(
            "logradouro x",
            "complemento z",
            "bairro",
            "localidade",
            "pi",
            "86"
        )

        Mockito.`when`(cepClient.consulta(autorRequest.cep)).thenReturn(HttpResponse.ok(cepResponse))

        val request = HttpRequest.POST("/autores", autorRequest)
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("location"))
        assertTrue(response.header("Location")!!.matches("/autores/\\d".toRegex()))
    }



    @MockBean(CepClient::class)
    fun cepMock(): CepClient {
        return Mockito.mock(CepClient::class.java)
    }
}