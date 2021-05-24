package br.com.zup.servicosexternos

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client(value = "\${cep.host}")
interface CepClient {

    @Get(
        value = "{cep}/json",
        consumes = [MediaType.APPLICATION_JSON]
    )
    fun consulta(@PathVariable cep: String): HttpResponse<CepResponse>
}