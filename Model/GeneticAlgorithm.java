package Project.Model;

import Project.Model.DBfunctions;
import Project.Model.Graph;
import Project.Model.Volunteer;
import Project.Model.emergency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class GeneticAlgorithm {
    private static final double AREA_MATCH_BONUS = 0.5; // Adjust as needed
    private static final Random rand = new Random();
    private static Graph distanceGraph;

    public static List<Volunteer> findVolunteer(Map<String, List<Volunteer>> areaVolunteers, emergency emergency, Graph graph) {
        distanceGraph= graph;
        String emergencyAreaID = String.valueOf(DBfunctions.getAreaOfStreet(emergency.getStreet()));
        System.out.println("Emergency Area ID: " + emergencyAreaID);
        int emergencyPriority = emergency.getPriority();

        List<List<Volunteer>> population = initializePopulation(areaVolunteers, emergencyAreaID, 50, emergencyPriority);
        for (int i = 0; i < 1000; i++) { // Increase the number of generations
           // System.out.println("Generation: " + (i + 1));
            population = evolvePopulation(population, emergency, areaVolunteers, emergencyAreaID, emergencyPriority);
        }
        return evaluateBestSolution(population, emergency);
    }

    private static List<List<Volunteer>> initializePopulation(Map<String, List<Volunteer>> areaVolunteers, String emergencyAreaID, int size, int emergencyPriority) {
        List<List<Volunteer>> population = new ArrayList<>();
        List<Volunteer> availableVolunteers = new ArrayList<>(areaVolunteers.getOrDefault(emergencyAreaID, new ArrayList<>()));
        Collections.shuffle(availableVolunteers);
    
        // Handle case where no volunteers are available initially
        if (availableVolunteers.isEmpty()) {
            // Fallback strategy: fetch volunteers from adjacent areas or modify criteria
            availableVolunteers.addAll(getVolunteersFromAdjacentAreasOrLowerSkills(areaVolunteers, emergencyAreaID, emergencyPriority));
        }
    
        // Attempt to find a perfect skill match and form initial teams
        boolean foundPerfectMatch = false;
        for (Volunteer volunteer : availableVolunteers) {
            if (volunteer.getSkill() == emergencyPriority) {
                List<Volunteer> specialTeam = new ArrayList<>();
                specialTeam.add(volunteer);
                population.add(specialTeam);
                foundPerfectMatch = true;
                if (population.size() >= size) break;  // Ensuring the minimum population size is met quickly
            }
        }
    
        // Fill the population to the required size with diverse teams
        while (population.size() < size) {
            Set<Volunteer> team = new HashSet<>();
            while (team.size() < Math.min(rand.nextInt(3) + 2, availableVolunteers.size())) {
                team.add(availableVolunteers.get(rand.nextInt(availableVolunteers.size())));
            }
            population.add(new ArrayList<>(team));
        }
    
        return population;
    }
    
    private static List<Volunteer> getVolunteersFromAdjacentAreasOrLowerSkills(Map<String, List<Volunteer>> areaVolunteers, String emergencyAreaID, int emergencyPriority) {
        List<Volunteer> fallbackVolunteers = new ArrayList<>();
        return fallbackVolunteers;
    }
    
    
    private static List<List<Volunteer>> evolvePopulation(List<List<Volunteer>> population, emergency emergency, Map<String, List<Volunteer>> areaVolunteers, String emergencyAreaID, int emergencyPriority) {
        List<List<Volunteer>> newGeneration = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            List<Volunteer> parent1 = select(population, emergency);
            List<Volunteer> parent2 = select(population, emergency);
            List<Volunteer> offspring = crossover(parent1, parent2, emergencyPriority);
            mutate(offspring, areaVolunteers, emergencyAreaID);
            newGeneration.add(offspring);
        }
        return newGeneration;
    }

    private static List<Volunteer> select(List<List<Volunteer>> population, emergency emergency) {
        Optional<List<Volunteer>> perfectMatch = population.stream()
            .filter(team -> team.size() == 1 && team.get(0).getSkill() == emergency.getPriority())
            .findFirst();

        if (perfectMatch.isPresent()) {
         //   System.out.println("Selected perfect match team: " + perfectMatch.get());
            return perfectMatch.get();
        }

        return population.stream()
            .max(Comparator.comparingDouble(team -> calculateFitness(team, emergency)))
            .orElse(new ArrayList<>());
    }

    private static List<Volunteer> crossover(List<Volunteer> parent1, List<Volunteer> parent2, int emergencyPriority) {
        // Consider both the quality of the match and the size of the team
        Set<Volunteer> child = new HashSet<>();
        int targetSize = Math.max(parent1.size(), parent2.size());  // Aim for larger team sizes generally
        List<Volunteer> allParents = new ArrayList<>(parent1);
        allParents.addAll(parent2);
        Collections.shuffle(allParents); // Randomly mix volunteers from both parents

        for (Volunteer v : allParents) {
            if (child.size() < targetSize) {
                child.add(v);
            }
        }
        return new ArrayList<>(child);
    }

    private static void mutate(List<Volunteer> team, Map<String, List<Volunteer>> areaVolunteers, String emergencyAreaID) {
        if (rand.nextDouble() < 0.2 && !team.isEmpty()) { // Mutation rate of 20%, check if the team is not empty
            List<Volunteer> availableVolunteers = new ArrayList<>(areaVolunteers.getOrDefault(emergencyAreaID, new ArrayList<>()));
            availableVolunteers.removeAll(team); // Remove current team members from potential replacements
            
            if (!availableVolunteers.isEmpty()) {
                Volunteer replacement = availableVolunteers.get(rand.nextInt(availableVolunteers.size()));
                int indexToMutate = rand.nextInt(team.size());
                team.set(indexToMutate, replacement);
                //System.out.println("Mutation: Team member at index " + indexToMutate + " mutated to " + replacement.getName());
            }
        }
    }
    
    

    private static double calculateFitness(List<Volunteer> team, emergency emergency) {
        double totalSkill = team.stream().mapToDouble(Volunteer::getSkill).sum();
        double skillMatchScore = calculateSkillMatchScore(totalSkill, emergency.getPriority(), team.size());

        double distanceScore = team.stream()
            .mapToDouble(v -> -distanceGraph.getDistance(v.getStreet(),emergency.getStreet()))
            .sum(); // Negative to favor closer distances

        double fitness = skillMatchScore + distanceScore ;
        return fitness;
    }

    private static double calculateSkillMatchScore(double totalSkill, int emergencyPriority, int teamSize) {
        if (teamSize == 1 && totalSkill == emergencyPriority) {
            return 5.0; // Exact match with one volunteer
        } else if (teamSize > 1 && totalSkill >= emergencyPriority) {
            return 2.5; // Good for combined skills matching/exceeding with more volunteers
        }
        return 1.0 ;/// (Math.abs(totalSkill - emergencyPriority) + 1); // Less ideal matches
    }

    private static List<Volunteer> evaluateBestSolution(List<List<Volunteer>> population, emergency emergency) {
        List<Volunteer> bestTeam = population.stream()
            .max(Comparator.comparingDouble(team -> calculateFitness(team, emergency)))
            .orElse(new ArrayList<>());

        System.out.println("Selected best team with fitness score: " + calculateFitness(bestTeam, emergency));
        bestTeam.forEach(volunteer -> System.out.println("Selected Volunteer: " + volunteer.getName() + ", Skills: " + volunteer.getSkill() + ", Area: " + DBfunctions.getAreaOfStreet(volunteer.getStreet())));

        return bestTeam;
    }
}
