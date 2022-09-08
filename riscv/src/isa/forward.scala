package isa

import chisel3._

object ForwardSel {
  val F_XXX     = 0.U(2.W)
  val F_SREG    = 0.U(2.W)
  val F_ALU     = 1.U(2.W)
  val F_MEM     = 2.U(2.W)
}

import isa.ControlSels._
import ForwardSel._

class ForwardIO extends Bundle {
  val idex_rs1    = Input(UInt(5.W))
  val idex_rs2    = Input(UInt(5.W))
  val exmem_rd    = Input(UInt(5.W))
  val memwb_rd    = Input(UInt(5.W))
  val exmem_wen   = Input(UInt(1.W))
  val memwb_wen   = Input(UInt(1.W))
  val forwardA    = Output(UInt(2.W))
  val forwardB    = Output(UInt(2.W))
}

class Forward extends Module {
  val io = IO(new ForwardIO)

  // default val
  io.forwardA := F_XXX
  io.forwardB := F_XXX

  when((io.exmem_wen===RegWen) && 
    (io.exmem_rd =/=0.U) && 
    (io.exmem_rd === io.idex_rs1)){
    io.forwardA := F_ALU
  }

  when((io.exmem_wen===RegWen) && 
    (io.exmem_rd =/=0.U) && 
    (io.exmem_rd === io.idex_rs2)){
    io.forwardB := F_ALU
  }

  when ((io.memwb_wen === RegWen) &&
    (io.memwb_rd =/= 0.U) &&
    !((io.exmem_wen ===RegWen) && (io.exmem_rd =/=0.U) && (io.exmem_rd === io.idex_rs1)) && 
    (io.memwb_rd === io.idex_rs1)) {
      io.forwardA := F_MEM
    }

  when ((io.memwb_wen === RegWen) &&
    (io.memwb_rd =/= 0.U) &&
    !((io.exmem_wen ===RegWen) && (io.exmem_rd =/=0.U) && (io.exmem_rd === io.idex_rs2)) && 
    (io.memwb_rd === io.idex_rs2)) {
      io.forwardB := F_MEM
    }
}
