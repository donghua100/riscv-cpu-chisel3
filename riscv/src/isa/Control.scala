package isa
import chisel3._
import chisel3.util.ListLookup


object ControlSels{

  // imm select
  val IMM_X = 0.U(3.W)
  val IMM_I = 1.U(3.W)
  val IMM_S = 2.U(3.W)
  val IMM_B = 3.U(3.W)
  val IMM_U = 4.U(3.W)
  val IMM_J = 5.U(3.W)

  // A select
  val A_XXX = 0.U(1.W)
  val A_RS1 = 0.U(1.W)
  val A_PC  = 1.U(1.W)

  // B select
  val B_XXX = 0.U(1.W)
  val B_RS2 = 0.U(1.W)
  val B_IMM = 1.U(1.W)

  // WB select
  val WB_XXX = 0.U(2.W)
  val WB_ALU = 1.U(2.W)
  val WB_MEM = 2.U(2.W)
  val WB_PC = 3.U(2.W)

  // MEM read write
  val MemXX = 0.U(1.W)
  val MemRead = 1.U(1.W)
  
  val MemXXX = 0.U(1.W)
  val MemWrite = 1.U(1.W)

  // reg write
  val RegXXX = 0.U(1.W)
  val RegWen = 1.U(1.W)
  
  val BR_XXX = 0.U(3.W)
  val BR_EQ = 1.U(3.W)
  val BR_NEQ = 2.U(3.W)
  val BR_LT = 3.U(3.W)
  val BR_GE = 4.U(3.W)
  val BR_LTU = 5.U(3.W)
  val BR_GEU = 6.U(3.W)
  import cpu.AluSels._
  import Instrutions._
  val default = List(IMM_X,A_XXX,B_XXX,ALU_XXX,BR_XXX,MemXX,MemXXX,RegXXX,WB_XXX)

  val map = Array(
    ADDI -> (List(IMM_I,A_RS1,B_IMM,ALU_ADD,BR_XXX,MemXX,MemXXX,RegWen,WB_ALU)),
    )
}

class ControlSignals extends Bundle {
  val inst      = Input(UInt(32.W))
  val Imm_sel   = Output(UInt(3.W))
  val A_sel     = Output(UInt(1.W))
  val B_sel     = Output(UInt(1.W))
  val alu_op    = Output(UInt(4.W))
  val MemRead   = Output(Bool())
  val MemWrite  = Output(Bool())
  val BR_sel    = Output(UInt(3.W))
  val wen       = Output(Bool())
  val WB_sel    = Output(UInt(2.W))
}

class Control extends Module {
  val io = IO(new ControlSignals)
  val ctrlsignals = ListLookup(io.inst,ControlSels.default,ControlSels.map)
  io.Imm_sel := ctrlsignals(0) 
  io.A_sel := ctrlsignals(1)
  io.B_sel := ctrlsignals(2)
  io.alu_op := ctrlsignals(3)
  io.BR_sel := ctrlsignals(4)
  io.MemRead := ctrlsignals(5).asBool
  io.MemWrite := ctrlsignals(6).asBool
  io.wen := ctrlsignals(7).asBool
  io.WB_sel := ctrlsignals(8)
}


