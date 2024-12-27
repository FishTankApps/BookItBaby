package com.fishtankapps.bookitbaby.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

public class RawAudioFile {

	public final static int MONO_CHANNEL = 1;
	public final static int LEFT_CHANNEL = 1;
	public final static int RIGHT_CHANNEL = 2;

	private byte[] rawAudioData;
	private short[] shortChannel1, shortChannel2;
	private int numOfChannels;
	private AudioFormat format;

	public RawAudioFile(File file) throws IOException, UnsupportedAudioFileException {

		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
		DataInputStream dis = new DataInputStream(audioInputStream);

		format = audioInputStream.getFormat();

		numOfChannels = format.getChannels();

		rawAudioData = new byte[(int) (audioInputStream.getFrameLength() * format.getFrameSize())];

		dis.readFully(rawAudioData);
		dis.close();

		if (numOfChannels == 1) {

			shortChannel1 = new short[rawAudioData.length / 2];
			shortChannel2 = null;

			for (int index = 0; index < rawAudioData.length; index += 2) {
				shortChannel1[index / 2] = (short) ((rawAudioData[index] & 0xff) | (rawAudioData[index + 1] << 8));
			}

		} else if (numOfChannels == 2) {

			shortChannel1 = new short[rawAudioData.length / 4];
			shortChannel2 = new short[rawAudioData.length / 4];

			for (int index = 0; index < rawAudioData.length; index += 4) {
				shortChannel1[index / 4] = (short) ((rawAudioData[index] & 0xff) | (rawAudioData[index + 1] << 8));
				shortChannel2[index / 4] = (short) ((rawAudioData[index + 2] & 0xff) | (rawAudioData[index + 3] << 8));
			}
		} else {
			throw new RuntimeException("Unable to work with more than two channels!");
		}
	}

	public int getNumberOfChannels() {
		return numOfChannels;
	}

	public int getNumberOfSamples() {
		if(shortChannel1 != null)
			return shortChannel1.length;
		
		return -1;
	}
	
	
	public AudioFormat getAudioFormat() {
		return format;
	}

	public short[] getChannel(int index) {
		if (index > numOfChannels)
			throw new RuntimeException("Their are only " + numOfChannels + " channel(s).");

		if (index == MONO_CHANNEL)
			return shortChannel1;
		else if (index == RIGHT_CHANNEL)
			return shortChannel2;

		return null;
	}

	public void dispose() {
		rawAudioData = null;
		shortChannel1 = null;
		shortChannel2 = null;
		numOfChannels = -1;

		System.gc();
	}

	public Clip getPlayableClip() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(format, rawAudioData, 0, rawAudioData.length);

			return clip;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}