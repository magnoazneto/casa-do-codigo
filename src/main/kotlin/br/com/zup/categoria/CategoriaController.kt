package br.com.zup.categoria

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.validation.Valid

@Validated
@Controller("/categorias")
class CategoriaController(val categoriaRepository: CategoriaRepository) {

    @Post
    fun criaCategoria(@Body @Valid request: CategoriaRequest) {
        println(request)
        request.paraCategoria(categoriaRepository).let { novaCategoria ->
            categoriaRepository.save(novaCategoria)
        }
    }
}