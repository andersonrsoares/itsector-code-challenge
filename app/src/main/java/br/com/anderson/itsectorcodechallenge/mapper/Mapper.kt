package br.com.anderson.itsectorcodechallenge.mapper

import javax.inject.Singleton

@Singleton
abstract class Mapper<in T, E> {

    abstract fun map(from: T): E
}
