import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static String host = "127.0.0.1";
    public static int port = 23334;

    public static void main(String[] args) throws IOException {
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(socketAddress);

        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer byteBuffer = ByteBuffer.allocate(2 << 10);
            String message;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Сообщения без пробелов:");

            while (true) {
                System.out.print("Введите ваше сообщение:");
                message = scanner.nextLine();
                if ("end".equals(message)) {
                    System.out.println(stringBuilder);
                    break;
                }
                socketChannel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
                int bytes = socketChannel.read(byteBuffer);
                stringBuilder.append(new String(byteBuffer.array(), 0, bytes,
                        StandardCharsets.UTF_8));
                byteBuffer.clear();
            }
        } catch (IOException err) {
            System.out.println(err.getMessage());
        } finally {
            socketChannel.close();
        }
    }
}
