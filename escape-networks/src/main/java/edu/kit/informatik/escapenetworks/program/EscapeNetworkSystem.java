package edu.kit.informatik.escapenetworks.program;

import edu.kit.informatik.escapenetworks.network.Flow;
import edu.kit.informatik.escapenetworks.network.Network;
import edu.kit.informatik.escapenetworks.network.Vertex;
import edu.kit.informatik.util.Checks;
import edu.kit.informatik.util.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that encapsulates the state of the escape networks program.
 * <p>
 * Keeps track of the registered networks and their identifiers as well as
 * the flows calculated and cached for a given network.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class EscapeNetworkSystem {

    /**
     * Pattern that matches valid vertex names in a network.
     */
    public static final Pattern VERTEX_NAME_PATTERN = Pattern.compile("[a-z]{1,6}");

    /**
     * Pattern that matches valid network names.
     */
    public static final Pattern NETWORK_NAME_PATTERN = Pattern.compile("[A-Z]{1,6}");

    private final Map<String, Network> registeredNetworks;
    private final Map<String, Map<Pair<Vertex, Vertex>, Long>> flowCache;

    /**
     * Initialises a new escape system environment with an empty map
     * of registered networks and an empty flow cache.
     */
    public EscapeNetworkSystem() {
        this.registeredNetworks = new HashMap<>();
        this.flowCache = new HashMap<>();
    }

    /**
     * Looks up a registered network by its network name.
     *
     * @param networkId The name of the registered network to look for.
     * @return The empty optional if no network could be found for that name,
     * an optional containing the found network otherwise.
     */
    public Optional<Network> findNetwork(String networkId) {
        return Optional.ofNullable(registeredNetworks.get(networkId));
    }

    /**
     * Registers a network by associating it with an id/name.
     *
     * @param networkId The id of the network to register.
     * @param network   The network to associate the id with.
     * @throws NullPointerException     if the id or the network is {@code null}.
     * @throws IllegalArgumentException If the network or one of the vertices within
     *                                  does not match the name requirements.
     * @see #VERTEX_NAME_PATTERN
     * @see #NETWORK_NAME_PATTERN
     */
    public void registerNetwork(String networkId, Network network) {
        Checks.notNull(networkId, "Identifier");
        Checks.notNull(network, "Network");
        Checks.argument(NETWORK_NAME_PATTERN.matcher(networkId).matches(),
                "Illegal network identifier");
        Checks.argument(network.getGraph().getVertices().stream()
                        .map(Vertex::getIdentifier)
                        .map(VERTEX_NAME_PATTERN::matcher)
                        .allMatch(Matcher::matches),
                "Network contains vertices with illegal identifiers");
        registeredNetworks.put(networkId, network);
    }

    /**
     * Returns the maximum flow from {@code source} to {@code sink} in the network associated with
     * {@code networkId}.
     * <p>
     * First checks to see if this flow has already been calculated and returns the cached result if that is the case.
     * Otherwise, computes the result and caches it.
     *
     * @param networkId The id of the network to use for flow.
     * @param source The source of the flow.
     * @param sink The sink of the flow.
     * @return The flow from {@code source} to {@code sink}.
     * @throws NullPointerException if any of the arguments is {@code null}.
     * @throws IllegalArgumentException if the source/sink is not a valid source/sink for {@code network}.
     */
    public long computeMaxFlow(String networkId, Vertex source, Vertex sink) {
        Optional<Network> network = findNetwork(networkId);
        Checks.argument(network.isPresent(), "Network " + networkId + " is not registered");
        return flowCache
                .computeIfAbsent(networkId, (n) -> new HashMap<>())
                .computeIfAbsent(Pair.of(source, sink), (pair) -> Flow.computeMaxFlow(network.get(), source, sink));
    }

    /**
     * Returns an unmodifiable view of the networks currently registered.
     *
     * @return the registered networks.
     */
    public Map<String, Network> getRegisteredNetworks() {
        return Collections.unmodifiableMap(registeredNetworks);
    }

    /**
     * Returns an unmodifiable view of the flow cache map for the given network id.
     *
     * @param networkId The network id to get the flow cache for.
     * @return The cached flows, or the empty map if no flows have been cached.
     */
    public Map<Pair<Vertex, Vertex>, Long> getCachedFlows(String networkId) {
        return Collections.unmodifiableMap(flowCache.getOrDefault(networkId, Map.of()));
    }

    /**
     * Invalidates the flow cache for the given network id, in other words, clears the cache.
     * <p>
     * Does nothing if the network doesn't exist.
     *
     * @param networkId The network id to clear the associated cache for.
     */
    public void invalidateFlowCache(String networkId) {
        flowCache.remove(networkId);
    }
}
