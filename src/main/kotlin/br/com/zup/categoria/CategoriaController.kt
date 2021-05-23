package br.com.zup.categoria

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@Controller("/categorias")
class CategoriaController(val categoriaRepository: CategoriaRepository) {

    @Post
    fun criaCategoria(@Body @Valid request: CategoriaRequest): HttpResponse<Any> {
        val categoria = request.paraCategoria(categoriaRepository).let {
            categoriaRepository.save(it)
        }
        return HttpResponse.created(UriBuilder
            .of("/categorias/{id}")
            .expand(mutableMapOf(Pair("id", categoria.id))))
    }
}