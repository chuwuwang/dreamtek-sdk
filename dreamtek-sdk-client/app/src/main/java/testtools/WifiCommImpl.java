package testtools;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import Utils.LogUtil;

public class WifiCommImpl {
    private final String TAG = "COMM";

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private SocketChannel socketChannel;
    private int listenPort = 5556; // default
    private MsgCallback msgCallback;
    private int maxSingleRecvLen = 1024; // default
    private boolean isRunning;

    public WifiCommImpl(int listenPort, int maxSingleRecvLen) {
        LogUtil.d(TAG, "Wifi listenPort=" + listenPort + " maxSingleRecvLen=" + maxSingleRecvLen);
        this.maxSingleRecvLen = maxSingleRecvLen;
        this.listenPort = listenPort;
    }

    public synchronized void init() {
        try {
            LogUtil.d(TAG, "Wifi init listenPort=" + this.listenPort + " maxSingleRecvLen=" + this.maxSingleRecvLen);
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(listenPort));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            socketChannel = null;
            isRunning = true;
            LogUtil.d(TAG, "Wifi init success.");
        } catch (IOException e) {
            LogUtil.d(TAG, "Wifi init failed.");
            e.printStackTrace();
        }
    }

    public void listenAndRead(MsgCallback callback) {
        this.msgCallback = callback;

        LogUtil.d(TAG, "listenAndRead start.");
        while (isRunning && selector.isOpen()) {
            try {
                int n = selector.select();
                if (n == 0) {
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isAcceptable()) {
                        LogUtil.d(TAG, "Client connected.");
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                        socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }

                    if (selectionKey.isReadable()) {
                        if (!doReadProcessing(selectionKey)) {
                            continue;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LogUtil.d(TAG, "listenAndRead end");
    }

    public void send(byte[] msg) {
        if (msg == null) {
            LogUtil.d(TAG, "No valid message found.");
            return;
        }

        try {
            int sendCount = 0;
            while (sendCount < msg.length) {
                int ret = -1;
                ret = socketChannel.write(ByteBuffer.wrap(msg));
                LogUtil.d(TAG, "send ret=" + ret + " sendCount=" + sendCount);
                if (ret < 0) {
                    doClentCloseProcess(socketChannel);
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendCount += ret;
                }
            }
        } catch (IOException e) {
            doClentCloseProcess(socketChannel);
            e.printStackTrace();
        }
    }

    /**
     * Read client data
     * @param selectionKey
     * @return if client disconnected return false, else return true.
     */
    private boolean doReadProcessing(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        if (socketChannel == null) {
            return true;
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(maxSingleRecvLen);
        int ret = -1;
        try {
            ret = socketChannel.read(byteBuffer);
        } catch (IOException e) {
            LogUtil.d(TAG, "read exception happened.");
            e.printStackTrace();
        }

        LogUtil.d(TAG, "read data len=" + ret);
        if (ret <= 0) {
            selectionKey.cancel();
            doClentCloseProcess(socketChannel);
            msgCallback.onReceive(null);
            return false;
        } else {
            byte[] data = new byte[byteBuffer.position()];
            LogUtil.d(TAG, "byteBuffer data len=" + data.length);
            byteBuffer.flip();
            byteBuffer.get(data, 0, data.length);
            msgCallback.onReceive(data);
        }

        return true;
    }

    private void doClentCloseProcess(SocketChannel socketChannel) {
        LogUtil.d(TAG, "Client socket closed.");

        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopListen() {
        try {
            isRunning = false;
            if (socketChannel != null && socketChannel.isConnected()) {
                socketChannel.close();
            }
            if (serverSocketChannel != null) {
                ServerSocket serverSocket = serverSocketChannel.socket();
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
                serverSocketChannel.close();
            }
            if (selector != null && selector.isOpen()) {
                selector.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.d(TAG, "stopListen finished==========================================");
    }

    public interface MsgCallback {
        void onReceive(byte[] msg);
    }
}
