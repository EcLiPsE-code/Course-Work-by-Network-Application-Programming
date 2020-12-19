package by.gstu.beans.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class HttpResponse extends OutputStream {
    private final PrintWriter writer;

    public HttpResponse(OutputStream outputStream) {
        writer = new PrintWriter(outputStream);
    }

    public void sendText(String content) {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=utf-8");
        writer.println();
        writer.write(content);
        writer.flush();
    }

    public void sendNotFound() {
        writer.println("HTTP/1.1 404 Page Not Found");
        writer.println();
        writer.flush();
    }

    @Override
    public void write(int b) throws IOException {
        writer.write(b);
    }
}
