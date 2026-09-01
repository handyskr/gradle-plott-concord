package kr.co.plott.concord

fun interface Initializer<T> {
    fun initialize(configuration: T)
}
