import Core
import XCTest

/// Contrat SKIE : `Outcome` doit rester un enum Swift exhaustif (le test ne compile plus si un cas est ajouté).
final class OutcomeContractTests: XCTestCase {
    func testOutcomeIsExhaustive() {
        let outcome: Outcome<NSString> = OutcomeSuccess(value: "ok")
        switch onEnum(of: outcome) {
        case .success(let success):
            XCTAssertEqual(success.value, "ok")
        case .failure:
            XCTFail("Outcome.Success attendu")
        }
    }
}
