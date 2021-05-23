package br.com.zup.categoria

import br.com.zup.compartilhado.ExistsValue
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class CategoriaRequest(
    @field:NotBlank val nome: String,
    @field:ExistsValue(fieldName = "id", targetClass = "Categoria") val categoriaMaeId: Long?
){
    fun paraCategoria(repository: CategoriaRepository): Categoria {
        var categoriaMae : Categoria? = null
        if(categoriaMaeId != null) {
            categoriaMae = repository.findById(categoriaMaeId).get()
        }
        return Categoria(nome, categoriaMae)
    }
}
