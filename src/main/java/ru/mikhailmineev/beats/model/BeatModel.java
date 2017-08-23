package ru.mikhailmineev.beats.model;

public interface BeatModel {
	
	void initialize();

	void on();

	void off();

	void setBpm(int bpm);

	int getBpm();

	void registerObserver(BeatObserver o);

	void deregisterObserver(BeatObserver o);

	void registerObserver(BpmObserver o);

	void deregisterObserver(BpmObserver o);

}
