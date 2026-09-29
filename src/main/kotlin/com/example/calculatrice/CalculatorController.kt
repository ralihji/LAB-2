package com.example.calculatrice

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class CalculatorController {

    @GetMapping("/")
    fun index(model: Model): String {
        model.addAttribute("calculatorForm", CalculatorForm())
        return "calculator"
    }

    @PostMapping("/calculer")
    fun calculer(@ModelAttribute calculatorForm: CalculatorForm, model: Model): String {
        val a = calculatorForm.premierNombre
        val b = calculatorForm.secondNombre
        val operation = calculatorForm.operation

        if (a == null || b == null || operation == null) {
            calculatorForm.erreur = "Veuillez remplir les deux nombres."
            model.addAttribute("calculatorForm", calculatorForm)
            return "calculator"
        }

        when (operation) {
            "+" -> calculatorForm.resultat = a + b
            "-" -> calculatorForm.resultat = a - b
            "*" -> calculatorForm.resultat = a * b
            "/" -> {
                if (b == 0.0) {
                    calculatorForm.erreur = "Division par zéro impossible."
                    calculatorForm.resultat = null
                } else {
                    calculatorForm.resultat = a / b
                }
            }
            else -> calculatorForm.erreur = "Opération inconnue."
        }

        model.addAttribute("calculatorForm", calculatorForm)
        return "calculator"
    }
}
