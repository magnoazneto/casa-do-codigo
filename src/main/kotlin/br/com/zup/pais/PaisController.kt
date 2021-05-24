package br.com.zup.pais

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@Controller("/paises")
class PaisController(val paisRepository: PaisRepository) {

    @Post
    fun criaPais(@Body @Valid request: PaisRequest){
        request.paraPais().let { paisRepository.save(it) }
    }
}