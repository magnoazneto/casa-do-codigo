package br.com.zup.estado

import br.com.zup.pais.Pais
import javax.persistence.*

@Entity
class Estado(
    val nome: String,
    @field:ManyToOne
    val pais: Pais
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
