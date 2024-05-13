package Project.Model;

import java.util.Map;

public class Graph {
    private double[][] distanceMatrix;
    private int streetCount;
    private static final double AREA_MATCH_BONUS = 0.5;

    public Graph(){
            this.streetCount = DBfunctions.fetchStreetCount();
            if (this.streetCount > 0) {
                this.distanceMatrix = new double[this.streetCount][this.streetCount];
                Map<Integer, Integer> areaMap = DBfunctions.fetchAreaMap();
                buildGraph(areaMap);
            } else {
                System.out.println("Failed to find the number of streets");
            }
    } 

    private void buildGraph(Map<Integer, Integer> areaMap){
        for (int i = 0; i < this.streetCount; i++) {
            for (int j = 0; j < this.streetCount; j++) {
                distanceMatrix[i][j] = Math.abs(i - j);
                if (areaMap.get(i + 1).equals(areaMap.get(j + 1))) {
                    distanceMatrix[i][j] += AREA_MATCH_BONUS;
                }
            }
        }
    }

    public double getDistance(int from, int to){
        return this.distanceMatrix[from - 1][to - 1];
    }

}
