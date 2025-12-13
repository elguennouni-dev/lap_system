package com.lap.Order.Management.System;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return """
        <!DOCTYPE html>
        <html lang="fr">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>LAP Order Management System - Documentation</title>
            <style>
                body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; max-width: 900px; margin: 0 auto; padding: 20px; background-color: #f4f4f9; }
                h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }
                h2 { color: #2980b9; margin-top: 30px; }
                .card { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 20px; }
                code { background-color: #eee; padding: 2px 5px; border-radius: 4px; font-family: 'Consolas', monospace; color: #c7254e; }
                .method { font-weight: bold; color: white; padding: 3px 8px; border-radius: 4px; font-size: 0.9em; margin-right: 10px; }
                .get { background-color: #61affe; }
                .post { background-color: #49cc90; }
                .put { background-color: #fca130; }
                .delete { background-color: #f93e3e; }
                ul { list-style-type: none; padding: 0; }
                li { margin-bottom: 10px; border-bottom: 1px solid #eee; padding-bottom: 10px; }
                .footer { margin-top: 40px; text-align: center; font-size: 0.9em; color: #777; }
            </style>
        </head>
        <body>

            <div class="card">
                <h1>LAP Order Management System</h1>
                <p>Bienvenue sur l'API de gestion des commandes. Ce système permet de gérer le cycle de vie complet des commandes, de la création à la livraison, en passant par le design et l'impression.</p>
                <p><strong>Statut du Serveur :</strong> <span style="color:green">En Ligne</span> 🟢</p>
                <p><strong>Port :</strong> 2099</p>
            </div>

            <div class="card">
                <h2>1. Authentification</h2>
                <ul>
                    <li><span class="method post">POST</span> <code>/auth/login</code> - Connexion utilisateur (Retourne JWT ou Statut).</li>
                </ul>
            </div>

            <div class="card">
                <h2>2. Gestion des Utilisateurs (Admin)</h2>
                <ul>
                    <li><span class="method post">POST</span> <code>/user/create</code> - Créer un nouvel employé (Designer, Imprimeur, etc.).</li>
                    <li><span class="method get">GET</span> <code>/user</code> - Lister tous les utilisateurs.</li>
                    <li><span class="method post">POST</span> <code>/user/zone</code> - Ajouter une nouvelle zone géographique.</li>
                </ul>
            </div>

            <div class="card">
                <h2>3. Gestion des Commandes</h2>
                <ul>
                    <li><span class="method post">POST</span> <code>/commande</code> - Créer une commande (Supporte JSON + Fichiers Logo/Façade).</li>
                    <li><span class="method get">GET</span> <code>/commande</code> - Lister toutes les commandes.</li>
                    <li><span class="method get">GET</span> <code>/commande/{id}</code> - Détails d'une commande spécifique.</li>
                    <li><span class="method delete">DELETE</span> <code>/commande/{id}</code> - Supprimer une commande.</li>
                </ul>
            </div>

            <div class="card">
                <h2>4. Workflow & Tâches</h2>
                <ul>
                    <li><span class="method post">POST</span> <code>/workflow/assign</code> - Assigner une tâche (Design/Impression) à un utilisateur.</li>
                    <li><span class="method put">PUT</span> <code>/workflow/task/{taskId}/complete</code> - Terminer une tâche et uploader le fichier final.</li>
                    <li><span class="method put">PUT</span> <code>/workflow/task/{taskId}/validate</code> - Valider ou rejeter le travail effectué.</li>
                    <li><span class="method put">PUT</span> <code>/workflow/commande/{id}/move-to-stock</code> - Déplacer une commande livrée vers le stock final.</li>
                </ul>
            </div>

            <div class="card">
                <h2>5. Référence des Données</h2>
                <p><strong>Rôles :</strong> ADMINISTRATEUR, COMMERCIAL, DESIGNER, IMPRIMEUR, LOGISTIQUE</p>
                <p><strong>Types de Travaux :</strong> PANNEAU, ONEWAY</p>
                <p><strong>États de Commande :</strong> CREEE, EN_DESIGN, EN_IMPRESSION, IMPRESSION_VALIDE, EN_LIVRAISON, LIVRAISON_VALIDE, TERMINEE_STOCK</p>
            </div>

            <div class="footer">
                &copy; 2025 LAP Order Management System | Backend API
            </div>

        </body>
        </html>
        """;
    }
}