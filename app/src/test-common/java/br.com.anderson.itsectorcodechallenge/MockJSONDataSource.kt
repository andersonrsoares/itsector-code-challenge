package br.com.anderson.itsectorcodechallenge


import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MockJSONDataSource(val fileName: String, val clazz: KClass<*>)
