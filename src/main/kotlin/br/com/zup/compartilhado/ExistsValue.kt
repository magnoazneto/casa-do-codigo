package br.com.zup.compartilhado

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.transaction.annotation.TransactionalAdvice
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Inject
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.validation.Constraint
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*

@MustBeDocumented
@Target(FIELD, TYPE_PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = [ExistsValueValidator::class])
annotation class ExistsValue(
    val message: String = "Valor informado não existe no banco de dados",
    val fieldName: String,
    val targetClass: String
)

@Singleton
@TransactionalAdvice
class ExistsValueValidator(@Inject val manager: EntityManager): ConstraintValidator<ExistsValue, Any> {

    override fun isValid(
        value: Any?,
        annotationMetadata: AnnotationValue<ExistsValue>,
        context: ConstraintValidatorContext
    ): Boolean {
        if(value == null){
            return true
        }

        val targetClass: String = annotationMetadata.stringValue("targetClass").get()
        val fieldName: String = annotationMetadata.stringValue("fieldName").get()

        return manager
            .createQuery("select 1 from $targetClass where $fieldName =:pValue")
            .setParameter("pValue", value)
            .resultList
            .isNotEmpty()
    }
}
