import com.rm5248.serial.NoSuchPortException;
import com.rm5248.serial.NotASerialPortException;
import com.rm5248.serial.SerialPort;
import com.rm5248.serial.SerialPortBuilder;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class Uart {

    SerialPort port;
    public Uart(String portname) {
        try {
            SerialPortBuilder builder = new SerialPortBuilder();
            builder.setBaudRate( SerialPort.BaudRate.B9600 ).setPort(portname);
            port = builder.build();
        } catch (NoSuchPortException ex) {
            System.err.println( "That port doesn't exist!" );
        } catch (NotASerialPortException ex) {
            System.err.println( "That's not a serial port!" );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void uartwrite(char status) {
        OutputStream os = port.getOutputStream();
        try {
            os.write(status);
            //os.write(status.getBytes(StandardCharsets.UTF_8));
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
