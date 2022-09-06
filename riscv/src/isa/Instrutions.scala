package isa

import chisel3._
import chisel3.util.BitPat

object Instrutions{
  // Integer Register-Immediate Instructions
  def ADDI =      BitPat("b??????? ????? ????? 000 ????? 00100 11")
  def NOP  =      BitPat.bitPatToUInt(BitPat("b0000000 00000 00000 000 00000 00000 00"))
}
