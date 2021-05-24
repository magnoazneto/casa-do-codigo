package br.com.zup.pais

import br.com.zup.compartilhado.UniqueValue
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class PaisRequest(
    @field:NotBlank @UniqueValue(fieldName = "nome", targetClass = "Pais")
    val nome: String
) {
    fun paraPais(): Pais {
        return Pais(nome)
    }
}
