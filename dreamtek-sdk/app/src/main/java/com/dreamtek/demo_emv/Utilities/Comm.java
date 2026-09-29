package com.dreamtek.demo_emv.Utilities;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Simon on 2018/8/23.
 */

public class Comm {

    private static final String TAG = "EMVDemo-Comm";

    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private int status;
    private String ip;
    private int port;

    public Comm(){
        status = 0;
        ip = "";
        port = 0;
        outputStream = null;
        inputStream = null;
    }

    public Comm(String ip, int port ){
        status = 0;
        this.ip = ip;
        this.port = port;
        outputStream = null;
        inputStream = null;
    }


    public boolean connect(String ip, int port) {
        if (status > 0 && ip != null && ip.equals(this.ip) && port == this.port) {
            return true;
        }
        disconnect();
        this.ip = ip;
        this.port = port;
        return connect();
    }

    public boolean connect() {
        if (status > 0) {
            return true;
        }
        if (ip == null || ip.length() == 0 || port < 1 || port > 65535) {
            Log.e(TAG, "Invalid host configuration");
            return false;
        }
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 10000);
            this.ip = ip;
            this.port = port;
            status = 1;
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Connect failed", e);
            disconnect();
        }
        return false;
    }


    public int send(byte[] data) {
        if (status <= 0 || data == null || data.length == 0) {
            return 0;
        }

        Log.d(TAG, "Sending " + data.length + " bytes");

        try {
            outputStream = socket.getOutputStream();
            if( null == outputStream ){
                return 0;
            }

            outputStream.write( data );
            outputStream.flush();
            return data.length;
        } catch (IOException e) {
            Log.e(TAG, "Send failed", e);
        }
        return 0;
    }


    public byte[] receive(int wantLength, int timeoutSecond) {
        if (status <= 0 || wantLength < 3 || timeoutSecond <= 0) {
            return null;
        }
        try {
            socket.setSoTimeout(timeoutSecond * 1000);
            inputStream = socket.getInputStream();
            byte[] header = new byte[2];
            if (!readFully(header, 0, header.length)) {
                return null;
            }
            int bodyLength = ((header[0] & 0xFF) << 8) | (header[1] & 0xFF);
            if (bodyLength <= 0 || bodyLength + header.length > wantLength) {
                Log.e(TAG, "Invalid response length: " + bodyLength);
                return null;
            }

            byte[] response = new byte[bodyLength + header.length];
            System.arraycopy(header, 0, response, 0, header.length);
            if (!readFully(response, header.length, bodyLength)) {
                return null;
            }
            return response;
        } catch (SocketException e) {
            Log.e(TAG, "Receive socket error", e);
        } catch (IOException e) {
            Log.e(TAG, "Receive failed", e);
        }
        return null;
    }

    private boolean readFully(byte[] buffer, int offset, int length) throws IOException {
        int total = 0;
        while (total < length) {
            int count = inputStream.read(buffer, offset + total, length - total);
            if (count < 0) {
                return false;
            }
            total += count;
        }
        return true;
    }

    public void disconnect() {
        status = 0;
        try {
            if( null != inputStream ) {
                inputStream.close();
                inputStream = null;
            }
        } catch (IOException e) {
            Log.w(TAG, "Unable to close input stream", e);
        }

        try {
            if( null != outputStream ){
                outputStream.close();
                outputStream = null;
            }
        } catch (IOException e) {
            Log.w(TAG, "Unable to close output stream", e);
        }

        try {
            if( null != socket ){
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            Log.w(TAG, "Unable to close socket", e);
        }
        status = 0;
    }
}
