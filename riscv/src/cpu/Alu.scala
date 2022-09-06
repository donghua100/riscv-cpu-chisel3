package cpu
import chisel3._
import chisel3.util._

object AluSels{
  val ALU_ADD   = 0.U(4.W)
  val ALU_SUB   = 1.U(4.W)
  val ALU_OR    = 2.U(4.W)
  val ALU_AND   = 3.U(4.W)
  val ALU_XOR   = 4.U(4.W)
  val ALU_XXX   = 15.U(4.W)
}

class AluIO(width:Int) extends Bundle{
  val alu_op = Input(UInt(4.W))
  val A = Input(UInt(width.W))
  val B = Input(UInt(width.W))
  val out = Output(UInt(width.W))
}

import AluSels._
class Alu extends Module{
  val io = IO(new AluIO(64))
  io.out := MuxLookup(
    io.alu_op,
    io.B,
    Seq(
      ALU_ADD -> (io.A + io.B),
      ALU_SUB -> (io.A - io.B),
      ALU_AND -> (io.A & io.B),
      ALU_OR  -> (io.A | io.B),
      ALU_XOR -> (io.A ^ io.B),
      )
    ) 
}
