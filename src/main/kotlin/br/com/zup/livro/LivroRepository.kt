package br.com.zup.livro

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface LivroRepository: JpaRepository<Livro, Long> {

}
