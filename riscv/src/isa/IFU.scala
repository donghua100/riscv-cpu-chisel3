package isa
import chisel3._
class IFU extends Module {
  val io = IO(new Bundle{
    val pc = Input(UInt(64.W))
    val inst = Output(UInt(32.W))
  })
  val IMem = Mem(1024,UInt(32.W))
  io.inst := IMem(io.pc)
} 
