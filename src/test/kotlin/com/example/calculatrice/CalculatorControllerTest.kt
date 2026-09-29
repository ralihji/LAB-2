package com.example.calculatrice

import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

/**
 * Tests du contrôleur avec MockMvc : on vérifie la route, la vue retournée
 * et ce qui est placé dans le Model, sans démarrer le serveur.
 */
@WebMvcTest(CalculatorController::class)
class CalculatorControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    /** Envoie le formulaire comme le ferait le navigateur. */
    private fun calculer(premier: String, second: String, operation: String): ResultActions =
        mockMvc.perform(
            post("/calculer")
                .param("premierNombre", premier)
                .param("secondNombre", second)
                .param("operation", operation)
        )
            .andExpect(status().isOk)
            .andExpect(view().name("calculator"))

    @Test
    fun accueilAfficheLaCalculatriceVide() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(view().name("calculator"))
            .andExpect(model().attributeExists("calculatorForm"))
            .andExpect(model().attribute("affichage", "0"))
    }

    @Test
    fun addition() {
        calculer("2", "3", "+").andExpect(model().attribute("affichage", "5"))
    }

    @Test
    fun soustraction() {
        calculer("10", "4", "-").andExpect(model().attribute("affichage", "6"))
    }

    @Test
    fun multiplication() {
        calculer("3", "5", "*").andExpect(model().attribute("affichage", "15"))
    }

    @Test
    fun division() {
        calculer("10", "4", "/").andExpect(model().attribute("affichage", "2.5"))
    }

    @Test
    fun divisionArrondieAHuitDecimales() {
        calculer("1", "3", "/").andExpect(model().attribute("affichage", "0.33333333"))
    }

    @Test
    fun nombresDecimauxEtNegatifs() {
        calculer("-1.5", "4", "*").andExpect(model().attribute("affichage", "-6"))
    }

    @Test
    fun divisionParZero() {
        calculer("5", "0", "/")
            .andExpect(model().attribute("affichage", "Erreur"))
            .andExpect(model().attribute("calculatorForm", hasProperty<CalculatorForm>("erreur", equalTo("Division par zéro impossible."))))
    }

    @Test
    fun saisieNonNumerique() {
        calculer("abc", "3", "+")
            .andExpect(model().attribute("affichage", "Erreur"))
            .andExpect(model().attributeHasFieldErrors("calculatorForm", "premierNombre"))
            .andExpect(model().attribute("calculatorForm", hasProperty<CalculatorForm>("erreur", equalTo("Veuillez entrer des nombres valides."))))
    }

    @Test
    fun champVide() {
        calculer("", "3", "+")
            .andExpect(model().attribute("affichage", "Erreur"))
            .andExpect(model().attribute("calculatorForm", hasProperty<CalculatorForm>("erreur", equalTo("Veuillez remplir les deux nombres."))))
    }

    @Test
    fun operationInconnue() {
        calculer("2", "3", "%")
            .andExpect(model().attribute("affichage", "Erreur"))
            .andExpect(model().attribute("calculatorForm", hasProperty<CalculatorForm>("erreur", equalTo("Opération inconnue."))))
    }

    @Test
    fun getSurCalculerRedirigeVersAccueil() {
        mockMvc.perform(get("/calculer"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/"))
    }
}
