from hypothesis import given, strategies as st
from nash_equilibrium_solver import NashEquilibriumSolver

@given(
    a_honesto_b_honesto_a=st.floats(min_value=-100, max_value=100),
    a_honesto_b_honesto_b=st.floats(min_value=-100, max_value=100),
    a_honesto_b_malicioso_a=st.floats(min_value=-100, max_value=100),
    a_honesto_b_malicioso_b=st.floats(min_value=-100, max_value=100),
    a_malicioso_b_honesto_a=st.floats(min_value=-100, max_value=100),
    a_malicioso_b_honesto_b=st.floats(min_value=-100, max_value=100),
    a_malicioso_b_malicioso_a=st.floats(min_value=-100, max_value=100),
    a_malicioso_b_malicioso_b=st.floats(min_value=-100, max_value=100)
)
def test_nash_solver_no_exceptions(
    a_honesto_b_honesto_a, a_honesto_b_honesto_b,
    a_honesto_b_malicioso_a, a_honesto_b_malicioso_b,
    a_malicioso_b_honesto_a, a_malicioso_b_honesto_b,
    a_malicioso_b_malicioso_a, a_malicioso_b_malicioso_b
):
    solver = NashEquilibriumSolver()
    payoff = [
        [(a_honesto_b_honesto_a, a_honesto_b_honesto_b), (a_honesto_b_malicioso_a, a_honesto_b_malicioso_b)],
        [(a_malicioso_b_honesto_a, a_malicioso_b_honesto_b), (a_malicioso_b_malicioso_a, a_malicioso_b_malicioso_b)]
    ]
    
    # La función no debe lanzar excepciones
    result = solver.check_for_death_spiral(payoff)
    assert isinstance(result, bool)

def test_nash_death_spiral_detection():
    solver = NashEquilibriumSolver()
    payoff = [
        [(5, 5), (-10, 10)],
        [(10, -10), (-5, -5)],
    ]
    is_valid = solver.check_for_death_spiral(payoff)
    assert is_valid is False, "Debe detectar la espiral de la muerte"

def test_nash_healthy_system():
    solver = NashEquilibriumSolver()
    payoff = [
        [(10, 10), (0, 0)],
        [(0, 0), (2, 2)],
    ]
    is_valid = solver.check_for_death_spiral(payoff)
    assert is_valid is True, "El sistema saludable debe ser válido"
