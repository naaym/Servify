# Architecture notes

## Recherche par référentiels et facettes

Les options de recherche (services, gouvernorats) s'appuient désormais sur des tables de référence (`services`, `governorates`) et sur la table de liaison `provider_offerings` pour compter les prestataires approuvés. Les endpoints `/api/search/options/*` exposent des facettes `{id, name, count}` filtrées sur les offres actives des prestataires en statut `ACCEPTED`, ce qui remplace l'ancienne logique basée sur des `string[]` implicites.
