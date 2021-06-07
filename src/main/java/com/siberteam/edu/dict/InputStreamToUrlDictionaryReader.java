package com.siberteam.edu.dict;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class InputStreamToUrlDictionaryReader implements Runnable {
    private final FileChannel channel;
    private final int startLocation;
    private final int size;
    private int sequence;
    private String string_chunk;

    public InputStreamToUrlDictionaryReader(FileChannel channel,
                                            int startLocation, int size,
                                            int sequence) {
        this.channel = channel;
        this.startLocation = startLocation;
        this.size = size;
        this.sequence = sequence;
    }

    @Override
    public void run() {
        try {
            System.out.println("Reading the channel: " + startLocation + ":" + size);
            ByteBuffer buffer = ByteBuffer.allocate(size);

            channel.read(buffer, startLocation);

            string_chunk = new String(buffer.array(), StandardCharsets.UTF_8);
            System.out.println("Done Reading the channel: " + startLocation + ":" + size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
