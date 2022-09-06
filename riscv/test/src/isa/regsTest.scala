import chisel3._
import chiseltest._
import  org.scalatest.flatspec.AnyFlatSpec
import isa._
class regsTest extends AnyFlatSpec with ChiselScalatestTester{
  behavior of "regs"
  it should "read data in regs" in {
    test(new RegisterFile){c=>
      c.io.wen.poke(false.B)
      c.io.rs1.poke(0.U)
      c.io.rs2.poke(3.U)
      c.clock.step()
      c.io.rdata1.expect(0.U)
      c.io.rdata2.expect(0.U)
      
      c.io.wen.poke(true.B)
      c.io.wdata.poke(10000.U)
      c.io.dest.poke(3.U)
      c.clock.step()

      c.io.wen.poke(false.B)
      c.io.rs1.poke(0.U)
      c.io.rs2.poke(3.U)
      c.clock.step()
      c.io.rdata1.expect(0.U)
      c.io.rdata2.expect(10000.U)
    }
  }
}
