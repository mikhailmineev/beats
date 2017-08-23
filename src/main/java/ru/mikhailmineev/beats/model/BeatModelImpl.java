package ru.mikhailmineev.beats.model;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class BeatModelImpl implements BeatModel, MetaEventListener {

	private static final int DEFAULT_BPM = 90;

	Sequencer sequencer;

	Sequence sequence;

	Track track;

	List<BeatObserver> beatObersvers = new ArrayList<>();

	List<BpmObserver> bpmObersvers = new ArrayList<>();

	int bpm = DEFAULT_BPM;

	public void initialize() {
		setUpMidi();
		buildTrackAndStart();
	}

	public void on() {
		sequencer.start();
		setBpm(DEFAULT_BPM);
	}

	public void off() {
		setBpm(0);
		sequencer.stop();
	}

	public void setBpm(int bpm) {
		this.bpm = bpm;
		sequencer.setTempoInBPM(getBpm());
		notifyBpmObservers();
	}

	public int getBpm() {
		return bpm;
	}

	private void beatEvent() {
		notifyBeatObservers();
	}

	public void registerObserver(BeatObserver o) {
		beatObersvers.add(o);

	}

	public void deregisterObserver(BeatObserver o) {
		beatObersvers.remove(o);

	}

	public void notifyBeatObservers() {
		for (BeatObserver o : beatObersvers) {
			o.updateBeat();
		}
	}

	public void registerObserver(BpmObserver o) {
		bpmObersvers.add(o);

	}

	public void deregisterObserver(BpmObserver o) {
		bpmObersvers.remove(o);

	}

	public void notifyBpmObservers() {
		for (BpmObserver o : bpmObersvers) {
			o.updateBpm();
		}
	}

	@Override
	public void meta(MetaMessage meta) {
		if (meta.getType() == 47) {
			beatEvent();
			sequencer.start();
			setBpm(getBpm());
		}
	}

	public void setUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.addMetaEventListener(this);
			sequence = new Sequence(Sequence.PPQ, 4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(getBpm());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buildTrackAndStart() {
		int[] trackList = { 35, 0, 46, 0 };

		sequence.deleteTrack(null);
		track = sequence.createTrack();

		makeTracks(trackList);
		track.add(makeEvent(192, 9, 1, 0, 4));

		try {
			sequencer.setSequence(sequence);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void makeTracks(int[] list) {
		for (int i = 0; i < list.length; i++) {
			int key = list[i];
			if (key != 0) {
				track.add(makeEvent(144, 9, key, 100, i));
				track.add(makeEvent(128, 9, key, 100, i + 1));
			}
		}
	}

	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one, two);
			event = new MidiEvent(a, tick);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return event;
	}
}
