package utils
import chisel3._
import chisel3.util._

object SignExt{
  def apply(a:UInt,len:Int) = {
    val alen = a.getWidth
    val signBit = a(alen-1)
    if (alen>=len) a(len-1,0) else Cat(Fill(len-alen,signBit),a)
  }
}


object ZeroExt{
  def apply(a:UInt,len:Int) = {
    val alen = a.getWidth
    if (alen>=len) a(len-1,0) else Cat(0.U((len-alen).W),a)
  }
}
