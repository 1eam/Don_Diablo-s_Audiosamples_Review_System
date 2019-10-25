package com.diabolo.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ws.schild.jave.*;

import java.io.File;

public class Jave2Util {
    private static final Logger LOGGER = LogManager.getLogger(Jave2Util.class.getName());
    private MultimediaObject inputObject;
    private MultimediaInfo inputInfo;
    private File inputFile;
    private long inputDuration;
    private int inputChannels;
    private String inputFormat;
    private int inputBitRate;
    private int inputSamplingRate;

    Jave2Util(String filePath) {
        inputFile = new File(filePath);
        inputObject = new MultimediaObject(inputFile);
        try {
            inputInfo = inputObject.getInfo();
        } catch (Exception ex) {
            LOGGER.error(Jave2Util.class.getName() + " MultimediaInfo Error Message Logged !!!",
                    ex.getMessage(), ex);
        }
        assert inputInfo != null;
        inputDuration = inputInfo.getDuration();
        AudioInfo inputAudioInfo = inputInfo.getAudio();
        inputChannels = inputAudioInfo.getChannels();
        inputFormat = inputInfo.getFormat();
        inputBitRate = inputAudioInfo.getBitRate();
        inputSamplingRate = inputAudioInfo.getSamplingRate();
    }

    protected String getInputFormat() {
        return inputFormat;
    }

    protected long getInputDuration() {
        return inputDuration;
    }

    protected int getInputBitRate() {
        return inputBitRate;
    }

    protected AudioAttributes createAudioAttributes(String codec) {
        AudioAttributes audioAttributes = new AudioAttributes();
        audioAttributes.setCodec(codec);
        //audioAttributes.setBitRate(inputBitRate);
        audioAttributes.setBitRate(320000);
        audioAttributes.setChannels(inputChannels);
        audioAttributes.setSamplingRate(inputSamplingRate);
        return audioAttributes;
    }

    protected EncodingAttributes createEncodingAttributes(AudioAttributes audioAttributes, String format) {
        EncodingAttributes encodingAttributes = new EncodingAttributes();
        encodingAttributes.setFormat(format);
        encodingAttributes.setAudioAttributes(audioAttributes);
        return encodingAttributes;
    }

    protected boolean encodeAudio(EncodingAttributes encodingAttributes, String outputFilePath) {
        File outputFile = new File(outputFilePath);
        try {
            Encoder encoder = new Encoder();
            encoder.encode(inputObject, outputFile, encodingAttributes);
            return true;
        } catch (Exception ex) {
            LOGGER.error(Jave2Util.class.getName() + " Encoding Error Message Logged !!!",
                    ex.getMessage(), ex);
        }
        return false;
    }
}
