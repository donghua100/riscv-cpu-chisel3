package isa
import chisel3._
import  chisel3.util._
import utils.SignExt


class IDU extends Module{
  val io = IO(new Bundle{
    val inst = Input(UInt(32.W))
    val rs1 = Output(UInt(5.W))
    val rs2 = Output(UInt(5.W))
    val rd = Output(UInt(5.W))
    val funct3 = Output(UInt(3.W))
    val funct7 = Output(UInt(7.W))
  })
  io.rs1        := io.inst(19,15)
  io.rs2        := io.inst(24,20)
  io.rd         := io.inst(11,7)
  io.funct3     := io.inst(14,12)
  io.funct7     := io.inst(31,25)
}
