import unittest
from src import __init__

class Testcore_kalman_twin(unittest.TestCase):
    def test_version(self):
        self.assertEqual(__init__.get_version(), "1.0.0")

if __name__ == '__main__':
    unittest.main()
