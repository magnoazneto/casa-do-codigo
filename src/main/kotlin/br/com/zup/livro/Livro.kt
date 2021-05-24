package br.com.zup.livro

import br.com.zup.autor.Autor
import br.com.zup.categoria.Categoria
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.*

@Entity
class Livro(
    val titulo: String,
    val resumo: String,
    val sumario: String,
    val valor: BigDecimal,
    val numPaginas: Int,
    val isbn: String,
    val dataPublicacao: LocalDate,
    @field:NotNull @field:ManyToOne
    val categoria: Categoria,
    @field:NotNull @field:ManyToOne
    val autor: Autor
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}