package com.gmail.ikurenkov88.ebit.test.service.calculation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ForkJoinPoolFactoryBean;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.DoubleAccumulator;

/**
 * Created by ilia on 28.02.16.
 */

@Service
public class HumanPyramidCalcImpl implements HumanPyramidCalc {
    @Autowired
    ForkJoinPoolFactoryBean joinPoolFactoryBean;

    @Value("${pyramid.block.weight}")
    double weight;

    @Override
    public double getHumanEdgeWeight(int level, int index) {
        PyramidTask pyramidTask = PyramidTaskFactory.getTask(weight, level, index);
        joinPoolFactoryBean.getObject().invoke(pyramidTask);
        Double join = pyramidTask.join();
        return join;
    }

    static class PyramidTask extends RecursiveTask<Double> {
        Double weight;
        int level;
        int index;

        public PyramidTask(double weight, int level, int index) {
            this.weight = weight;
            this.level = level;
            this.index = index;
        }

        boolean isLeft() {
            return (index == 0);
        }

        boolean isRight() {
            return index == level;
        }

        @Override
        protected Double compute() {
            if (level == 0 || index < 0 || index > level) return 0d;
            ForkJoinTask<Double> pyramidTaskLeft =
                    PyramidTaskFactory.getTask(weight, level - 1, index - 1).fork();
            ForkJoinTask<Double> pyramidTaskRight =
                    PyramidTaskFactory.getTask(weight, level - 1, index).fork();

            double result = pyramidTaskLeft.join() / 2 + pyramidTaskRight.join() / 2 + weight / 2;
            if (!isLeft() && !isRight())
                result += weight / 2;
            return result;

        }

    }

    static class PyramidTaskFactory {
        static protected Map<AbstractMap.SimpleImmutableEntry<Integer, Integer>, PyramidTask> taskMap = new ConcurrentHashMap<>();

        static public PyramidTask getTask(double weight, int level, int index) {
            AbstractMap.SimpleImmutableEntry<Integer, Integer> pair = new AbstractMap.SimpleImmutableEntry<>(level, index);
            return taskMap.computeIfAbsent(pair, p ->
                    new PyramidTask(weight, level, index)
            );
        }
    }
}
