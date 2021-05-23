package br.com.zup.autor

class AutorResponse(
    autor: Autor
) {
    val nome: String = autor.nome
    val email: String = autor.email
    val descricao: String = autor.descricao
}
