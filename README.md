# RISCV64 CPU
We choose chisel3 to implement
RISCV64  5-stage Pipeline CPU desgined using chisel3.Now it's just a framework.The memory is simluated by cpp code to make tesing easily.
## Feature
- Pipeline
- Data Hazard

=======================


## Getting Started

First, install mill by referring to the documentation [here](https://com-lihaoyi.github.io/mill).
Then, install verilator by referring to the documentation [here](https://github.com/verilator/verilator).
To watch vcd files,gtkwave is nedded.

To run all tests in this design (recommended for test-driven development):
```bash
make test
```

To generate Verilog:
```bash
make verilog
```
## DataPath
[!Datapath](img/datapath.png "DataPath")

