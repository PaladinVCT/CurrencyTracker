package by.lebedev.core.data.model.mapper

interface Mapper<F, T> {
    fun map(from: F): T
}