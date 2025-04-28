package org.example;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.function.Function;

public  class LongRunningTask implements Callable<String> {
    private final int taskId;

    public LongRunningTask(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public String call() throws Exception {
        Function<Void, Number> calculatingFunction;
        String result;
        if(taskId % 2 == 0) {
            result = calculateNPower().toString();
        } else {
            result = "" + calculateSqrt();
        }


        return "Result of task " + taskId + ": sum = " + result;
    }

    private double calculateSqrt() throws InterruptedException {
        double sum=0;
        for (int i = 1; i <= 100; i++) {
            Thread.sleep(100);
            sum+= Math.sqrt(i);
        }
        return sum;
    }

    private ArrayList<Integer> calculateNPower() throws InterruptedException {
        int sum=0;
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 29; i++) {
            Thread.sleep(100);
            numbers.add(nPower(i));
        }
        return numbers;
    }

    private int nPower(int number) {
        if(number == 0) return 1;

        return taskId * nPower(number - 1);
    }
}
