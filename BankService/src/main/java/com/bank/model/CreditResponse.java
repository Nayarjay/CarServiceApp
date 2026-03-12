package com.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modèle de données représentant la réponse renvoyée par la banque.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditResponse {
    private String userId; // Pour qui est la réponse ?
    private boolean approved; // Est-ce que c'est accepté (true) ou refusé (false) ?
    private String message; // Message explicatif (ex: "Solde insuffisant")
}
