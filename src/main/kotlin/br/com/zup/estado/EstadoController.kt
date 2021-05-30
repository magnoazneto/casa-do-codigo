package br.com.zup.estado

import br.com.zup.pais.PaisRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@Controller("/estados")
class EstadoController(
    val estadoRepository: EstadoRepository,
    val paisRepository: PaisRepository
) {

    @Post
    fun criaEstado(@Body @Valid request: EstadoRequest): HttpResponse<Any>{
        if(estadoDuplicadoNoPais(request)){
            return HttpResponse.badRequest(mutableMapOf(
                    Pair(request.nome, "Já existe um estado com esse nome no pais informado")))
        }

        val estado = estadoRepository.save(request.paraEstado(paisRepository))
        return HttpResponse.created(UriBuilder.of("/estados/{id}").expand(
            mutableMapOf(Pair("id", estado.id))
        ))
    }

    fun estadoDuplicadoNoPais(request: EstadoRequest): Boolean {
        return estadoRepository
            .existsByNomeAndPaisId(request.nome, request.paisId)
    }

}