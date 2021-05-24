package br.com.zup.autor

import br.com.zup.compartilhado.UniqueValue
import br.com.zup.endereco.Endereco
import br.com.zup.servicosexternos.CepResponse
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
data class AutorRequest(
    @field:NotBlank
    val nome: String,
    @field:NotBlank @field:Email @field:UniqueValue(fieldName = "email", targetClass = "Autor")
    val email: String,
    @field:NotBlank @field:Size(max = 400)
    val descricao: String,
    @field:NotBlank
    val cep: String
){
    fun paraAutor(cepResponse: CepResponse): Autor {
        val endereco: Endereco = Endereco(cepResponse)
        return Autor(nome, email, descricao, endereco)
    }
}
