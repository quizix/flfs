/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.communication.base;

/**
 *
 * @author Administrator
 */
public class Converter {

    public static float shortsToFloat(short[] data) {
        return shortsToFloat(data, 0);
    }

    public static float shortsToFloat(short[] data, int offset) {
        if (data == null || data.length < offset + 2) {
            throw new IllegalArgumentException("invalid argument");
        }
        float f = Float.intBitsToFloat((((data[offset] >> 8) & 0xff) << 24) | ((data[offset] & 0xff) << 16)
                | (((data[offset + 1] >> 8) & 0xff) << 8) | (data[offset + 1] & 0xff));

        return f;
    }

    public static int shortsToInt(short[] data) {
        return shortsToInt(data, 0);

    }

    public static int shortsToInt(short[] data, int offset) {
        if (data == null || data.length < offset + 2) {
            throw new IllegalArgumentException("invalid argument");
        }
        int x = (((data[offset] >> 8) & 0xff) << 24) | ((data[offset] & 0xff) << 16)
                | (((data[offset + 1] >> 8) & 0xff) << 8) | (data[offset + 1] & 0xff);

        return x;

    }

    public static int[] shortsToInts(short[] data) {
        if (data == null || data.length < 2) {
            throw new IllegalArgumentException("from must not be null or from.length must greater than 2");
        }
        int count = data.length / 2;
        int[] value = new int[count];
        for (int i = 0; i < count; i++) {
            value[i] = shortsToInt(data, i * 2);
        }
        return value;
    }

    public static float[] shortsToFloats(short[] data) {
        if (data == null || data.length < 2) {
            throw new IllegalArgumentException("from must not be null or from.length must greater than 2");
        }
        int count = data.length / 2;
        float[] value = new float[count];
        for (int i = 0; i < count; i++) {
            value[i] = shortsToFloat(data, i * 2);
        }
        return value;
    }

    public static short[] floatToShorts(float data) {
        int x = Float.floatToIntBits(data);
        short[] result = new short[2];
        result[0] = (short) ((x >> 16) & 0xffff);
        result[1] = (short) (x & 0xffff);
        return result;
    }
    
    public static short[] intToShorts(int data) {
        int x = data;
        short[] result = new short[2];
        result[0] = (short) ((x >> 16) & 0xffff);
        result[1] = (short) (x & 0xffff);
        return result;
    }

    public static short[] floatsToShorts(float[] data) {
        int length = data.length;

        short[] result = new short[length * 2];
        for (int i = 0; i < length; i++) {
            int x = Float.floatToIntBits(data[i]);

            result[i * 2] = (short) ((x >> 16) & 0xffff);
            result[i * 2 + 1] = (short) (x & 0xffff);
        }
        return result;
    }
    
    public static short[] intsToShorts(int[] data) {
        int length = data.length;

        short[] result = new short[length * 2];
        for (int i = 0; i < length; i++) {
            int x = data[i];

            result[i * 2] = (short) ((x >> 16) & 0xffff);
            result[i * 2 + 1] = (short) (x & 0xffff);
        }
        return result;
    }
    
    /*public static short[] floatsAndShortsToShorts(float[] floats, short[] shorts) {

        short[] result = new short[floats.length * 2 + shorts.length];
        int offset = 0;
        for (int i = 0; i < floats.length; i++) {
            int x = Float.floatToIntBits(floats[i]);
            result[offset] = (short) ((x >> 16) & 0xffff);
            result[offset + 1] = (short) (x & 0xffff);
            offset += 2;
        }

        for (int i = 0; i < shorts.length; i++) {
            result[offset++] = shorts[i];
        }
        return result;
    }*/
}
