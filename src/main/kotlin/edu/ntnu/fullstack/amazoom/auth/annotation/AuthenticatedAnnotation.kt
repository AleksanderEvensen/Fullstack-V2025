package edu.ntnu.fullstack.amazoom.auth.annotation

import org.springframework.security.access.prepost.PreAuthorize


@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CLASS
)
@Retention(
    AnnotationRetention.RUNTIME
)
@PreAuthorize("isAuthenticated()")
annotation class Authenticated
