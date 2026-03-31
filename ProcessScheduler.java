class ProcessScheduler {

    static class Process {
        int id;
        char type; // A, B, C
        int priority;
        int remainingTime;
        int executedTime;
        int signalsReceived;

        public Process(int id, char type, int priority, int totalTime) {
            this.id = id;
            this.type = type;
            this.priority = priority;
            this.remainingTime = totalTime;
            this.executedTime = 0;
            this.signalsReceived = 0;
        }
    }

    static final int MAX = 100;

    static Process[] queue = new Process[MAX];
    static int size = 0;

    // store completed processes
    static Process[] completed = new Process[MAX];
    static int completedSize = 0;

    static int processCounter = 3; // P1, P2 already exist

    static void enqueue(Process p) {
        queue[size++] = p;
    }

    static void removeAt(int index) {
        for (int i = index; i < size - 1; i++) {
            queue[i] = queue[i + 1];
        }
        size--;
    }

    static int getHighestPriorityIndex() {
        int idx = 0;
        for (int i = 1; i < size; i++) {
            if (queue[i].priority < queue[idx].priority) {
                idx = i;
            }
        }
        return idx;
    }

    static Process createProcess(char type) {
        if (type == 'A') return new Process(processCounter++, 'A', 2, 10);
        if (type == 'B') return new Process(processCounter++, 'B', 3, 7);
        return new Process(processCounter++, 'C', 1, 5);
    }

    public static void main(String[] args) {

        int time = 0;
        int quantum = 4;

        // Initial processes
        enqueue(new Process(1, 'A', 2, 10));
        enqueue(new Process(2, 'B', 3, 7));

        String gantt = "";

        while (size > 0) {

            int idx = getHighestPriorityIndex();
            Process current = queue[idx];

            int startTime = time; // track actual start

            int runTime = (current.remainingTime < quantum) ? current.remainingTime : quantum;

            for (int t = 0; t < runTime; t++) {

                time++;
                current.remainingTime--;
                current.executedTime++;

                // SIGNAL every 3 ticks
                if (time % 3 == 0) {
                    current.signalsReceived++;
                }

                // FORKING
                if (current.executedTime % 3 == 0) {
                    if (current.type == 'A') {
                        enqueue(createProcess('B'));
                    } else if (current.type == 'B') {
                        enqueue(createProcess('C'));
                    }
                }

                if (current.remainingTime == 0) break;
            }

            gantt += "| P" + current.id + " (" + startTime + "-" + time + ") ";

            // store completed process BEFORE removing
            if (current.remainingTime == 0) {
                completed[completedSize++] = current;
                removeAt(idx);
            }
        }

        gantt += "|";

        // OUTPUT 
        System.out.println("Gantt Chart:");
        System.out.println(gantt);

        System.out.println("\nSignal Count Per Process:");

        for (int i = 0; i < completedSize; i++) {
            System.out.println(
                "P" + completed[i].id +
                " (" + completed[i].type + ") -> " +
                completed[i].signalsReceived + " signals"
            );
        }
    }
}