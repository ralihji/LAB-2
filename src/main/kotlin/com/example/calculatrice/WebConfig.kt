package com.example.calculatrice

import org.springframework.context.annotation.Configuration
import org.springframework.format.Formatter
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.math.BigDecimal
import java.util.Locale

/**
 * Conversion texte <-> nombre pour les champs du formulaire.
 *  - lecture   : "3" ou "2.5" devient un Double ; "abc", "NaN" ou "Infinity" sont refusés
 *                (Spring enregistre alors une erreur de saisie dans le BindingResult)
 *  - affichage : 3.0 est réaffiché "3" (et non "3.0") dans le champ après le calcul
 */
class NombreFormatter : Formatter<Double> {
    override fun parse(text: String, locale: Locale): Double {
        val nombre = text.trim().toDouble()
        require(nombre.isFinite()) { "Nombre invalide : $text" }
        return nombre
    }

    override fun print(valeur: Double, locale: Locale): String =
        if (valeur.isFinite() && Math.abs(valeur) < 1e15) BigDecimal.valueOf(valeur).stripTrailingZeros().toPlainString()
        else valeur.toString() // très grands nombres : notation scientifique (ex. 1.0E20)
}

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addFormatter(NombreFormatter())
    }
}
