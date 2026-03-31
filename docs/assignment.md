# Assignment Requirements (Summary)

- Priority scheduling + Round Robin
- Priorities: A=2, B=3, C=1 (lower number = higher priority)
- CPU time required: A=10, B=7, C=5
- Fork every 3 ticks of execution:
  - A forks B
  - B forks C
- Two initial processes at t=0: P1(A), P2(B)
- Hardware signals arrive every 3 ticks of global time (first at t=3)
- Output:
  1) Gantt chart with correct intervals
  2) Total signals per process
- Constraint: no built-in helper libraries for scheduling/sorting operations
