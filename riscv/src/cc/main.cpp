#include <cstdio>
#include <cstring>
#include <cassert>
#include "mem.h"
#include "verilated.h"
#include "verilated_vcd_c.h"
#include "Vtop.h"

VerilatedContext *contextp = NULL;
VerilatedVcdC *tfp = NULL;
Vtop *top = NULL;

void step_dump_wave(){
	top->eval();
	contextp->timeInc(1);
	tfp->dump(contextp->time());
}

void sim_init(){
	contextp = new VerilatedContext;
	tfp = new VerilatedVcdC;
	top = new Vtop;
	contextp->traceEverOn(true);
	top->trace(tfp,0);
	tfp->open("./build/dump.vcd");
}

void sim_exit(){
	step_dump_wave();
	tfp->close();
}

void single_cycle(){
	top->clock = 0; top->eval();
	top->clock = 1; top->eval();
}

void reset(int n){
	top->reset = 1;
	while(n > 0){
		single_cycle();
		n--;
	}
	top->reset = 0;
}


static const uint32_t img [] = {
	0x00000093, //addi x1 x0 0
	0x00108093,	//addi x1 x1 1 
  0x00000297,  // auipc t0,0
  0x0002b823,  // sd  zero,16(t0)
  0x0102b503,  // ld  a0,16(t0)
  0x00100073,  // ebreak (used as nemu_trap)
  0xdeadbeef,  // some data
};

int main(){
	init_mem();
	memcpy(guest_to_host(MBASE), img, sizeof(img));
	sim_init();
	reset(10);
	int n = 2;
	while(n>0){
		top->io_inst = pmem_read(top->io_pc,4);
		step_dump_wave();
		single_cycle();
		n--;
	}
	step_dump_wave();
	sim_exit();
}
