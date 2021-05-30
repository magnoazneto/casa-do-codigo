package br.com.zup.estado

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface EstadoRepository: JpaRepository<Estado, Long> {
    fun existsByNomeAndPaisId(nome: String,paisId: Long): Boolean
}