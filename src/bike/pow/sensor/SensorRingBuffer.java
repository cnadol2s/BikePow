package bike.pow.sensor;

import java.util.Arrays;

public class SensorRingBuffer {

	private double[] buffer;
	private int next;		// next free slot
	private int tail;		// oldest filled slot (oldest entry)
	private int count;
	private double lastValue;
	
	public SensorRingBuffer(int capacity) {
		this.next = 0;
		this.tail = 0;
		this.lastValue = 0.0;
		this.buffer = new double[capacity];
		for (int i=0; i < this.buffer.length; i++) {
			this.buffer[i] = 0;
		}
	}
	
	public void insert(double value) {
		if (this.buffer != null) {
			int n = this.buffer.length;
			this.lastValue = value;
			this.buffer[next] = value;
			this.count++;

			// if next bites tail
			if (this.tail == ((this.next + 1) % n)) {
				this.tail = (this.tail + 1) % n;
				this.count--;
			}
			this.next = (this.next + 1) % n;
		}
	}
	
	public double getRawValue() {
		return this.lastValue;
	}
	
	public double computeAverageValue() {
		if (this.buffer != null) {
			double sum = 0;
			int n = this.buffer.length;
			int i = this.tail;				// oldest entry first
			while (i != this.next) {
				sum = sum + this.buffer[i];
				i = (i + 1) % n;
			}
			return (sum / this.count);
		} else {
			return -1;
		}
	}
	
	public double computeMedianValue() {
		double[] medianArray = new double[this.count];
		if (this.buffer != null) {
			if (this.count > 1) {
				int n = medianArray.length - 1;
				int j = 0;
				int i = this.tail;				// oldest entry first
				while (i != this.next) {
					medianArray[j] = this.buffer[i];
					i = (i + 1) % this.buffer.length;
					j++;
				}
				Arrays.sort(medianArray);
				
				// Even
				if ((n % 2) == 0) {
					return ((1/2) * (medianArray[(n / 2) - 1])
							     + medianArray[(n / 2 + 1) - 1]);
				// Odd
				} else {
					return medianArray[(n + 1) / 2];
				}
			}
		}
		return -1;
	}
	
}
