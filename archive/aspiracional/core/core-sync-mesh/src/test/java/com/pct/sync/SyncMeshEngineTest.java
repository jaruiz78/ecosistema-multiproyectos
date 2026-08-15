package com.pct.sync;

import com.corp.contracts.DifferentialSyncPatch;
import com.pct.sync.crdt.LwwRegister;
import com.pct.sync.crdt.OrSet;
import com.pct.sync.crdt.PnCounter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SyncMeshEngine - Pruebas TDD de Convergencia CRDT")
class SyncMeshEngineTest {

    @Test
    @DisplayName("LwwRegister resuelve conflictos monótonamente por timestamp y nodeId")
    void testLwwRegisterMerge() {
        LwwRegister<String> r1 = new LwwRegister<>("Estado A", 1000L, "node-1");
        LwwRegister<String> r2 = new LwwRegister<>("Estado B", 2000L, "node-2");

        LwwRegister<String> merged = r1.merge(r2);
        assertEquals("Estado B", merged.getValue());

        // Conmutatividad
        assertEquals(merged.getValue(), r2.merge(r1).getValue());
    }

    @Test
    @DisplayName("PnCounter converge a la suma exacta con incrementos/decrementos concurrentes")
    void testPnCounterMerge() {
        PnCounter c1 = new PnCounter();
        c1 = c1.increment("node-1", 10).decrement("node-1", 2); // 8

        PnCounter c2 = new PnCounter();
        c2 = c2.increment("node-2", 20).decrement("node-2", 5); // 15

        PnCounter merged1 = c1.merge(c2);
        PnCounter merged2 = c2.merge(c1);

        assertEquals(23, merged1.value());
        assertEquals(23, merged2.value());
    }

    @Test
    @DisplayName("OrSet gestiona adiciones y eliminaciones concurrentes sin resucitación accidental")
    void testOrSetConcurrentAddRemove() {
        OrSet<String> set1 = new OrSet<>();
        set1 = set1.add("PARCELA-42");

        OrSet<String> set2 = set1.add("PARCELA-99");
        set1 = set1.remove("PARCELA-42");

        OrSet<String> merged = set1.merge(set2);
        assertFalse(merged.contains("PARCELA-42"));
        assertTrue(merged.contains("PARCELA-99"));
    }

    @Test
    @DisplayName("SyncMeshEngine exporta y aplica parches diferenciales correctamente")
    void testDifferentialPatchSync() throws Exception {
        SyncMeshEngine clientEngine = new SyncMeshEngine("mobile-app-client");
        SyncMeshEngine serverEngine = new SyncMeshEngine("cloud-run-server");

        clientEngine.updateLww("irrigation-valve-1", "OPEN");
        DifferentialSyncPatch patch = clientEngine.exportLwwPatch("tenant-alicante", "irrigation-valve-1");

        assertNull(serverEngine.getLwwValue("irrigation-valve-1"));
        serverEngine.applyLwwPatch(patch);
        assertEquals("OPEN", serverEngine.getLwwValue("irrigation-valve-1"));
    }
}
