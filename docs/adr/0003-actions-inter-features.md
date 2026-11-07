# ADR-0003 — Action d'une feature sur une autre : callback câblé par l'app

- **Date** : 2026-11-07
- **Statut** : accepté
- **Approche** : commun

## Contexte
L'écran catalogue doit ajouter un produit au panier. AGENTS.md interdit `feature → feature` et la skill
`new-feature-module` ne dit pas comment une feature déclenche l'action d'une autre. Le panier doit
aussi être partagé : ce que le catalogue ajoute doit apparaître dans l'onglet panier.

## Options considérées
1. `catalog` dépend de `cart-api` et appelle un `CartApi.add` (contrat d'écriture côté cart).
2. Bus d'événements dans `core` (`CatalogEvent.AddToCart`), écouté par cart.
3. L'écran catalogue reçoit un callback `onAddToCart(productId)` ; l'app, qui détient le `CartStore`,
   le relie à `CartIntent.Add`. `cart` dépend de `catalog-api` pour résoudre le produit.

## Décision
Option 3 : catalog ne connaît pas cart, une seule arête inter-features (`cart → catalog-api`), et le
câblage reste visible dans la composition root (`AppNavHost`, `RootView`).

## Conséquences
- Le `CartStore` est détenu par l'app (racine de navigation Android, `@State` de `RootView` iOS),
  pas par la destination panier ; le `CartRepository` est un `single` Koin.
- Chaque nouvelle interaction croisée ajoute un paramètre d'écran et une ligne de câblage dans les
  deux apps : réévaluer (option 1 ou 2) si une feature dépasse deux ou trois callbacks de ce type.
- Pas d'ADR « graphe » à part : les arêtes restent contrôlées par Konsist (`feature → *-api` seulement).
