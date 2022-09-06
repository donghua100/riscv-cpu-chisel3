package isa

import chisel3._
class RegisterFile extends Module{
  val io = IO(new Bundle{
    val wen = Input(Bool())
    val rs1 = Input(UInt(5.W))
    val rs2 = Input(UInt(5.W))
    val dest = Input(UInt(5.W))
    val wdata = Input(UInt(64.W))
    val rdata1 = Output(UInt(64.W))
    val rdata2 = Output(UInt(64.W))
  })
  val initValues = Seq.fill(32){0.U(64.W)}
  val regs = RegInit(VecInit(initValues))
  // val regs = Mem(32,UInt(64.W))
  io.rdata1 := Mux(io.rs1.orR,regs(io.rs1),0.U)
  io.rdata2 := Mux(io.rs2.orR,regs(io.rs2),0.U)
  when (io.wen && io.dest.orR){
    regs(io.dest) := io.wdata
  }
}

