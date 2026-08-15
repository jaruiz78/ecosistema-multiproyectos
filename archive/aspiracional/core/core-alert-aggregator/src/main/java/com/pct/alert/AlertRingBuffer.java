package com.pct.alert;

import com.corp.contracts.SystemAlertRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AlertRingBuffer
 * Búfer circular acotado de tamaño fijo O(1) en memoria para almacenamiento de telemetría de alertas.
 * Evita cualquier Carrier Thread Pinning mediante el uso de ReentrantLock en Java 25.
 */
public final class AlertRingBuffer {
    private final SystemAlertRecord[] buffer;
    private final int capacity;
    private int head = 0;
    private int size = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public AlertRingBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacidad debe ser mayor que 0");
        }
        this.capacity = capacity;
        this.buffer = new SystemAlertRecord[capacity];
    }

    public void push(SystemAlertRecord alert) {
        lock.lock();
        try {
            buffer[head] = alert;
            head = (head + 1) % capacity;
            if (size < capacity) {
                size++;
            }
        } finally {
            lock.unlock();
        }
    }

    public List<SystemAlertRecord> snapshot() {
        lock.lock();
        try {
            List<SystemAlertRecord> list = new ArrayList<>(size);
            int start = (size < capacity) ? 0 : head;
            for (int i = 0; i < size; i++) {
                int index = (start + i) % capacity;
                if (buffer[index] != null) {
                    list.add(buffer[index]);
                }
            }
            return list;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return size;
        } finally {
            lock.unlock();
        }
    }

    public int capacity() {
        return capacity;
    }
}
