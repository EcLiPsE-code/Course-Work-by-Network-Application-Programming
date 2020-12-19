package by.gstu.beans.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpRequest extends InputStream {

    private enum Headers {
        METHOD,
        PROTOCOL,
        PROTOCOL_VERSION,
        HOST,
        USER_AGENT, //пользовательский клиент(информация о клиенте)
        ACCEPT, //поддерживаемые типы файлов
        ACCEPT_LANGUAGE, //поддерживаемый язык
        ACCEPT_ENCODING,
        CONNECTION,
        COOKIE,
        UPGRADE_INSECURE_REQUESTS,
        URL_PATH,
        CONTENT_TYPE,
        CONTENT_LENGTH,
        DATA,
        ORIGIN,
        REFERER; //адрес перехода(указывает адрес с которого выполнен переход)
    }

    private static final Logger logger = LogManager.getLogger();
    private static final Pattern HEAD_TYPE_PATTERN = Pattern.compile("([A-z-])+:.+");

    private static synchronized void parseHeader(Map<Headers, String> headers, BufferedReader reader) {
        try {
            String[] methodUrlProtocol = reader.readLine().split(" ");

            headers.put(Headers.METHOD, methodUrlProtocol[0]);
            headers.put(Headers.URL_PATH, methodUrlProtocol[1]);
            headers.put(Headers.PROTOCOL, methodUrlProtocol[2]);

            String line = reader.readLine();
            while (reader.ready() && HEAD_TYPE_PATTERN.matcher(line).find() && !line.isEmpty()) {
                String[] headEntry = line.split(":", 2);
                String headKind = headEntry[0].replaceAll("([a-z])-([A-Z]+)", "$1_$2")
                        .toUpperCase();
                try {
                    Headers head = Headers.valueOf(headKind);
                    headers.put(head, headEntry[1].substring(1));
                } catch (IllegalArgumentException e) {
                    logger.error(e + ". Head undefined: " + line);
                }
                line = reader.readLine();
            }
            if (headers.get(Headers.METHOD).equals("POST")) {
                int contentLength = Integer.parseInt(headers.get(Headers.CONTENT_LENGTH));

                char[] buff = new char[contentLength];
                int byteRecv = reader.read(buff, 0, contentLength);
                headers.put(Headers.DATA, String.valueOf(buff));
                System.out.println(headers.get(Headers.DATA));
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        System.out.println(headers);
    }

    private final BufferedReader reader;
    private final Map<Headers, String> headers;

    public HttpRequest(InputStream stream) {
        this.reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        headers = new EnumMap<>(Headers.class);
        parseHeader(headers, reader);
    }

    public String getRawData() {
        return headers.get(Headers.DATA);
    }

    public String getUrlOrigin() {
        return headers.get(Headers.ORIGIN);
    }

    public String getUrlContextPath() {
        return headers.get(Headers.URL_PATH);
    }

    public String getMethod() {
        return headers.get(Headers.METHOD);
    }

    @Override
    public int read() throws IOException {
        return reader.read();
    }
}
