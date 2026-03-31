# Processes_Schedueler
Custom Priority Round-Robin scheduler (TQ=4) with dynamic process forking and periodic hardware fault signals . Built entirely from scratch without libraries  to generate Gantt charts and track signal counts , demonstrating complex CPU scheduling logic and efficiency .

# CPU Scheduling Simulator — Priority + Round Robin (Forking + Signals)

A Java simulation of a CPU scheduler using **Priority Scheduling with Round Robin (quantum = 4)**, including:
- **Process forking every 3 execution ticks** (A → forks B, B → forks C)
- **Periodic hardware signals every 3 global ticks** sent to the currently running process
- **Gantt chart output** showing execution intervals
- **Per-process signal totals** after completion

This project was implemented under the constraint of **no built-in scheduling/sorting libraries** — all queue operations and selection logic are written from scratch.

---

## Problem Statement (Assignment Summary)

We simulate three programs:

| Program | Priority | Total CPU Time |
|--------|----------|----------------|
| A      | 2        | 10 ticks       |
| B      | 3        | 7 ticks        |
| C      | 1        | 5 ticks        |

Rules:
- At `t = 0`, two processes arrive:
  - `P1` runs program **A**
  - `P2` runs program **B**
- Scheduling algorithm: **Priority with Round Robin**
  - Lower priority number = higher priority
  - Time quantum = **4**
- **Forking rule** (based on *executed CPU time* per process):
  - Every **3 executed ticks**
    - A forks a new process of type **B**
    - B forks a new process of type **C**
    - (C does not fork)
- **Hardware signal rule**
  - Every **3 global ticks** (first at `t=3`)
  - Signal is delivered to the **currently running** process
  - If a signal arrives exactly at a context switch boundary, it is received by the **newly executing** process

Expected output:
1. A correct **Gantt Chart** with time intervals  
2. Total **signals received by each process**

---

## Learning Outcomes Addressed

**CLO4:** Explain CPU scheduling algorithms and compare merits  
- Demonstrates **Priority Scheduling** + **Round Robin** working together
- Shows how different policies affect fairness and responsiveness

**CLO8:** Design + implement combined scheduling algorithms with efficient complexity  
- Scheduler logic built using arrays and manual operations (enqueue, remove, priority selection)
- Clear separation of concerns: selection, execution ticks, forking, signal delivery, completion tracking

---

## Implementation Overview

### Data Model
Each process tracks:
- `id`: unique process ID
- `type`: A, B, or C
- `priority`: numeric priority (lower is better)
- `remainingTime`: ticks left to complete
- `executedTime`: ticks executed so far
- `signalsReceived`: total signals delivered to this process

### Scheduler Core Logic
At each scheduling cycle:
1. Select the process with the **highest priority** (lowest priority number)
2. Execute for up to **quantum (4)** ticks or until completion
3. During each executed tick:
   - Increase global time
   - Decrease remaining time
   - Increase executed time
   - If `time % 3 == 0` → deliver signal to current process
   - If `executedTime % 3 == 0` → fork (A→B, B→C)
4. If completed → remove from queue and store in a completed list
5. Append an interval segment to the **Gantt chart**

---

## How To Run

Compile:
```bash
javac src/ProcessScheduler.java
