package main

// 03_lab_false_sharing_cache_line_padding.go
// -------------------------------------------------------------------------
// Laboratorio Práctico Feynman: Microarquitectura de Memoria & False Sharing
// Basado en el paper fundamental de Ulrich Drepper (2007):
// "What Every Programmer Should Know About Memory"
// -------------------------------------------------------------------------
// Demostración empírica de cómo dos hilos escribiendo en variables adyacentes
// en la misma línea de caché L1 (64 bytes) destruyen el rendimiento de la CPU.
// -------------------------------------------------------------------------

import (
	"fmt"
	"sync"
	"time"
)

const Iterations = 50_000_000

// Estructura SIN padding: a y b comparten la misma línea de caché de 64 bytes
type UnpaddedCounter struct {
	a uint64 // 8 bytes
	b uint64 // 8 bytes (mismo bloque L1)
}

// Estructura CON padding: 56 bytes de relleno para aislar a y b en líneas L1 separadas
type PaddedCounter struct {
	a   uint64   // 8 bytes
	_   [7]uint64 // 56 bytes de relleno (padding) -> Total 64 bytes
	b   uint64   // 8 bytes (nueva línea de caché L1 aislada)
	_   [7]uint64 // 56 bytes de relleno
}

func benchmarkUnpadded() time.Duration {
	var counter UnpaddedCounter
	var wg sync.WaitGroup
	wg.Add(2)

	start := time.Now()

	go func() {
		defer wg.Done()
		for i := 0; i < Iterations; i++ {
			counter.a++
		}
	}()

	go func() {
		defer wg.Done()
		for i := 0; i < Iterations; i++ {
			counter.b++
		}
	}()

	wg.Wait()
	return time.Since(start)
}

func benchmarkPadded() time.Duration {
	var counter PaddedCounter
	var wg sync.WaitGroup
	wg.Add(2)

	start := time.Now()

	go func() {
		defer wg.Done()
		for i := 0; i < Iterations; i++ {
			counter.a++
		}
	}()

	go func() {
		defer wg.Done()
		for i := 0; i < Iterations; i++ {
			counter.b++
		}
	}()

	wg.Wait()
	return time.Since(start)
}

func main() {
	fmt.Println("====================================================================")
	fmt.Println("  🧪 LAB FEYNMAN 03: MICROARQUITECTURA CPU & FALSE SHARING (DREPPER 2007)")
	fmt.Println("====================================================================")
	fmt.Printf("Iteraciones por hilo: %d\n\n", Iterations)

	fmt.Println("⏳ Ejecutando prueba SIN Padding (False Sharing activo en línea L1)...")
	durUnpadded := benchmarkUnpadded()
	fmt.Printf("❌ Tiempo SIN Padding: %v\n\n", durUnpadded)

	fmt.Println("⏳ Ejecutando prueba CON Padding (Líneas de caché L1 aisladas de 64B)...")
	durPadded := benchmarkPadded()
	fmt.Printf("✅ Tiempo CON Padding: %v\n\n", durPadded)

	speedup := float64(durUnpadded) / float64(durPadded)
	fmt.Println("--------------------------------------------------------------------")
	fmt.Printf("🚀 Aceleración por Aislamiento de Memoria L1: %.2fx más rápido\n", speedup)
	fmt.Println("🧠 Explicación Feynman: Dos personas intentando escribir a la vez en la")
	fmt.Println("   misma hoja de papel (línea de caché) se chocan los codos. Con dos hojas")
	fmt.Println("   separadas (padding), cada persona escribe a máxima velocidad.")
	fmt.Println("====================================================================")
}
