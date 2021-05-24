package br.com.zup.livro

import br.com.zup.autor.AutorRepository
import br.com.zup.categoria.CategoriaRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@Controller("/livros")
class LivroController(
    val livroRepository: LivroRepository,
    val autorRepository: AutorRepository,
    val categoriaRepository: CategoriaRepository
) {

    @Post
    fun criaLivro(@Body @Valid request: LivroRequest): HttpResponse<Any> {
        val livro = request.paraLivro(autorRepository, categoriaRepository).let {
            livroRepository.save(it)
        }
        return HttpResponse.created(UriBuilder.of("/livros/{id}")
            .expand(mutableMapOf(Pair("id", livro.id))))
    }
}