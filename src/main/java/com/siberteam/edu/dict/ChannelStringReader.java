package com.siberteam.edu.dict;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

//channel to string reader

public class ChannelStringReader implements Runnable {
    private final FileChannel channel;
    private final long startLocation;
    private final long size;
    private String string_chunk;

    public ChannelStringReader(FileChannel channel,
                               long startLocation, long size) {
        this.channel = channel;
        this.startLocation = startLocation;
        this.size = size;
    }

    @Override
    public void run() {
        try {
            System.out.println("Reading the channel: " + startLocation + ":" + size);
            ByteBuffer buffer = ByteBuffer.allocate(Math.toIntExact(size));

            channel.read(buffer, startLocation);

            string_chunk = new String(buffer.array(), StandardCharsets.UTF_8);
            System.out.println("Done Reading the channel: " + startLocation + ":" + size);

//            System.out.println(string_chunk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString_chunk() {
        return string_chunk;
    }
}
