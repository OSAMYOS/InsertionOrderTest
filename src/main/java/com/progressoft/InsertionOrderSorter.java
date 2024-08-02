package com.progressoft;

import java.util.*;

public class InsertionOrderSorter {
    private final Map<String, Set<String>> tables;

    public InsertionOrderSorter(Map<String, Set<String>> tables) {
        this.tables = tables;
    }

    public List<String> calculateInsertionOrder() {
        Map<String, Integer> dependencyCount = new HashMap<>();
        Map<String, List<String>> dependencyMap = new HashMap<>();

        for (String table : tables.keySet()) {
            dependencyCount.put(table, 0);
            dependencyMap.put(table, new ArrayList<>());
        }

        for (Map.Entry<String, Set<String>> entry : tables.entrySet()) {
            String table = entry.getKey();
            for (String dependency : entry.getValue()) {
                dependencyCount.put(table, dependencyCount.get(table) + 1);
                dependencyMap.get(dependency).add(table);
            }
        }

        Queue<String> queue = new LinkedList<>();
        for (String table : dependencyCount.keySet()) {
            if (dependencyCount.get(table) == 0) {
                queue.add(table);
            }
        }

        List<String> sortedOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            String table = queue.poll();
            sortedOrder.add(table);

            for (String dependent : dependencyMap.get(table)) {
                dependencyCount.put(dependent, dependencyCount.get(dependent) - 1);
                if (dependencyCount.get(dependent) == 0) {
                    queue.add(dependent);
                }
            }
        }

        if (sortedOrder.size() != tables.size()) {
            throw new IllegalStateException("Cycle detected");
        }

        return sortedOrder;
    }
}