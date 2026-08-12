#!/usr/bin/env python3
"""
test_duckdb_ram_stress.py - Empirical Stress & RAM Measurement Harness for DuckDB Parquet OLAP.
Empirical Challenger - Milestone 4 (AppViajes)
"""

import sys
sys.path.insert(0, '/home/jaruiz/.local/lib/python3.14/site-packages')
import os
import time
import json
import psutil
import duckdb
import numpy as np
from concurrent.futures import ThreadPoolExecutor

PARQUET_PATH = "/home/jaruiz/Desarrollo/AppViajes/services/frontend-web/public/data/h3_itineraries_analytics.parquet"
OUTPUT_REPORT = "/home/jaruiz/Desarrollo/.agents/challenger_m4/duckdb_ram_empirical_results.json"

def get_process_memory_mb():
    process = psutil.Process(os.getpid())
    return process.memory_info().rss / (1024 * 1024)

def run_empirical_duckdb_tests():
    print("==========================================================================")
    print(" EMPIRICAL STRESS HARNESS: DuckDB WASM / Parquet OLAP Memory & Speed Test ")
    print("==========================================================================")

    if not os.path.exists(PARQUET_PATH):
        raise FileNotFoundError(f"Parquet file not found at {PARQUET_PATH}")

    file_size_mb = os.path.getsize(PARQUET_PATH) / (1024 * 1024)
    print(f"Parquet File Path : {PARQUET_PATH}")
    print(f"Parquet File Size : {file_size_mb:.2f} MB")

    # 1. Metadata inspection via DuckDB
    con_meta = duckdb.connect()
    meta_df = con_meta.execute(f"SELECT * FROM parquet_metadata('{PARQUET_PATH}')").df()
    row_groups_count = meta_df['row_group_id'].nunique()
    total_rows = con_meta.execute(f"SELECT COUNT(*) FROM read_parquet('{PARQUET_PATH}')").fetchone()[0]
    print(f"Total Rows In File: {total_rows:,}")
    print(f"Row Groups Count  : {row_groups_count}")
    print(f"Rows Per Group    : {total_rows // row_groups_count:,}")
    con_meta.close()

    results = {
        "file_size_mb": round(file_size_mb, 2),
        "total_rows": int(total_rows),
        "row_groups_count": int(row_groups_count),
        "tests": []
    }

    # 2. Memory Constrained Tests: SET max_memory='20MB', '15MB', '10MB', '5MB', '2MB'
    memory_limits = ['20MB', '15MB', '10MB', '5MB', '2MB']

    for mem_limit in memory_limits:
        print(f"\n--- Testing with DuckDB PRAGMA max_memory = '{mem_limit}' ---")
        mem_before = get_process_memory_mb()
        
        con = duckdb.connect(database=':memory:')
        try:
            con.execute(f"SET max_memory='{mem_limit}'")
            con.execute("SET threads=2")

            # Test A: Full aggregation across all 500k rows
            t0 = time.perf_counter()
            query_full = f"""
                SELECT h3_cell, 
                       COUNT(*) as total_bookings, 
                       AVG(revenue) as avg_rev, 
                       AVG(latency_ms) as avg_lat
                FROM read_parquet('{PARQUET_PATH}')
                GROUP BY h3_cell
                ORDER BY total_bookings DESC
            """
            res_full = con.execute(query_full).fetchall()
            t_full_ms = (time.perf_counter() - t0) * 1000.0
            mem_peak = get_process_memory_mb()
            mem_used_delta = mem_peak - mem_before
            status_full = "SUCCESS"
            rows_retrieved = len(res_full)
        except Exception as e:
            t_full_ms = (time.perf_counter() - t0) * 1000.0
            mem_peak = get_process_memory_mb()
            mem_used_delta = mem_peak - mem_before
            status_full = f"FAILED: {e}"
            rows_retrieved = 0

        print(f"  [Full Query] Status: {status_full} | Latency: {t_full_ms:.2f} ms | Peak RAM Delta: {mem_used_delta:.2f} MB | Rows: {rows_retrieved}")

        # Test B: Point Query by h3_cell (Data Skipping)
        sample_h3 = 620000000000000005
        t0 = time.perf_counter()
        query_point = f"""
            SELECT h3_cell, COUNT(*), AVG(revenue), AVG(latency_ms)
            FROM read_parquet('{PARQUET_PATH}')
            WHERE h3_cell = {sample_h3}
            GROUP BY h3_cell
        """
        try:
            res_point = con.execute(query_point).fetchall()
            t_point_ms = (time.perf_counter() - t0) * 1000.0
            mem_peak_point = get_process_memory_mb()
            mem_used_point_delta = mem_peak_point - mem_before
            status_point = "SUCCESS"
        except Exception as e:
            t_point_ms = (time.perf_counter() - t0) * 1000.0
            mem_peak_point = get_process_memory_mb()
            mem_used_point_delta = mem_peak_point - mem_before
            status_point = f"FAILED: {e}"

        print(f"  [Point Query] Status: {status_point} | Latency: {t_point_ms:.2f} ms | Peak RAM Delta: {mem_used_point_delta:.2f} MB")

        # Test C: Benchmark 50 Repeated Queries under memory constraint
        latencies = []
        if status_point == "SUCCESS":
            for _ in range(50):
                t_iter_0 = time.perf_counter()
                con.execute(f"SELECT COUNT(*) FROM read_parquet('{PARQUET_PATH}') WHERE h3_cell = {sample_h3}").fetchall()
                latencies.append((time.perf_counter() - t_iter_0) * 1000.0)

            p50 = float(np.median(latencies))
            p95 = float(np.percentile(latencies, 95))
            p99 = float(np.percentile(latencies, 99))
            print(f"  [50x Iterations] P50: {p50:.2f} ms | P95: {p95:.2f} ms | P99: {p99:.2f} ms")
        else:
            p50, p95, p99 = 0.0, 0.0, 0.0

        con.close()

        results["tests"].append({
            "max_memory_setting": mem_limit,
            "full_query": {
                "status": status_full,
                "latency_ms": round(t_full_ms, 2),
                "peak_ram_delta_mb": round(mem_used_delta, 2),
                "rows_retrieved": rows_retrieved
            },
            "point_query": {
                "status": status_point,
                "latency_ms": round(t_point_ms, 2),
                "peak_ram_delta_mb": round(mem_used_point_delta, 2)
            },
            "benchmark_50_iters": {
                "p50_ms": round(p50, 2),
                "p95_ms": round(p95, 2),
                "p99_ms": round(p99, 2)
            }
        })

    # 3. Concurrent Multi-Threaded Stress Test under 20MB Memory Constraint
    print("\n--- Testing Concurrent Workload: 10 Threads x 20 Queries (max_memory = '20MB') ---")
    con_shared = duckdb.connect(database=':memory:')
    con_shared.execute("SET max_memory='20MB'")
    
    def worker_task(thread_id):
        sample_cell = 620000000000000000 + (thread_id % 25)
        task_latencies = []
        for _ in range(20):
            t0 = time.perf_counter()
            con_shared.execute(f"SELECT COUNT(*), AVG(revenue) FROM read_parquet('{PARQUET_PATH}') WHERE h3_cell = {sample_cell}").fetchall()
            task_latencies.append((time.perf_counter() - t0) * 1000.0)
        return task_latencies

    t_start_concurrent = time.perf_counter()
    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = [executor.submit(worker_task, i) for i in range(10)]
        all_concurrent_latencies = []
        for f in futures:
            all_concurrent_latencies.extend(f.result())

    t_total_concurrent_ms = (time.perf_counter() - t_start_concurrent) * 1000.0
    conc_p50 = float(np.median(all_concurrent_latencies))
    conc_p95 = float(np.percentile(all_concurrent_latencies, 95))
    conc_p99 = float(np.percentile(all_concurrent_latencies, 99))

    print(f"  [Concurrent 200 Queries] Total Time: {t_total_concurrent_ms:.2f} ms | P50: {conc_p50:.2f} ms | P95: {conc_p95:.2f} ms | P99: {conc_p99:.2f} ms")
    con_shared.close()

    results["concurrent_stress_test"] = {
        "total_queries": 200,
        "threads": 10,
        "total_duration_ms": round(t_total_concurrent_ms, 2),
        "p50_ms": round(conc_p50, 2),
        "p95_ms": round(conc_p95, 2),
        "p99_ms": round(conc_p99, 2)
    }

    with open(OUTPUT_REPORT, "w") as f:
        json.dump(results, f, indent=4)

    print(f"\nEmpirical report saved to: {OUTPUT_REPORT}")
    return results

if __name__ == "__main__":
    run_empirical_duckdb_tests()
