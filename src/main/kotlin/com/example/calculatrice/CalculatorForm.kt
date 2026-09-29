package com.example.calculatrice

data class CalculatorForm(
    var premierNombre: Double? = null,
    var secondNombre: Double? = null,
    var operation: String? = null,
    var resultat: Double? = null,
    var erreur: String? = null
)
