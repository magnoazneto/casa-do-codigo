package br.com.zup.autor

import br.com.zup.compartilhado.ExistsValue
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
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

    @Put(value = "/{id}")
    fun atualizaAutor(@PathVariable id: Long, descricao: String): HttpResponse<Any> {
        val possivelAutor = autorRepository.findById(id)

        return if (possivelAutor.isPresent){
            possivelAutor.get().let { autor ->
                autor.descricao = descricao
                autorRepository.update(autor)
                HttpResponse.ok(AutorResponse(autor))
            }
        } else{ HttpResponse.notFound() }

    }
}