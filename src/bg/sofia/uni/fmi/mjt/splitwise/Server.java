package bg.sofia.uni.fmi.mjt.splitwise;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class Server implements Runnable {
    private static final int BUFFER_SIZE = 1024;
    private static final String HOST = "localhost";

    private final ServerCommandExecutor commandExecutor;

    private final int port;
    private boolean isServerWorking;

    private ByteBuffer buffer;
    private Selector selector;

    public Server(int port, ServerCommandExecutor commandExecutor) {
        this.port = port;
        this.commandExecutor = commandExecutor;
    }

    public void run() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();

            configureServerSocketChannel(serverSocketChannel, selector);

            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerWorking = true;
            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            String clientInput = getClientInput(clientChannel);
                            if (clientInput == null) {
                                continue;
                            }
                            String output = commandExecutor
                                    .execute(ServerCommandExecutor
                                            .extractCommandAndArgs(buffer.array(), clientChannel.hashCode()));
                            writeClientOutput(clientChannel, output);
                            buffer.clear();
                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                        }
                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        }
    }

    public void stop() {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(HOST, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();
        buffer.put(new byte[BUFFER_SIZE]);
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }


        byte b = '\n';
        buffer.put(b);

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
        buffer.clear();
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }
}
