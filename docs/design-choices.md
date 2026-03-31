# Design Notes — Priority + Round Robin Scheduler

## Why arrays instead of built-in collections?
The assignment restricts using built-in libraries/functions for core logic.
To comply, the ready queue is implemented as a fixed-size array:
- enqueue: append at end
- remove: manual shifting to fill gaps

## Priority selection
Because priorities are numeric and small, the simplest correct approach is:
- scan the entire queue and pick index with smallest priority value
Complexity: O(n) per dispatch

## Round Robin behavior
Quantum = 4
A process runs for up to 4 ticks unless it finishes earlier.

## Forking rule
Forking depends on the process’s executed CPU time:
- Every 3 executed ticks:
  - A creates a new B
  - B creates a new C

Newly created processes are appended to the queue immediately.

## Signal delivery rule
Global time drives signal delivery:
- every 3 ticks of *global time*
- delivered to the currently running process

Edge rule (signal at boundary):
If a signal occurs exactly at a context switch, the newly running process receives it.
Implementation detail:
Signals are counted *inside the per-tick execution loop*, ensuring the running process
at that moment is the receiver.
