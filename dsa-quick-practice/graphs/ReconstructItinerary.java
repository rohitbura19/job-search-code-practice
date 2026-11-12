package graphs;

import java.util.List;

// Reconstruct Itinerary  [Hard | Hierholzer's Eulerian path, min-heap per airport]
// Start at JFK, use every ticket once; lexicographically smallest itinerary.
//
// [[MUC,LHR],[JFK,MUC],[SFO,SJC],[LHR,SFO]]                    -> [JFK, MUC, LHR, SFO, SJC]
// [[JFK,SFO],[JFK,ATL],[SFO,ATL],[ATL,JFK],[ATL,SFO]]          -> [JFK, ATL, JFK, SFO, ATL, SFO]

public class ReconstructItinerary {

    static List<String> findItinerary(List<List<String>> tickets) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(findItinerary(List.of(
                List.of("MUC", "LHR"), List.of("JFK", "MUC"), List.of("SFO", "SJC"), List.of("LHR", "SFO"))));
        // [JFK, MUC, LHR, SFO, SJC]
        System.out.println(findItinerary(List.of(
                List.of("JFK", "SFO"), List.of("JFK", "ATL"), List.of("SFO", "ATL"), List.of("ATL", "JFK"), List.of("ATL", "SFO"))));
        // [JFK, ATL, JFK, SFO, ATL, SFO]
    }
}
