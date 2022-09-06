import chisel3._
import cpu._
object Elaborate extends App{
  (new chisel3.stage.ChiselStage).execute(args,Seq(chisel3.stage.ChiselGeneratorAnnotation(()=> new top())))
  // println(getVerilogString(new DataPath()))
}
