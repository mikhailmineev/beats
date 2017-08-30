package ru.mikhailmineev.beats.model;

public interface BeatModel {
	
	void initialize();

	void on();

	void off();

	void setBPM(int bpm);

	int getBPM();

	void registerObserver(BeatObserver o);

	void deregisterObserver(BeatObserver o);

	void registerObserver(BPMObserver o);

	void deregisterObserver(BPMObserver o);

}
