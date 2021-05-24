package br.com.zup.pais

import br.com.zup.estado.Estado
import javax.persistence.*

@Entity
class Pais(
    val nome: String
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    @OneToMany(mappedBy = "pais")
    var estados: Set<Estado> = mutableSetOf()
}