package br.com.zup.servicosexternos

data class CepResponse(
    val logradouro: String?,
    val complemento: String?,
    val bairro: String?,
    val localidade: String?,
    val uf: String?,
    val ddd: String?
)
