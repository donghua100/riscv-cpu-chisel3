package isa

import chisel3._

class HazardIo extends Bundle {
  val idex_mren   = Input(UInt(1.W))
  val idex_rd     = Input(UInt(5.W))
  val rs1         = Input(UInt(5.W))
  val rs2         = Input(UInt(5.W))
  val stall       = Output(Bool())
}
import isa.ControlSels.MemRead
class Hazard extends Module {
  val io = IO(new HazardIo)
  io.stall := false.B
  when (io.idex_mren === MemRead && (io.idex_rd === io.rs1 || io.idex_rd === io.rs2)) {
    io.stall := true.B
  }

}
