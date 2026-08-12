import pytest
from simulate_5_years_pro import FiveYearProSimulator

def test_five_year_pro_simulation_metrics():
    simulator = FiveYearProSimulator()
    report = simulator.run_simulation()
    
    assert report["duration_months"] == 60
    assert report["final_maus"] > 4_000_000
    assert report["gross_margin_pct"] > 95.0
    assert report["avg_cost_per_mau_usd"] < 0.015
    assert report["finops_compliant"] is True
    assert report["final_enkf_covariance"] < 0.1
