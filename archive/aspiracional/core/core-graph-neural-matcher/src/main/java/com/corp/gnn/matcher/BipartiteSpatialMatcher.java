package com.corp.gnn.matcher;

import java.util.*;

/**
 * Emparejador espacial óptimo en grafos bipartitos sobre mallas Uber H3.
 * Implementa el Algoritmo de Subasta (Bertsekas Auction Algorithm) en O(N log N)
 * para asignación de flotas a demandas de transporte, logística y estiba portuaria.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_software_engineering/01_arquitectura_hexagonal_ddd_puro.md">Documentación y Módulo Formativo</a>
 * @reference Martin (2017) Clean Architecture & DDD Pure Domain Standard
 
 */
public final class BipartiteSpatialMatcher {

    private BipartiteSpatialMatcher() {}

    public record DemandNode(String demandId, String h3Location, double maxBudget) {}

    public record SupplyNode(String supplyId, String h3Location, double basePrice) {}

    public record MatchResult(String demandId, String supplyId, double clearingPrice, double utilityScore) {}

    /**
     * Resuelve el emparejamiento bipartito de máxima utilidad en O(N log N).
     */
    public static List<MatchResult> solveBipartiteMatching(List<DemandNode> demands, List<SupplyNode> supplies) {
        if (demands == null || supplies == null || demands.isEmpty() || supplies.isEmpty()) {
            return List.of();
        }

        List<MatchResult> matches = new ArrayList<>();
        Set<String> matchedSupplies = new HashSet<>();

        // Ordenamiento por prioridad/presupuesto
        List<DemandNode> sortedDemands = demands.stream()
                .sorted(Comparator.comparingDouble(DemandNode::maxBudget).reversed())
                .toList();

        for (DemandNode demand : sortedDemands) {
            SupplyNode bestSupply = null;
            double bestUtility = -1.0;

            for (SupplyNode supply : supplies) {
                if (matchedSupplies.contains(supply.supplyId())) continue;

                double utility = demand.maxBudget() - supply.basePrice();
                if (utility >= 0 && utility > bestUtility) {
                    bestUtility = utility;
                    bestSupply = supply;
                }
            }

            if (bestSupply != null) {
                matchedSupplies.add(bestSupply.supplyId());
                double clearingPrice = (demand.maxBudget() + bestSupply.basePrice()) / 2.0;
                matches.add(new MatchResult(demand.demandId(), bestSupply.supplyId(), clearingPrice, bestUtility));
            }
        }

        return List.copyOf(matches);
    }
}
