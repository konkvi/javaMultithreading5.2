import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    public static String host = "127.0.0.1";
    public static int port = 23334;

    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind( new InetSocketAddress(host, port));

        while (serverChannel.isOpen()) {
            try (SocketChannel socketChannel = serverChannel.accept()) {
                final ByteBuffer byteBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(byteBuffer);
                    if (bytesCount == -1) break;
                    final String message = new String(byteBuffer.array(), 0, bytesCount,
                            StandardCharsets.UTF_8);
                    byteBuffer.clear();
                    socketChannel.write(ByteBuffer.wrap((stringCut(message)).
                            getBytes(StandardCharsets.UTF_8)));
                }
                serverChannel.close();
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    public static String stringCut(String string) {
        return string.replaceAll("\\s+","");
    }

}