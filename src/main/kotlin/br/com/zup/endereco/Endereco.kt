package br.com.zup.endereco

import br.com.zup.servicosexternos.CepResponse
import javax.persistence.Embeddable

@Embeddable
class Endereco(
    endereco: CepResponse
) {
    val logradouro: String = endereco.logradouro ?: ""
    val bairro: String = endereco.bairro ?: ""
    val uf: String = endereco.uf ?: ""
    val complemento: String = endereco.complemento ?: ""
    val localidade: String = endereco.localidade ?: ""
    val ddd: String = endereco.ddd ?: ""
}