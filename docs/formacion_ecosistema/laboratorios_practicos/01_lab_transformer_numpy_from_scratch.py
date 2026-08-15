#!/usr/bin/env python3
"""
01_lab_transformer_numpy_from_scratch.py
-------------------------------------------------------------------------
Laboratorio Práctico Feynman: Deconstrucción Matemática de un Transformer
Implementación completa del mecanismo Multi-Head Scaled Dot-Product Attention
utilizando ÚNICAMENTE Python 3 y NumPy puro (sin PyTorch ni librerías ocultas).
-------------------------------------------------------------------------
Fórmula de Vaswani et al. (2017):
  Attention(Q, K, V) = softmax( (Q * K^T) / sqrt(d_k) ) * V
-------------------------------------------------------------------------
"""
import numpy as np

def softmax(x: np.ndarray, axis: int = -1) -> np.ndarray:
    """Softmax numéricamente estable restando el valor máximo para evitar desbordamiento."""
    e_x = np.exp(x - np.max(x, axis=axis, keepdims=True))
    return e_x / np.sum(e_x, axis=axis, keepdims=True)

def scaled_dot_product_attention(
    q: np.ndarray, 
    k: np.ndarray, 
    v: np.ndarray, 
    mask: np.ndarray = None
) -> tuple[np.ndarray, np.ndarray]:
    """
    Calcula la atención escalada paso a paso.
    Dimensiones:
      Q: (batch_size, num_heads, seq_len, d_k)
      K: (batch_size, num_heads, seq_len, d_k)
      V: (batch_size, num_heads, seq_len, d_v)
    """
    d_k = q.shape[-1]
    
    # 1. Producto escalar de consultas (Q) y claves (K): Similitud de conceptos
    # Q * K^T -> (..., seq_len, seq_len)
    scores = np.matmul(q, k.swapaxes(-2, -1)) / np.sqrt(d_k)
    
    # 2. Aplicación de máscara causal (opcional para decodificadores autorregresivos)
    if mask is not None:
        scores = np.where(mask == 0, -1e9, scores)
        
    # 3. Distribución de probabilidad de atención con Softmax
    attention_weights = softmax(scores, axis=-1)
    
    # 4. Agregación ponderada de los valores (V)
    output = np.matmul(attention_weights, v)
    
    return output, attention_weights

class MultiHeadAttentionNumpy:
    """Implementación limpia de Multi-Head Attention con NumPy."""
    def __init__(self, d_model: int = 64, num_heads: int = 4):
        assert d_model % num_heads == 0, "d_model debe ser divisible por num_heads"
        self.d_model = d_model
        self.num_heads = num_heads
        self.d_k = d_model // num_heads
        
        # Inicialización de matrices de pesos (Glorot/Xavier)
        np.random.seed(42)
        self.w_q = np.random.randn(d_model, d_model) * np.sqrt(2.0 / d_model)
        self.w_k = np.random.randn(d_model, d_model) * np.sqrt(2.0 / d_model)
        self.w_v = np.random.randn(d_model, d_model) * np.sqrt(2.0 / d_model)
        self.w_o = np.random.randn(d_model, d_model) * np.sqrt(2.0 / d_model)

    def forward(self, x: np.ndarray) -> tuple[np.ndarray, np.ndarray]:
        batch_size, seq_len, _ = x.shape
        
        # Proyecciones lineales iniciales
        q = np.matmul(x, self.w_q)
        k = np.matmul(x, self.w_k)
        v = np.matmul(x, self.w_v)
        
        # Dividir en múltiples cabezas (Multi-Head Split)
        q = q.reshape(batch_size, seq_len, self.num_heads, self.d_k).swapaxes(1, 2)
        k = k.reshape(batch_size, seq_len, self.num_heads, self.d_k).swapaxes(1, 2)
        v = v.reshape(batch_size, seq_len, self.num_heads, self.d_k).swapaxes(1, 2)
        
        # Calcular atención para todas las cabezas en paralelo
        out_heads, attn_weights = scaled_dot_product_attention(q, k, v)
        
        # Concatenar cabezas y aplicar proyección final W_o
        concat_out = out_heads.swapaxes(1, 2).reshape(batch_size, seq_len, self.d_model)
        output = np.matmul(concat_out, self.w_o)
        
        return output, attn_weights

def main():
    print("====================================================================")
    print("  🧪 LAB FEYNMAN 01: TRANSFORMER MULTI-HEAD ATTENTION EN NUMPY PURO")
    print("====================================================================")
    
    batch_size = 2
    seq_len = 5 # 5 palabras / tokens
    d_model = 16 # Dimensión del embedding
    num_heads = 4
    
    # Secuencia simulada de embeddings
    embeddings = np.random.randn(batch_size, seq_len, d_model)
    print(f"📊 Tensor de Entrada: Batch={batch_size}, SeqLen={seq_len}, Dim={d_model}")
    
    mha = MultiHeadAttentionNumpy(d_model=d_model, num_heads=num_heads)
    output, weights = mha.forward(embeddings)
    
    print(f"✅ Tensor de Salida Transformado: {output.shape}")
    print(f"✅ Matriz de Pesos de Atención : {weights.shape} (Cabezas={num_heads})")
    print("\n🔬 Matriz de Atención de la Cabeza #1 (Token-to-Token Relevance):")
    print(np.round(weights[0, 0], 3))
    print("--------------------------------------------------------------------")
    print("✓ Laboratorio ejecutado con éxito: 100% NumPy puro sin dependencias.")
    print("====================================================================")

if __name__ == "__main__":
    main()
