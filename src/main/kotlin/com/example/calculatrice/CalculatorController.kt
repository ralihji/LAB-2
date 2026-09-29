package com.example.calculatrice

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Contrôleur MVC de la calculatrice.
 *
 *  - GET  /         : affiche la calculatrice vide (vue "calculator")
 *  - POST /calculer : lit les deux nombres et l'opération, calcule côté serveur,
 *                     place le résultat (ou l'erreur) dans le Model puis réaffiche la vue.
 */
@Controller
class CalculatorController {

    @GetMapping("/")
    fun index(model: Model): String {
        model.addAttribute("calculatorForm", CalculatorForm())
        model.addAttribute("affichage", "0")
        return "calculator"
    }

    /** Si quelqu'un tape /calculer directement dans la barre d'adresse, on le renvoie à l'accueil. */
    @GetMapping("/calculer")
    fun calculerSansFormulaire(): String = "redirect:/"

    @PostMapping("/calculer")
    fun calculer(
        @ModelAttribute calculatorForm: CalculatorForm,
        bindingResult: BindingResult, // capte les valeurs non numériques au lieu de renvoyer une erreur 400
        model: Model
    ): String {
        // On ignore tout résultat ou message qui aurait pu être envoyé par le formulaire.
        calculatorForm.resultat = null
        calculatorForm.erreur = null

        val a = calculatorForm.premierNombre
        val b = calculatorForm.secondNombre
        val operation = calculatorForm.operation

        when {
            bindingResult.hasFieldErrors("premierNombre") || bindingResult.hasFieldErrors("secondNombre") ->
                calculatorForm.erreur = "Veuillez entrer des nombres valides."

            a == null || b == null ->
                calculatorForm.erreur = "Veuillez remplir les deux nombres."

            !a.isFinite() || !b.isFinite() -> // "NaN" ou "Infinity" ne sont pas des nombres acceptés
                calculatorForm.erreur = "Veuillez entrer des nombres valides."

            else -> when (operation) {
                "+" -> calculatorForm.resultat = a + b
                "-" -> calculatorForm.resultat = a - b
                "*" -> calculatorForm.resultat = a * b
                "/" -> {
                    if (b == 0.0) {
                        calculatorForm.erreur = "Division par zéro impossible."
                    } else {
                        calculatorForm.resultat = a / b
                    }
                }
                else -> calculatorForm.erreur = "Opération inconnue."
            }
        }

        // Un nombre trop grand pour un Double donne "Infinity" : on le signale comme une erreur.
        val resultat = calculatorForm.resultat
        if (resultat != null && !resultat.isFinite()) {
            calculatorForm.resultat = null
            calculatorForm.erreur = "Résultat trop grand pour être affiché."
        }

        // calculatorForm est déjà dans le Model grâce à @ModelAttribute (avec son BindingResult) :
        // on ajoute seulement le texte à afficher sur l'écran.
        model.addAttribute("affichage", texteEcran(calculatorForm))
        return "calculator"
    }

    /** Texte affiché sur l'écran : "Erreur", "0" ou le résultat avec au plus 8 décimales. */
    private fun texteEcran(form: CalculatorForm): String {
        if (form.erreur != null) return "Erreur"
        val resultat = form.resultat ?: return "0"
        val texte = BigDecimal.valueOf(resultat)
            .setScale(8, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        return if (texte == "-0") "0" else texte
    }
}
