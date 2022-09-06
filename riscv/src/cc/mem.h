#ifndef __MEM_H__
#define __MEM_H__
#include <cstdint>
#include <cassert>
#define MSIZE 0x8000000
#define MBASE 0x80000000

#if MBASE + MSIZE > 0x100000000ul
#define PMEM64 1
#endif

#ifndef PMEM64
typedef uint32_t paddr_t;
#else
typedef uint64_t paddr_t;
#endif

typedef uint64_t word_t;

#define PG_ALIGN __attribute((aligned(4096)))

uint8_t *guest_to_host(paddr_t paddr);
paddr_t host_to_guest(uint8_t *haddr);
word_t pmem_read(paddr_t addr, int len);

void pmem_write(paddr_t addr, int len, word_t data);
void init_mem();

#endif

