import Core
import XCTest

/// Contrat SKIE : chaque `sealed interface` d'intents (auth, catalog, cart) devient un enum Swift exhaustif
/// via `onEnum(of:)`. Le test ne compile plus si un cas est ajouté côté Kotlin sans être traité ici.
final class IntentContractTests: XCTestCase {
    func testAuthIntentIsExhaustive() {
        XCTAssertEqual(describe(AuthIntentEmailChanged(value: "a@b.c")), "email:a@b.c")
        XCTAssertEqual(describe(AuthIntentPasswordChanged(value: "secret")), "password")
        XCTAssertEqual(describe(AuthIntentSubmit.shared), "submit")
        XCTAssertEqual(describe(AuthIntentLogout.shared), "logout")
    }

    func testCatalogIntentIsExhaustive() {
        XCTAssertEqual(describe(CatalogIntentLoad.shared), "load")
    }

    func testCartIntentIsExhaustive() {
        XCTAssertEqual(describe(CartIntentAdd(productId: "p1")), "add:p1")
        XCTAssertEqual(describe(CartIntentDecrement(productId: "p1")), "decrement:p1")
        XCTAssertEqual(describe(CartIntentRemove(productId: "p1")), "remove:p1")
        XCTAssertEqual(describe(CartIntentClear.shared), "clear")
    }

    private func describe(_ intent: any AuthIntent) -> String {
        switch onEnum(of: intent) {
        case .emailChanged(let change): return "email:\(change.value)"
        case .passwordChanged: return "password"
        case .submit: return "submit"
        case .logout: return "logout"
        }
    }

    private func describe(_ intent: any CatalogIntent) -> String {
        switch onEnum(of: intent) {
        case .load: return "load"
        }
    }

    private func describe(_ intent: any CartIntent) -> String {
        switch onEnum(of: intent) {
        case .add(let add): return "add:\(add.productId)"
        case .decrement(let decrement): return "decrement:\(decrement.productId)"
        case .remove(let remove): return "remove:\(remove.productId)"
        case .clear: return "clear"
        }
    }
}
