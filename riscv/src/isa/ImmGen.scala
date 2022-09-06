package isa

import chisel3._
import chisel3.util._
import utils._
class ImmGenIO extends Bundle {
  val inst = Input(UInt(32.W))
  val immI = Output(UInt(64.W))
  val immS = Output(UInt(64.W))
  val immB = Output(UInt(64.W))
  val immU = Output(UInt(64.W))
  val immJ = Output(UInt(64.W))
}

class  ImmGen extends Module{
  val io = IO(new ImmGenIO)
  io.immI       := SignExt(io.inst(31,20),64)
  io.immU       := SignExt(Cat(io.inst(31,12),0.U(12.W)),64)
  io.immS       := SignExt(Cat(io.inst(31,25),io.inst(11,7)),64)
  io.immB       := SignExt(Cat(io.inst(31),io.inst(7),io.inst(30,25),io.inst(11,8),0.U(1.W)),64)
  io.immJ       := SignExt(Cat(io.inst(31),io.inst(19,12),io.inst(20),io.inst(30,21),0.U(1.W)),64)
}
