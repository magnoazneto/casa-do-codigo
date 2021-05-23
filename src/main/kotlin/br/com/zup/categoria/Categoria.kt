package br.com.zup.categoria

import javax.persistence.*

@Entity
class Categoria(
    val nome: String,
    @field:ManyToOne val categoriaMae: Categoria?
){
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}