package domain.di

import org.koin.core.module.Module

expect fun platformModule(): List<Module>

fun getAllModules(extra: List<Module> = listOf()): List<Module> {
    val list = mutableListOf(networkModule, repositoryModule, useCaseModule, presentationModule)
    list.addAll(platformModule())
    list.addAll(extra)
    return list.toList()
}