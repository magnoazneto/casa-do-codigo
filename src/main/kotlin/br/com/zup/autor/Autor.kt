package br.com.zup.autor

import br.com.zup.endereco.Endereco
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Autor(
    val nome: String,
    val email: String,
    var descricao: String,
    @field:Embedded val endereco: Endereco
){
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? =  null
    val criadoEm: LocalDateTime = LocalDateTime.now()
}