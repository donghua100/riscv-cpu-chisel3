package cpu
import chisel3._
import chisel3.util.MuxLookup
import isa._
import isa.ControlSels._


class DataPathIO extends Bundle { 
  val inst = Input(UInt(32.W))
  val rdata = Input(UInt(64.W))
  val wen = Output(Bool())
  val ren = Output(Bool())
  val addr = Output(UInt(64.W))
  val wdata = Output(UInt(64.W))
  val pc = Output(UInt(64.W))

}


class top extends Module {
  val io = IO(new DataPathIO) 
  //val ifu = Module(new IFU)
  val idu = Module(new IDU)
  // val mem = Module(new Memory)
  val immgen = Module(new ImmGen)
  val ctrl = Module(new Control)
  val br = Module(new Brcond)
  val alu = Module(new Alu())
  val rf = Module(new RegisterFile)

  // ifu.io.pc := pc
  // val pc4 = pc + 4.U
  // idu.io.inst :=ifu.io.inst
  // ctrl.io.inst := ifu.io.inst
  // immgen.io.inst := ifu.io.inst
  val pc = RegInit(0x80000000L.U(64.W))
  io.pc := pc
  idu.io.inst :=  io.inst
  val pc4 = pc + 4.U
  ctrl.io.inst := io.inst
  immgen.io.inst := io.inst

  rf.io.rs1 := idu.io.rs1
  rf.io.rs2 := idu.io.rs2
  val imm = MuxLookup(
    ctrl.io.Imm_sel,
    immgen.io.immI,
    Seq(
      IMM_I->immgen.io.immI,
      IMM_S->immgen.io.immS,
      IMM_B->immgen.io.immB,
      IMM_U->immgen.io.immU,
      IMM_J->immgen.io.immJ,
      ))
  val brpc = io.pc + imm
  br.io.br_sel := ctrl.io.BR_sel
  br.io.a := rf.io.rdata1
  br.io.b := rf.io.rdata2
  pc := Mux(br.io.taken,brpc,pc4)
  alu.io.alu_op := ctrl.io.alu_op
  alu.io.A := Mux(ctrl.io.A_sel=== A_RS1,rf.io.rdata1,pc)
  alu.io.B := Mux(ctrl.io.B_sel=== B_RS2,rf.io.rs2,imm)
  io.wen := ctrl.io.MemWrite
  io.ren := ctrl.io.MemRead
  io.addr := alu.io.out
  io.wdata := rf.io.rdata2
  // mem.io.wen := ctrl.io.MemWrite
  // mem.io.ren := ctrl.io.MemRead
  // mem.io.addr := alu.io.out
  // mem.io.wdata := rf.io.rdata2
  rf.io.wen := ctrl.io.wen
  rf.io.dest := idu.io.rd
  rf.io.wdata := Mux(ctrl.io.WB_sel===WB_ALU,alu.io.out,io.rdata)

}
