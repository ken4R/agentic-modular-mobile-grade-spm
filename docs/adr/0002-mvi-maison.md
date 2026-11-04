# ADR-0002 — MVI maison plutôt que MVIKotlin

- **Date** : 2026-11-04
- **Statut** : accepté
- **Approche** : commun

## Contexte
La skill `expose-to-swift` impose que chaque Store expose `val state: StateFlow<XUiState>` et
`fun accept(intent: XIntent)`. L'export Objective-C de Kotlin/Native perd les paramètres génériques
hérités : un Store dérivé d'une base générique exposerait `state` sans type concret côté Swift.
MVIKotlin est présent dans `libs.versions.toml`.

## Options considérées
1. MVIKotlin (`Store<Intent, State, Label>`, générique, API riche).
2. Base minimale `BaseStore` (scope annulable) dans `core`, chaque Store déclare ses types concrets.

## Décision
Option 2 : API Swift typée via SKIE sans adaptateur, et aucune dépendance supplémentaire.

## Conséquences
- Chaque Store réécrit quelques lignes (`MutableStateFlow`, `accept`) ; pas d'outillage time-travel.
- L'entrée `mvikotlin` du catalogue reste inutilisée ; à retirer du template si les trois POC confirment.
- Réévaluer si les Stores doivent gérer des effets complexes (labels, bootstrappers).
