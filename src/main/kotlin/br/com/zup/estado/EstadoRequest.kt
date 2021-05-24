package br.com.zup.estado

import br.com.zup.compartilhado.ExistsValue
import br.com.zup.pais.PaisRepository
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class EstadoRequest(
    @field:NotBlank
    val nome: String,
    @field:ExistsValue(fieldName = "id", targetClass = "Pais")
    val paisId: Long
) {
    fun paraEstado(paisRepository: PaisRepository): Estado {
        return Estado(
            nome,
            paisRepository.findById(paisId).get()
        )
    }
}
