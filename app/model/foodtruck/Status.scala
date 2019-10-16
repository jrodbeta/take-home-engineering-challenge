package model.foodtruck

// Sealed means it's exhaustive to what's in this file
// Referencing
sealed trait Status

case object Approved extends Status
case object Expired extends Status
case object Inactive extends Status
case object Issued extends Status
case object OnHold extends Status
case object Requested extends Status
case object Suspend extends Status

