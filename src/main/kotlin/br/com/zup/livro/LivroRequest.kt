package br.com.zup.livro

import br.com.zup.autor.Autor
import br.com.zup.autor.AutorRepository
import br.com.zup.categoria.Categoria
import br.com.zup.categoria.CategoriaRepository
import br.com.zup.compartilhado.ExistsValue
import br.com.zup.compartilhado.UniqueValue
import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.*

@Introspected
data class LivroRequest(
    @field:NotBlank @field:UniqueValue(fieldName = "titulo", targetClass = "Livro")
    val titulo: String,
    @field:NotBlank @field:Size(max = 500)
    val resumo: String,
    val sumario: String,
    @field:NotNull @field:Min(20)
    val valor: BigDecimal,
    @field:NotNull @field:Min(100)
    val numPaginas: Int,
    @field:NotBlank @field:UniqueValue(fieldName = "isbn", targetClass = "Livro")
    val isbn: String,
    @field:Future @field:NotNull
    val dataPublicacao: LocalDate,
    @field:NotNull @field:ExistsValue(fieldName = "id", targetClass = "Categoria")
    val categoriaId: Long,
    @field:NotNull @field:ExistsValue(fieldName = "id", targetClass = "Autor")
    val autorId: Long
) {
    fun paraLivro(
        autorRepository: AutorRepository,
        categoriaRepository: CategoriaRepository
    ): Livro {
        val autor: Autor = autorRepository.findById(autorId).get()
        val categoria: Categoria = categoriaRepository.findById(categoriaId).get()

        return Livro(
            titulo = this.titulo,
            resumo = this.resumo,
            sumario = this.sumario,
            valor = this.valor,
            numPaginas = this.numPaginas,
            isbn = this.isbn,
            dataPublicacao = this.dataPublicacao,
            categoria = categoria,
            autor = autor
        )
    }

}
