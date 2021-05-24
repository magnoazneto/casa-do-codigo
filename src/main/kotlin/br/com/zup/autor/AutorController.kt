package br.com.zup.autor

import br.com.zup.servicosexternos.CepClient
import br.com.zup.servicosexternos.CepResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Controller("/autores")
class AutorController(
    val autorRepository: AutorRepository,
    val cepClient: CepClient
) {

    @Get
    @Transactional
    fun listaAutores(@QueryValue(defaultValue = "") email: String): HttpResponse<Any>{
        return if(email.isBlank()){
            HttpResponse.ok(autorRepository.findAll()
                    .map { autor -> AutorResponse(autor) })
        } else {
            val possivelAutor = autorRepository.findByEmail(email)
            when {
                possivelAutor.isPresent -> HttpResponse.ok(AutorResponse(possivelAutor.get()))
                else -> HttpResponse.notFound()
            }
        }
    }

    @Post
    @Transactional
    fun novoAutor(@Body @Valid request: AutorRequest): HttpResponse<Any> {
        val cep: CepResponse = buscaPorCep(request.cep)
        val autor = request.paraAutor(cep).let { autorRepository.save(it) }

        return HttpResponse.created(UriBuilder
            .of("/autores/{id}")
            .expand(mutableMapOf(Pair("id", autor.id))))
    }

    @Put(value = "/{id}")
    @Transactional
    fun atualizaAutor(@PathVariable id: Long, descricao: String): HttpResponse<Any> {
        val possivelAutor = autorRepository.findById(id)

        return if (possivelAutor.isPresent){
            possivelAutor.get().let { autor ->
                autor.descricao = descricao
                HttpResponse.ok(AutorResponse(autor))
            }
        } else{ HttpResponse.notFound() }

    }

    @Delete(value = "/{id}")
    fun deletaAutor(@PathVariable id: Long): HttpResponse<Any> {
        return if (autorRepository.findById(id).isPresent){
            autorRepository.deleteById(id)
            HttpResponse.ok()
        } else { HttpResponse.notFound() }
    }

    fun buscaPorCep(cep: String): CepResponse {
        return try{
            cepClient.consulta(cep).body.get()
        } catch (e: HttpClientResponseException){
            throw HttpStatusException(HttpStatus.BAD_REQUEST, "CEP inválido")
        }
    }
}