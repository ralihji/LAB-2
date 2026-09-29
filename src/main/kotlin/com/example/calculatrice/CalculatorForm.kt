package com.example.calculatrice

/**
 * Objet lié au formulaire (th:object="${calculatorForm}").
 *
 * Classe avec un constructeur sans argument : Spring crée d'abord l'objet puis remplit
 * chaque propriété. Ainsi, une saisie invalide (ex. "abc") est simplement enregistrée
 * comme erreur dans le BindingResult, et l'objet reste disponible pour la vue.
 */
class CalculatorForm {
    var premierNombre: Double? = null
    var secondNombre: Double? = null
    var operation: String? = null
    var resultat: Double? = null
    var erreur: String? = null
}
