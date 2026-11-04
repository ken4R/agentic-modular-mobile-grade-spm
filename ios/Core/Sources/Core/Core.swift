@_exported import Shared

/// Formate un prix en centimes : 450 → « 4,50 € ».
public func formatPrice(cents: Int64) -> String {
    let euros = cents / 100
    let rest = cents % 100
    return "\(euros),\(rest < 10 ? "0" : "")\(rest) €"
}
