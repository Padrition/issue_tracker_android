package cz.mendelu.projek.constants

object Priorities{
    const val HIGH = "High"
    const val MEDIUM = "Medium"
    const val LOW = "Low"

    fun toList(): List<String> = listOf(HIGH, MEDIUM, LOW)
}