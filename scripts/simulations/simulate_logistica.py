#!/usr/bin/env python3
import os
import pickle
import json


def simulate():
    try:
        with open("current_state.json", "r") as f:
            state = json.load(f)
            month = state["month"]
            health = state["ecosystem_health"]
    except:
        month = "N/A"
        health = "N/A"

    model_path = os.path.join(os.path.dirname(__file__), "models/dqn_mobility.pkl")
    try:
        with open(model_path, "rb") as f:
            model = pickle.load(f)
            print(
                f"Logistica [Mes {month}] - Salud {health} - Precisión IA: {model['accuracy']} - Meta: {model.get('metadata','')}"
            )
    except FileNotFoundError:
        pass


if __name__ == "__main__":
    simulate()
