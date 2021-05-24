package br.com.zup.autor

import br.com.zup.endereco.Endereco
import br.com.zup.servicosexternos.CepResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
internal class AutorControllerTest {

    lateinit var autor: Autor

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

    @Test
    internal fun `DEVE retornar os detalhes de um autor`() {

        val response = client
            .toBlocking()
            .exchange("autores?email=${autor.email}", AutorResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(autor.nome, response.body()!!.nome)
        assertEquals(autor.email, response.body()!!.email)
        assertEquals(autor.descricao, response.body()!!.descricao)
    }
}