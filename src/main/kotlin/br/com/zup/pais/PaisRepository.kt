package br.com.zup.pais

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface PaisRepository: JpaRepository<Pais, Long>