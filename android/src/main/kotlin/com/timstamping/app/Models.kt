package com.timstamping.app

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val version: String,
    val specialCodes: List<SpecialCode>,
    val title: String,
    val subTitle: String,
    val timeMessages: Long
)

@Serializable
data class SpecialCode(
    val code: String,
    val function: String
)

@Serializable
data class Dipendenti(
    val dipendenti: List<Dipendente>
)

@Serializable
data class Dipendente(
    val nome: String,
    val code: String
)

@Serializable
data class Stampings(
    val stampings: MutableList<Stamping> = mutableListOf()
)

@Serializable
data class Stamping(
    val code: String,
    val type: String,
    val time: String
)
