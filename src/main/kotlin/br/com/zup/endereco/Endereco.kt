package br.com.zup.endereco

import br.com.zup.servicosexternos.CepResponse
import javax.persistence.Embeddable

@Embeddable
class Endereco(
    endereco: CepResponse?
) {
    val logradouro = endereco?.logradouro
    val bairro = endereco?.bairro
    val uf = endereco?.uf
    val complemento = endereco?.complemento
    val localidade = endereco?.localidade
    val ddd = endereco?.ddd
}