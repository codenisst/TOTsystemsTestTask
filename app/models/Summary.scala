package models

case class Summary(var secid: String, var regnumber: String, var name: String, var emitentTitle: String,
                   var tradedate: String, var numtrades: Int, var open: Double, var close: Double)
