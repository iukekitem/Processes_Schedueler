# Pseudocode — Scheduler Simulation

Initialize global time = 0
quantum = 4

Create P1(type A, priority 2, time 10)
Create P2(type B, priority 3, time 7)
enqueue P1, enqueue P2

while queue not empty:
    idx = index of smallest priority in queue
    current = queue[idx]
    startTime = time

    runTime = min(quantum, current.remainingTime)

    repeat runTime times:
        time++
        current.remainingTime--
        current.executedTime++

        if time % 3 == 0:
            current.signalsReceived++

        if current.executedTime % 3 == 0:
            if current.type == A: enqueue(new B)
            if current.type == B: enqueue(new C)

        if current.remainingTime == 0:
            break

    append "| Pid (startTime-time)" to gantt

    if current.remainingTime == 0:
        store current in completed list
        remove current from queue

print gantt
print signals per completed process
