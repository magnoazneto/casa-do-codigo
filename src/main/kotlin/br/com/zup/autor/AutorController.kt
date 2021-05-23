package br.com.zup.autor

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@Controller("/autores")
class AutorController(val autorRepository: AutorRepository) {

    @Get
    fun listaAutores(): HttpResponse<List<AutorResponse>>{
        return HttpResponse.ok(autorRepository
            .findAll().map { autor -> AutorResponse(autor) })
    }

    @Post
    fun novoAutor(@Body @Valid request: AutorRequest): HttpResponse<Any> {
        val autor = request.paraAutor().let { autorRepository.save(it) }
        return HttpResponse.created(UriBuilder
            .of("/autores/{id}")
            .expand(mutableMapOf(Pair("id", autor.id))))
    }
}