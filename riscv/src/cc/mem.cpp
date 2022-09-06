#include <cstdio>
#include <cstdlib>
#include <ctime>
#include "mem.h"


static uint8_t pmem[MSIZE] PG_ALIGN = {};

uint8_t *guest_to_host(paddr_t paddr) {return pmem + paddr - MBASE;}
paddr_t host_to_guest(uint8_t *haddr) {return haddr - pmem + MBASE;}

static inline word_t host_read(void *addr, int len) {
  switch (len) {
    case 1: return *(uint8_t  *)addr;
    case 2: return *(uint16_t *)addr;
    case 4: return *(uint32_t *)addr;
    case 8: return *(uint64_t *)addr;
    default: assert(0);
  }
}

static inline void host_write(void *addr, int len, word_t data) {
  switch (len) {
    case 1: *(uint8_t  *)addr = data; return;
    case 2: *(uint16_t *)addr = data; return;
    case 4: *(uint32_t *)addr = data; return;
    case 8: *(uint64_t *)addr = data; return;
    default: assert(0);
  }
}

word_t pmem_read(paddr_t addr, int len) {
  word_t ret = host_read(guest_to_host(addr), len);
  return ret;
}

void pmem_write(paddr_t addr, int len, word_t data) {
  host_write(guest_to_host(addr), len, data);
}


void init_mem(){
	srand(time(NULL));
  uint32_t *p = (uint32_t *)pmem;
  int i;
  for (i = 0; i < (int) (MSIZE / sizeof(p[0])); i ++) {
    p[i] = rand();
  }
}


