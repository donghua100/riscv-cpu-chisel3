package cpu
import chisel3._
import chisel3.util.MuxLookup
import isa._
import isa.ControlSels._
import chisel3.experimental.BundleLiterals._

// class DataPathIO extends Bundle { 
//   val inst = Input(UInt(32.W))
//   val rdata = Input(UInt(64.W))
//   val wen = Output(Bool())
//   val ren = Output(Bool())
//   val addr = Output(UInt(64.W))
//   val wdata = Output(UInt(64.W))
//   val pc = Output(UInt(64.W))
//
// }

class PipeLineDataPathIO extends Bundle {
  val pc = Input(UInt(64.W))
}

class IFID_REG(xlen: Int) extends Bundle {
  val inst  = chiselTypeOf(Instrutions.NOP)
  val pc    = UInt(xlen.W)
}

class IDEX_REG(xlen: Int) extends Bundle {
  // ctrl signals
  val A_sel     =   UInt(1.W)
  val B_sel     =   UInt(1.W)
  val ALU_op    =   UInt(4.W)
  val BR_sel    =   UInt(3.W)
  val WB_sel    =   UInt(2.W)
  val Wen       =   UInt(1.W)
  val MWen      =   UInt(1.W)
  val MRen      =   UInt(1.W)
  // data
  val dst       =   UInt(5.W)
  val Imm       =   UInt(xlen.W)
  val rdata1    =   UInt(xlen.W)
  val rdata2    =   UInt(xlen.W)
  val pc        =   UInt(xlen.W)
}

class EXMEM_REG(xlen:Int) extends Bundle {
  val Wen       = UInt(1.W)
  val MWen      = UInt(1.W)
  val MRen      = UInt(1.W)
  val WB_sel    = UInt(2.W)

  val dst       = UInt(5.W)
  val pc        = UInt(xlen.W)
  val ALU_out   = UInt(xlen.W)
  val rdata2    = UInt(xlen.W)

}

class MEMWB_REG(xlen:Int) extends Bundle {
  val Wen       = UInt(1.W)
  val dst       = UInt(5.W)
  val WB_sel    = UInt(2.W)
  val ALU_out   = UInt(xlen.W)
  val mdata     = UInt(xlen.W)
  val pc        = UInt(xlen.W)
}

  
class top extends Module {
  val io      = IO(new PipeLineDataPathIO)
  val imem    = Module(new Memory)
  // val ifu     = Module(new IFU)
  val id      = Module(new IDU)
  val rf      = Module(new RegisterFile)
  val ctrl    = Module(new Control)
  val immgen  = Module(new ImmGen)
  val alu     = Module(new Alu)
  val br      = Module(new Brcond)
  val dmem    = Module(new Memory)

  val ifid_reg = RegInit(
    (new IFID_REG(64)).Lit(
      _.inst    -> Instrutions.NOP,
      _.pc      -> 0.U
      )
    )
  import cpu.AluSels._
  val idex_reg = RegInit(
    (new IDEX_REG(64)).Lit(
      _.A_sel     -> A_XXX,
      _.B_sel     -> B_XXX,
      _.ALU_op    -> ALU_XXX,
      _.BR_sel    -> BR_XXX,
      _.WB_sel    -> WB_XXX,
      _.Wen       -> RegXXX,
      _.MRen      -> MemXX,
      _.MWen      -> MemXXX,
      _.dst       -> 0.U,
      _.Imm       -> 0.U,
      _.rdata1    -> 0.U,
      _.rdata2    -> 0.U,
      _.pc        -> 0.U
      )
    )

  val exmem_reg = RegInit(
    (new EXMEM_REG(64)).Lit(
      _.Wen       -> RegXXX,
      _.MRen      -> MemXX,
      _.MWen      -> MemXXX,
      _.WB_sel    -> WB_XXX,
      _.dst       -> 0.U,
      _.pc        -> 0.U,
      _.ALU_out   -> 0.U,
      _.rdata2    -> 0.U
      )
    )

  val memwb_reg = RegInit(
    (new MEMWB_REG(64)).Lit(
      _.Wen       -> RegXXX,
      _.dst       -> 0.U,
      _.WB_sel    -> WB_XXX,
      _.ALU_out   -> 0.U,
      _.mdata     -> 0.U,
      _.pc        -> 0.U
      )
    )


  
  // val pc = RegInit(0x80000000L.U(64.W))
  val pc = RegInit(0.U(64.W))

  // load mem
  // loadmem(imem)
  imem.io.ren := true.B
  imem.io.addr := pc
  imem.io.wen  := false.B
  imem.io.wdata := 0.U
  // if
  ifid_reg.inst       := imem.io.rdata
  ifid_reg.pc         := pc

  // id
  id.io.inst        := ifid_reg.inst
  immgen.io.inst    := ifid_reg.inst
  ctrl.io.inst      := ifid_reg.inst
  rf.io.rs1         := id.io.rs1
  rf.io.rs2         := id.io.rs2
  val imm = MuxLookup(ctrl.io.Imm_sel,
    immgen.io.immI,
    Seq(
      IMM_I     -> immgen.io.immI,
      IMM_S     -> immgen.io.immS,
      IMM_B     -> immgen.io.immB,
      IMM_U     -> immgen.io.immU,
      IMM_J     -> immgen.io.immJ,
      )
    )
  idex_reg.dst      := id.io.rd
  idex_reg.A_sel    := ctrl.io.A_sel
  idex_reg.B_sel    := ctrl.io.B_sel
  idex_reg.ALU_op   := ctrl.io.alu_op
  idex_reg.BR_sel   := ctrl.io.BR_sel
  idex_reg.WB_sel   := ctrl.io.WB_sel
  idex_reg.Wen      := ctrl.io.wen
  idex_reg.MRen     := ctrl.io.MemRead
  idex_reg.MWen     := ctrl.io.MemWrite
  idex_reg.Imm      := imm
  idex_reg.rdata1   := rf.io.rdata1
  idex_reg.rdata2   := rf.io.rdata2
  idex_reg.pc       := ifid_reg.pc
  
  // ex
  alu.io.alu_op     := idex_reg.ALU_op
  alu.io.A          := Mux(idex_reg.A_sel === A_RS1, idex_reg.rdata1, idex_reg.pc)
  alu.io.B          := Mux(idex_reg.B_sel === B_RS2, idex_reg.rdata2, idex_reg.Imm)
  br.io.a           := idex_reg.rdata1
  br.io.b           := idex_reg.rdata2
  br.io.br_sel      := idex_reg.BR_sel
  exmem_reg.Wen     := idex_reg.Wen
  exmem_reg.MRen    := idex_reg.MRen
  exmem_reg.MWen    := idex_reg.MWen
  exmem_reg.WB_sel  := idex_reg.WB_sel
  exmem_reg.dst     := idex_reg.dst
  exmem_reg.pc      := idex_reg.pc
  exmem_reg.ALU_out := alu.io.out
  exmem_reg.rdata2  := idex_reg.rdata2
  val dnpc          = idex_reg.pc + idex_reg.Imm
  val pc4           = idex_reg.pc + 4.U
  pc                := Mux(br.io.taken, dnpc, pc4)
  
  // mem
  dmem.io.wen       := exmem_reg.MWen
  dmem.io.ren       := exmem_reg.MRen
  dmem.io.addr      := exmem_reg.ALU_out
  dmem.io.wdata     := exmem_reg.rdata2
  memwb_reg.Wen     := exmem_reg.Wen
  memwb_reg.dst     := exmem_reg.dst
  memwb_reg.ALU_out := exmem_reg.ALU_out
  memwb_reg.mdata   := dmem.io.rdata
  memwb_reg.pc      := exmem_reg.pc
  memwb_reg.WB_sel  := exmem_reg.WB_sel

  // wb
  rf.io.wen         := memwb_reg.Wen
  rf.io.dest        := memwb_reg.dst
  rf.io.wdata       := Mux(memwb_reg.WB_sel===WB_ALU, memwb_reg.ALU_out, memwb_reg.mdata)
}
