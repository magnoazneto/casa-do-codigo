package br.com.zup.livro

import br.com.zup.autor.AutorResponse

class LivroResponse(
    livro: Livro
) {
    val titulo = livro.titulo
    val resumo = livro.resumo
    val valor = livro.valor
    val numPaginas = livro.numPaginas
    val dataPublicacao = livro.dataPublicacao.toString()
    val autor: AutorResponse = AutorResponse(livro.autor)
    val categoria = livro.categoria.nome
}
