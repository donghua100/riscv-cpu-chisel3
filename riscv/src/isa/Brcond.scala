package isa

import chisel3._
import isa.ControlSels._

class BrcondIO(xlen:Int) extends Bundle {
  val br_sel = Input(UInt(3.W))
  val a = Input(UInt(xlen.W))
  val b = Input(UInt(xlen.W))
  val taken = Output(Bool())
}

class Brcond extends Module {
  val io = IO(new BrcondIO(64))
  val eq = io.a === io.b
  val neq = !eq
  val lt = io.a.asSInt < io.b.asSInt
  val ge = !lt
  val ltu = io.a < io.b
  val geu = !ltu
  io.taken := Mux(io.br_sel===BR_EQ && eq 
                ||io.br_sel===BR_NEQ && neq
                ||io.br_sel===BR_LT && lt
                ||io.br_sel===BR_GE && ge
                ||io.br_sel===BR_LTU && ltu
                ||io.br_sel===BR_GEU && geu,true.B,false.B)
}
