# Design Choices and Implementation Rationale

This document explains the key design decisions made while implementing the **Priority with Round Robin CPU Scheduling Simulator**, and how those decisions satisfy both the **functional requirements** and the **constraints** of the assignment.

---

## 1. Programming Language Selection (Java)

Java was selected because:
- It is explicitly allowed by the assignment
- It provides strong type safety for modeling processes and scheduler state
- It allows precise control over data structures without relying on external libraries

All scheduling logic, queue manipulation, and priority handling are implemented **manually**, without using any built‑in scheduling or sorting utilities, as required.

---

## 2. Process Representation

Each process is modeled using a custom `Process` class containing:

- `id`: unique process identifier
- `type`: program type (`A`, `B`, or `C`)
- `priority`: numeric priority (lower value = higher priority)
- `remainingTime`: CPU ticks left to complete execution
- `executedTime`: CPU ticks already executed
- `signalsReceived`: total number of hardware signals received

### Rationale
This design keeps **execution state**, **scheduling state**, and **measurement state** (signals) encapsulated in a single structure, making it easy to:
- Track per‑process behavior over time
- Apply forking and signal rules correctly
- Report final statistics after completion

---

## 3. Ready Queue Implementation (Manual Array-Based Queue)

The ready queue is implemented using a **fixed‑size array** and a manual `size` counter.

Supported operations:
- `enqueue`: append a process at the end of the array
- `removeAt(index)`: remove a process by shifting remaining elements left

### Rationale
- The assignment prohibits the use of built‑in collections or helper libraries
- Manual array manipulation ensures full compliance
- The approach is simple, transparent, and sufficient for the problem scale

This design also makes the scheduling behavior explicit and easy to follow during grading.

---

## 4. Priority Selection Strategy

At each scheduling decision, the scheduler:
- Scans the entire ready queue
- Selects the process with the **smallest priority number**

This is implemented using a linear scan (`O(n)`).

### Rationale
- Priority values are static and known
- Linear scanning avoids prohibited sorting utilities
- The time complexity is acceptable given the bounded number of processes
- Ensures correctness and clarity over optimization

This directly reflects **priority scheduling**, where priority determines which process is selected next.

---

## 5. Round Robin Execution (Time Quantum = 4)

Once a process is selected:
- It executes for **up to 4 ticks**
- Execution stops early if the process finishes before the quantum expires

### Rationale
This enforces **round robin fairness within the priority policy**, preventing long-running processes from monopolizing the CPU while still respecting priority ordering.

The quantum value is fixed at `4` to exactly match the assignment requirements.

---

## 6. Forking Mechanism (Execution-Based)

Forking is triggered based on **executed CPU time**, not global time:

- Every **3 executed ticks**:
  - Process `A` forks a new process of type `B`
  - Process `B` forks a new process of type `C`
  - Process `C` does not fork

Newly forked processes are immediately enqueued into the ready queue.

### Rationale
- Forking rules are defined in terms of execution time per process
- Tracking `executedTime` per process ensures accurate behavior
- Immediate enqueue reflects real-world OS behavior where new processes enter the ready state

---

## 7. Hardware Signal Handling (Global Time-Based)

A global clock is maintained to track total CPU time elapsed.

- A hardware signal is generated **every 3 global ticks**
- The signal is delivered to the **currently running process**
- If a signal occurs exactly at a context switch boundary, it is received by the **newly executing process**

### Implementation Detail
Signals are processed **inside the per-tick execution loop**, ensuring:
- The currently running process is always the signal recipient
- Boundary conditions are handled naturally without special-case logic

### Rationale
This approach ensures strict adherence to the assignment’s signal delivery rule while keeping the logic simple and deterministic.

---

## 8. Completion Tracking and Reporting

When a process completes:
- It is removed from the ready queue
- It is stored in a separate `completed` array

After all processes finish:
- A **Gantt chart** is printed, showing execution intervals
- Each process’s total number of received signals is displayed

### Rationale
Separating completed processes from active scheduling simplifies:
- Final reporting
- Validation of correctness
- Debugging and traceability

---

## 9. Gantt Chart Construction

The Gantt chart is built incrementally by appending a segment after each CPU burst:

``
