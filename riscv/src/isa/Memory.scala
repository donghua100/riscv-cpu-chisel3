package isa

import chisel3._

class MemoryIO(xlen:Int) extends Bundle{
  val addr = Input(UInt(xlen.W))
  val wen = Input(Bool())
  val ren = Input(Bool())
  val wdata = Input(UInt(xlen.W))
  val rdata = Output(UInt(xlen.W))
}

class Memory extends Module {
  val io = IO(new MemoryIO(64))
  val DataMem = Mem(1024*1024*1024,UInt(64.W))
  when(io.wen){
    DataMem(io.addr) := io.wdata
  }.elsewhen(io.ren){
    io.rdata := DataMem(io.addr)
  }
}
