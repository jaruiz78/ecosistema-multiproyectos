package com.proyecto.defensa.application;

import com.proyecto.defensa.domain.TacticalNode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TacticalMeshConsensusServiceTest {

    @Test
    void testVerifyQuorumSuccess() {
        TacticalMeshConsensusService service = new TacticalMeshConsensusService();
        List<TacticalNode> nodes = List.of(
                new TacticalNode("tac_01", "8828308281fffff", true, System.currentTimeMillis()),
                new TacticalNode("tac_02", "8828308281fffff", true, System.currentTimeMillis()),
                new TacticalNode("tac_03", "8828308281fffff", true, System.currentTimeMillis())
        );

        boolean quorumReached = service.verifyQuorum(nodes, 2);

        assertTrue(quorumReached);
    }
}
